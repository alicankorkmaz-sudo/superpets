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
                    applyFilters()
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
     * Set date filter
     */
    fun setDateFilter(filter: DateFilter) {
        _uiState.update { it.copy(dateFilter = filter) }
        applyFilters()
    }

    /**
     * Set hero filter
     */
    fun setHeroFilter(heroName: String?) {
        _uiState.update { it.copy(selectedHeroFilter = heroName) }
        applyFilters()
    }

    /**
     * Clear all filters
     */
    fun clearFilters() {
        _uiState.update {
            it.copy(
                dateFilter = DateFilter.NEWEST_FIRST,
                selectedHeroFilter = null
            )
        }
        applyFilters()
    }

    /**
     * Apply current filters to edit list
     */
    private fun applyFilters() {
        val state = _uiState.value
        var filtered = state.edits

        // Apply hero filter
        state.selectedHeroFilter?.let { heroName ->
            filtered = filtered.filter { edit ->
                extractHeroName(edit.prompt).equals(heroName, ignoreCase = true)
            }
        }

        // Apply date sorting
        filtered = when (state.dateFilter) {
            DateFilter.NEWEST_FIRST -> filtered.sortedByDescending { it.timestamp }
            DateFilter.OLDEST_FIRST -> filtered.sortedBy { it.timestamp }
        }

        _uiState.update { it.copy(filteredEdits = filtered) }
    }

    /**
     * Extract hero name from prompt
     */
    private fun extractHeroName(prompt: String): String {
        val regex = """Transform the pet into ([\w\s]+)\.""".toRegex()
        val match = regex.find(prompt)
        return match?.groupValues?.getOrNull(1)?.trim() ?: "Superpet"
    }

    /**
     * Get unique hero names from all edits
     */
    fun getUniqueHeroNames(): List<String> {
        return _uiState.value.edits
            .map { extractHeroName(it.prompt) }
            .distinct()
            .sorted()
    }

    /**
     * Clear any error message
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * Date filter options
 */
enum class DateFilter(val label: String) {
    NEWEST_FIRST("Newest First"),
    OLDEST_FIRST("Oldest First")
}

/**
 * UI state for the History screen
 */
data class HistoryUiState(
    val edits: List<EditHistory> = emptyList(),
    val filteredEdits: List<EditHistory> = emptyList(),
    val dateFilter: DateFilter = DateFilter.NEWEST_FIRST,
    val selectedHeroFilter: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
