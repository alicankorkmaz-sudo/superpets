package com.superpets.mobile.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.models.EditHistory
import com.superpets.mobile.data.network.SuperpetsApiService
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the History screen
 * Manages edit history data and filtering
 */
class HistoryViewModel(
    private val apiService: SuperpetsApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadEditHistory()
    }

    /**
     * Load edit history from API
     */
    fun loadEditHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            apiService.getEditHistory().fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            edits = response.edits,
                            filteredEdits = response.edits,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    Napier.e("Failed to load edit history: ${error.message}", error)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Failed to load history: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    /**
     * Refresh edit history
     */
    fun refresh() {
        loadEditHistory()
    }

    /**
     * Filter by date (placeholder - implement sorting logic)
     */
    fun filterByDate() {
        val state = _uiState.value
        val sortedEdits = state.edits.sortedByDescending { it.timestamp }
        _uiState.update {
            it.copy(filteredEdits = sortedEdits)
        }
    }

    /**
     * Filter by hero (placeholder - implement hero filtering logic)
     */
    fun filterByHero() {
        // TODO: Implement hero filtering
        // For now, just refresh the list
        _uiState.update {
            it.copy(filteredEdits = it.edits)
        }
    }

    /**
     * Clear any error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * UI state for the History screen
 */
data class HistoryUiState(
    val edits: List<EditHistory> = emptyList(),
    val filteredEdits: List<EditHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
