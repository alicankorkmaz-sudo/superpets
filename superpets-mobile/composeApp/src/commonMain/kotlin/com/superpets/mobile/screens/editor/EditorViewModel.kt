package com.superpets.mobile.screens.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.core.image.ImageCompressor
import com.superpets.mobile.data.models.EditImageResponse
import com.superpets.mobile.data.models.Hero
import com.superpets.mobile.data.models.User
import com.superpets.mobile.data.network.ApiException
import com.superpets.mobile.data.network.SuperpetsApiService
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Editor screen
 * Manages image selection, hero selection, and image generation
 */
class EditorViewModel(
    private val apiService: SuperpetsApiService,
    private val imageCompressor: ImageCompressor
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
        loadHeroes()
    }

    /**
     * Load user profile to get credit balance
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingProfile = true) }
            apiService.getUserProfile().fold(
                onSuccess = { user ->
                    _uiState.update {
                        it.copy(
                            userProfile = user,
                            isLoadingProfile = false
                        )
                    }
                },
                onFailure = { error ->
                    Napier.e("Failed to load user profile: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            isLoadingProfile = false,
                            error = "Failed to load profile: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    /**
     * Load available heroes from API
     */
    private fun loadHeroes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingHeroes = true) }
            apiService.getHeroes().fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            heroes = response.heroes,
                            isLoadingHeroes = false
                        )
                    }
                },
                onFailure = { error ->
                    Napier.e("Failed to load heroes: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            isLoadingHeroes = false,
                            error = "Failed to load heroes: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    /**
     * Add a single image (for camera capture)
     */
    fun addImage(image: ByteArray) {
        addImages(listOf(image))
    }

    /**
     * Set images (replaces existing images, for gallery selection)
     */
    fun setImages(images: List<ByteArray>) {
        _uiState.update { state ->
            val newImages = images.take(10) // Max 10 images
            state.copy(selectedImages = newImages)
        }
    }

    /**
     * Add selected images
     */
    fun addImages(images: List<ByteArray>) {
        _uiState.update { state ->
            val currentImages = state.selectedImages
            val newImages = (currentImages + images).take(10) // Max 10 images
            state.copy(selectedImages = newImages)
        }
    }

    /**
     * Remove image at specific index
     */
    fun removeImage(index: Int) {
        _uiState.update { state ->
            val newImages = state.selectedImages.toMutableList()
            if (index in newImages.indices) {
                newImages.removeAt(index)
            }
            state.copy(selectedImages = newImages)
        }
    }

    /**
     * Select a hero
     */
    fun selectHero(hero: Hero) {
        _uiState.update { it.copy(selectedHero = hero) }
    }

    /**
     * Update number of output images
     */
    fun updateNumOutputs(num: Int) {
        val validNum = num.coerceIn(1, 10)
        _uiState.update { it.copy(numOutputs = validNum) }
    }

    /**
     * Generate superhero images
     */
    fun generateImages(onSuccess: (EditImageResponse) -> Unit) {
        val state = _uiState.value

        // Validation
        if (state.selectedImages.isEmpty()) {
            _uiState.update { it.copy(error = "Please select at least one image") }
            return
        }

        if (state.selectedHero == null) {
            _uiState.update { it.copy(error = "Please select a hero") }
            return
        }

        val creditsCost = state.numOutputs
        if (state.userProfile != null && state.userProfile.credits < creditsCost) {
            _uiState.update {
                it.copy(error = "Insufficient credits. You need $creditsCost credits but only have ${state.userProfile.credits}.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true, error = null, generationProgress = 0f) }

            try {
                // Compress images before upload
                val compressedImages = state.selectedImages.mapIndexed { index, imageData ->
                    _uiState.update { it.copy(generationProgress = (index.toFloat() / state.selectedImages.size) * 0.3f) }
                    imageCompressor.compress(imageData)
                }

                Napier.d("Compressed ${compressedImages.size} images. Sizes: ${compressedImages.map { "${it.size / 1024}KB" }}")

                // Upload and generate
                _uiState.update { it.copy(generationProgress = 0.4f) }

                apiService.uploadAndEditImages(
                    imageData = compressedImages,
                    heroId = state.selectedHero.id,
                    numImages = state.numOutputs
                ).fold(
                    onSuccess = { response ->
                        // Extract image URLs from response
                        val imageUrls = response.images.map { it.url }

                        _uiState.update {
                            it.copy(
                                isGenerating = false,
                                generationProgress = 1f,
                                generatedImageUrls = imageUrls,
                                generationComplete = true
                            )
                        }
                        // Refresh user profile to update credits
                        loadUserProfile()
                        onSuccess(response)
                    },
                    onFailure = { error ->
                        val errorMessage = when (error) {
                            is ApiException -> {
                                when (error.statusCode) {
                                    402 -> "Insufficient credits. Please purchase more credits."
                                    429 -> "Rate limit exceeded. Please try again in a moment."
                                    else -> error.message ?: "Unknown error occurred"
                                }
                            }
                            else -> error.message ?: "Failed to generate images"
                        }

                        Napier.e("Image generation failed: $errorMessage", error)
                        _uiState.update {
                            it.copy(
                                isGenerating = false,
                                error = errorMessage,
                                generationProgress = 0f
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                Napier.e("Unexpected error during image generation: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        error = "Unexpected error: ${e.message}",
                        generationProgress = 0f
                    )
                }
            }
        }
    }

    /**
     * Clear any error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    /**
     * Reset editor state
     */
    fun reset() {
        _uiState.update {
            EditorUiState(
                userProfile = it.userProfile,
                heroes = it.heroes
            )
        }
    }
}

/**
 * UI state for the Editor screen
 */
data class EditorUiState(
    val userProfile: User? = null,
    val heroes: List<Hero> = emptyList(),
    val selectedImages: List<ByteArray> = emptyList(),
    val selectedHero: Hero? = null,
    val numOutputs: Int = 5,
    val isLoadingProfile: Boolean = false,
    val isLoadingHeroes: Boolean = false,
    val isGenerating: Boolean = false,
    val generationProgress: Float = 0f,
    val generatedImageUrls: List<String>? = null,
    val generationComplete: Boolean = false,
    val error: String? = null
)
