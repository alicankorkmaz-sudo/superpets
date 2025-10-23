package com.superpets.mobile.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.auth.AuthManager
import com.superpets.mobile.data.models.CreditTransaction
import com.superpets.mobile.data.models.User
import com.superpets.mobile.data.repository.SuperpetsRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Profile screen
 *
 * Manages user profile data, credit information, and transaction history
 */
class ProfileViewModel(
    private val repository: SuperpetsRepository,
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    /**
     * Load user profile data
     */
    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = repository.getUserProfile()

            result.fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        error = null
                    )
                    Napier.d("User profile loaded: ${user.email}, credits: ${user.credits}")
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load profile"
                    )
                    Napier.e("Failed to load user profile", exception)
                }
            )
        }
    }

    /**
     * Load transaction history
     */
    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingTransactions = true,
                transactionsError = null
            )

            val result = repository.getTransactions()

            result.fold(
                onSuccess = { transactions ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingTransactions = false,
                        transactions = transactions,
                        transactionsError = null
                    )
                    Napier.d("Loaded ${transactions.size} transactions")
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoadingTransactions = false,
                        transactionsError = exception.message ?: "Failed to load transactions"
                    )
                    Napier.e("Failed to load transactions", exception)
                }
            )
        }
    }

    /**
     * Refresh all profile data
     */
    fun refresh() {
        loadUserProfile()
        if (_uiState.value.showTransactions) {
            loadTransactions()
        }
    }

    /**
     * Toggle transaction history visibility
     */
    fun toggleTransactions() {
        val newShowTransactions = !_uiState.value.showTransactions
        _uiState.value = _uiState.value.copy(showTransactions = newShowTransactions)

        if (newShowTransactions && _uiState.value.transactions.isEmpty()) {
            loadTransactions()
        }
    }

    /**
     * Sign out the current user
     */
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSigningOut = true)

            val result = authManager.signOut()

            result.fold(
                onSuccess = {
                    Napier.d("User signed out successfully")
                    // Navigation handled by auth state change
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSigningOut = false,
                        error = exception.message ?: "Failed to sign out"
                    )
                    Napier.e("Failed to sign out", exception)
                }
            )
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Clear transactions error
     */
    fun clearTransactionsError() {
        _uiState.value = _uiState.value.copy(transactionsError = null)
    }
}

/**
 * UI state for Profile screen
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,

    // Transaction history
    val showTransactions: Boolean = false,
    val isLoadingTransactions: Boolean = false,
    val transactions: List<CreditTransaction> = emptyList(),
    val transactionsError: String? = null,

    // Sign out
    val isSigningOut: Boolean = false
)
