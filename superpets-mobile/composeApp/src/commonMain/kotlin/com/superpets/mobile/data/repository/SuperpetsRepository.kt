package com.superpets.mobile.data.repository

import com.superpets.mobile.data.models.*
import com.superpets.mobile.data.network.SuperpetsApiService
import io.github.aakira.napier.Napier

/**
 * Repository layer for Superpets API
 *
 * This provides a clean interface for ViewModels to interact with the API,
 * handling business logic and data transformation.
 *
 * Example usage in a ViewModel:
 * ```
 * class HomeViewModel(private val repository: SuperpetsRepository) : ViewModel() {
 *     fun loadHeroes() {
 *         viewModelScope.launch {
 *             repository.getHeroes()
 *                 .onSuccess { heroes -> _heroesState.value = heroes }
 *                 .onFailure { error -> _errorState.value = error.message }
 *         }
 *     }
 * }
 * ```
 */
class SuperpetsRepository(
    private val apiService: SuperpetsApiService
) {

    /**
     * Get all available heroes
     * Public endpoint - no authentication required
     */
    suspend fun getHeroes(): Result<List<Hero>> {
        return apiService.getHeroes()
            .map { it.heroes }
            .onSuccess { Napier.d("Fetched ${it.size} heroes") }
            .onFailure { Napier.e("Failed to fetch heroes", it) }
    }

    /**
     * Get user profile (auto-creates with 5 credits on first call)
     * Requires authentication
     */
    suspend fun getUserProfile(): Result<User> {
        return apiService.getUserProfile()
            .onSuccess { Napier.d("User profile: ${it.email}, credits: ${it.credits}") }
            .onFailure { Napier.e("Failed to fetch user profile", it) }
    }

    /**
     * Get user's current credit balance
     * Requires authentication
     */
    suspend fun getUserCredits(): Result<Int> {
        return apiService.getUserCredits()
            .map { it.credits }
            .onSuccess { Napier.d("User credits: $it") }
            .onFailure { Napier.e("Failed to fetch credits", it) }
    }

    /**
     * Get user's transaction history
     * Requires authentication
     */
    suspend fun getTransactions(): Result<List<CreditTransaction>> {
        return apiService.getTransactions()
            .map { it.transactions }
            .onSuccess { Napier.d("Fetched ${it.size} transactions") }
            .onFailure { Napier.e("Failed to fetch transactions", it) }
    }

    /**
     * Get user's edit history
     * Requires authentication
     */
    suspend fun getEditHistory(): Result<List<EditHistory>> {
        return apiService.getEditHistory()
            .map { it.edits }
            .onSuccess { Napier.d("Fetched ${it.size} edits") }
            .onFailure { Napier.e("Failed to fetch edit history", it) }
    }

    /**
     * Upload and edit images
     *
     * @param imageData List of compressed image byte arrays (max 10MB each, 2048x2048 recommended)
     * @param heroId ID of the hero to transform the pet into
     * @param numImages Number of output images to generate (1-10, each costs 1 credit)
     *
     * Requires authentication and sufficient credits (numImages credits)
     */
    suspend fun uploadAndEditImages(
        imageData: List<ByteArray>,
        heroId: String,
        numImages: Int
    ): Result<EditImageResponse> {
        require(imageData.isNotEmpty()) { "At least one image is required" }
        require(numImages in 1..10) { "numImages must be between 1 and 10" }

        Napier.d("Uploading ${imageData.size} images, generating $numImages outputs with hero: $heroId")

        return apiService.uploadAndEditImages(imageData, heroId, numImages)
            .onSuccess { response ->
                Napier.d("Generated ${response.outputs.size} images")
            }
            .onFailure { error ->
                Napier.e("Image upload/edit failed", error)
            }
    }

    /**
     * Edit images from URLs (if images are already hosted)
     *
     * @param inputImages List of image URLs
     * @param heroId ID of the hero to transform the pet into
     * @param numImages Number of output images to generate (1-10)
     *
     * Requires authentication and sufficient credits
     */
    suspend fun editImage(
        inputImages: List<String>,
        heroId: String,
        numImages: Int
    ): Result<EditImageResponse> {
        require(inputImages.isNotEmpty()) { "At least one input image is required" }
        require(numImages in 1..10) { "numImages must be between 1 and 10" }

        return apiService.editImage(inputImages, heroId, numImages)
            .onSuccess { Napier.d("Generated ${it.outputs.size} images") }
            .onFailure { Napier.e("Image edit failed", it) }
    }

    /**
     * Create Stripe checkout session for purchasing credits
     *
     * @param priceId Stripe price ID for the credit package
     * @param successUrl URL to redirect to after successful payment
     * @param cancelUrl URL to redirect to if payment is cancelled
     */
    suspend fun createCheckoutSession(
        priceId: String,
        successUrl: String,
        cancelUrl: String
    ): Result<CheckoutSessionResponse> {
        return apiService.createCheckoutSession(priceId, successUrl, cancelUrl)
            .onSuccess { Napier.d("Created checkout session: ${it.sessionId}") }
            .onFailure { Napier.e("Failed to create checkout session", it) }
    }
}
