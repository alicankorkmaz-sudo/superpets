package com.superpets.mobile.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.models.EditHistory
import com.superpets.mobile.data.repository.SuperpetsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Home screen
 *
 * Displays user's credit balance and recent edits
 */
class HomeViewModel(
    private val repository: SuperpetsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    /**
     * Load home screen data (credits and recent edits)
     */
    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            // Load credits
            val creditsResult = repository.getUserCredits()

            // Load recent edits (last 3)
            val editsResult = repository.getEditHistory()

            creditsResult.fold(
                onSuccess = { credits ->
                    _uiState.value = _uiState.value.copy(
                        credits = credits
                    )
                },
                onFailure = { exception ->
                    Napier.e("Failed to load credits", exception)
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to load data"
                    )
                }
            )

            editsResult.fold(
                onSuccess = { edits ->
                    _uiState.value = _uiState.value.copy(
                        recentEdits = edits.take(3), // Show only 3 most recent
                        isLoading = false
                    )
                    Napier.d("Loaded ${edits.size} edits")
                },
                onFailure = { exception ->
                    Napier.e("Failed to load edit history", exception)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load data"
                    )
                }
            )
        }
    }

    /**
     * Refresh home screen data
     */
    fun refresh() {
        loadHomeData()
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * UI state for Home screen
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val credits: Int = 0,
    val recentEdits: List<EditHistory> = emptyList(),
    val error: String? = null
)
