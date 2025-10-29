package com.superpets.mobile.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.auth.AuthManager
import com.superpets.mobile.data.auth.AuthState
import com.superpets.mobile.data.models.EditHistory
import com.superpets.mobile.data.repository.SuperpetsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * ViewModel for Home screen
 *
 * Displays user's credit balance and recent edits
 * Automatically loads data when authentication state is ready
 */
class HomeViewModel(
    private val repository: SuperpetsRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var hasLoadedData = false

    init {
        // Observe auth state and load data only once when authenticated AND token is available
        viewModelScope.launch {
            authManager.authState
                .filterIsInstance<AuthState.Authenticated>()
                .filter { authState ->
                    // Filter out premature OAuth emissions before deep link arrives
                    // OAuth flow sets email to "Google User" or "Apple User" initially
                    val isRealEmail = authState.email.contains("@")
                    if (!isRealEmail) {
                        Napier.d("Skipping premature auth state (OAuth in progress): ${authState.email}")
                    }
                    isRealEmail
                }
                .collect { authState ->
                    if (hasLoadedData) {
                        Napier.d("Data already loaded, skipping duplicate auth emission")
                        return@collect
                    }

                    // For OAuth flows (Google/Apple), there's a delay between auth state
                    // becoming Authenticated and the token being available via deep link.
                    // Wait for token to be available before loading data.
                    Napier.d("Auth state is Authenticated for: ${authState.email}, waiting for token...")

                    // Retry up to 5 times with 500ms delay (max 2.5 seconds)
                    var token: String? = null
                    repeat(5) { attempt ->
                        token = authManager.getToken()
                        if (token != null) {
                            Napier.d("Token available after ${attempt * 500}ms, loading home data")
                            hasLoadedData = true
                            loadHomeData()
                            return@collect
                        }
                        if (attempt < 4) { // Don't delay on last attempt
                            delay(500)
                        }
                    }

                    if (token == null) {
                        Napier.e("Token not available after 2.5 seconds, aborting data load")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Authentication token not available. Please try again."
                        )
                    }
                }
        }
    }

    /**
     * Load home screen data (credits and recent edits)
     * Only called when auth state is confirmed to be Authenticated
     */
    private fun loadHomeData() {
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
     * Refresh home screen data (public method for manual refresh)
     */
    fun refresh() {
        Napier.d("Manual refresh triggered")
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
