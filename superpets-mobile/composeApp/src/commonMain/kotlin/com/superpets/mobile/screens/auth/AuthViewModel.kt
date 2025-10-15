package com.superpets.mobile.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.auth.AuthManager
import com.superpets.mobile.data.auth.AuthState
import com.superpets.mobile.data.auth.SignUpResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for authentication screens (Login and Signup)
 *
 * Manages authentication state and operations using AuthManager
 */
class AuthViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    // Auth state from AuthManager
    val authState: StateFlow<AuthState> = authManager.authState

    // UI state for login screen
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    // UI state for signup screen
    private val _signupUiState = MutableStateFlow(SignupUiState())
    val signupUiState: StateFlow<SignupUiState> = _signupUiState.asStateFlow()

    /**
     * Sign in with email and password
     */
    fun signIn(email: String, password: String) {
        // Basic validation
        if (email.isBlank() || password.isBlank()) {
            _loginUiState.value = _loginUiState.value.copy(
                error = "Please enter email and password"
            )
            return
        }

        if (!isValidEmail(email)) {
            _loginUiState.value = _loginUiState.value.copy(
                error = "Please enter a valid email address"
            )
            return
        }

        viewModelScope.launch {
            _loginUiState.value = _loginUiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = authManager.signIn(email, password)

            result.fold(
                onSuccess = {
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                    Napier.d("Sign in successful")
                },
                onFailure = { exception ->
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = getErrorMessage(exception)
                    )
                    Napier.e("Sign in failed: ${exception.message}", exception)
                }
            )
        }
    }

    /**
     * Sign in with Google OAuth
     */
    fun signInWithGoogle() {
        viewModelScope.launch {
            _loginUiState.value = _loginUiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = authManager.signInWithGoogle()

            result.fold(
                onSuccess = {
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                    Napier.d("Google sign in successful")
                },
                onFailure = { exception ->
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = getErrorMessage(exception)
                    )
                    Napier.e("Google sign in failed: ${exception.message}", exception)
                }
            )
        }
    }

    /**
     * Sign in with Apple OAuth
     */
    fun signInWithApple() {
        viewModelScope.launch {
            _loginUiState.value = _loginUiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = authManager.signInWithApple()

            result.fold(
                onSuccess = {
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                    Napier.d("Apple sign in successful")
                },
                onFailure = { exception ->
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = getErrorMessage(exception)
                    )
                    Napier.e("Apple sign in failed: ${exception.message}", exception)
                }
            )
        }
    }

    /**
     * Sign up with email and password
     */
    fun signUp(email: String, password: String, confirmPassword: String) {
        // Validation
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _signupUiState.value = _signupUiState.value.copy(
                error = "Please fill in all fields"
            )
            return
        }

        if (!isValidEmail(email)) {
            _signupUiState.value = _signupUiState.value.copy(
                error = "Please enter a valid email address"
            )
            return
        }

        if (password.length < 6) {
            _signupUiState.value = _signupUiState.value.copy(
                error = "Password must be at least 6 characters"
            )
            return
        }

        if (password != confirmPassword) {
            _signupUiState.value = _signupUiState.value.copy(
                error = "Passwords do not match"
            )
            return
        }

        viewModelScope.launch {
            _signupUiState.value = _signupUiState.value.copy(
                isLoading = true,
                error = null,
                confirmationPending = false
            )

            val result = authManager.signUp(email, password)

            result.fold(
                onSuccess = { signUpResult ->
                    when (signUpResult) {
                        is SignUpResult.Authenticated -> {
                            _signupUiState.value = _signupUiState.value.copy(
                                isLoading = false,
                                error = null,
                                confirmationPending = false
                            )
                            Napier.d("Sign up successful - auto authenticated")
                        }
                        is SignUpResult.ConfirmationRequired -> {
                            _signupUiState.value = _signupUiState.value.copy(
                                isLoading = false,
                                error = null,
                                confirmationPending = true,
                                confirmationEmail = signUpResult.email
                            )
                            Napier.d("Sign up successful - confirmation required for ${signUpResult.email}")
                        }
                    }
                },
                onFailure = { exception ->
                    _signupUiState.value = _signupUiState.value.copy(
                        isLoading = false,
                        error = getErrorMessage(exception),
                        confirmationPending = false
                    )
                    Napier.e("Sign up failed: ${exception.message}", exception)
                }
            )
        }
    }

    /**
     * Reset password (send reset email)
     */
    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _loginUiState.value = _loginUiState.value.copy(
                error = "Please enter your email address"
            )
            return
        }

        if (!isValidEmail(email)) {
            _loginUiState.value = _loginUiState.value.copy(
                error = "Please enter a valid email address"
            )
            return
        }

        viewModelScope.launch {
            _loginUiState.value = _loginUiState.value.copy(
                isLoading = true,
                error = null
            )

            val result = authManager.resetPassword(email)

            result.fold(
                onSuccess = {
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = null,
                        successMessage = "Password reset email sent! Check your inbox."
                    )
                    Napier.d("Password reset email sent")
                },
                onFailure = { exception ->
                    _loginUiState.value = _loginUiState.value.copy(
                        isLoading = false,
                        error = getErrorMessage(exception)
                    )
                    Napier.e("Password reset failed: ${exception.message}", exception)
                }
            )
        }
    }

    /**
     * Clear error messages
     */
    fun clearLoginError() {
        _loginUiState.value = _loginUiState.value.copy(error = null, successMessage = null)
    }

    fun clearSignupError() {
        _signupUiState.value = _signupUiState.value.copy(error = null)
    }

    /**
     * Clear confirmation pending state
     * Call this when user wants to try a different email or navigates away
     */
    fun clearConfirmationPending() {
        _signupUiState.value = _signupUiState.value.copy(
            confirmationPending = false,
            confirmationEmail = null,
            error = null
        )
    }

    /**
     * Validate email format
     */
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    /**
     * Convert exception to user-friendly error message
     */
    private fun getErrorMessage(exception: Throwable): String {
        val message = exception.message ?: return "An error occurred. Please try again."

        return when {
            message.contains("Invalid login credentials", ignoreCase = true) ->
                "Invalid email or password"
            message.contains("Email not confirmed", ignoreCase = true) ->
                "Please verify your email address"
            message.contains("already exists", ignoreCase = true) ||
            message.contains("User already registered", ignoreCase = true) ||
            message.contains("already registered", ignoreCase = true) ->
                "An account with this email already exists. Please sign in instead."
            message.contains("network", ignoreCase = true) ->
                "Network error. Please check your connection."
            message.contains("timeout", ignoreCase = true) ->
                "Request timed out. Please try again."
            else -> message // Show the actual error message if it's specific
        }
    }
}

/**
 * UI state for login screen
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

/**
 * UI state for signup screen
 */
data class SignupUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val confirmationPending: Boolean = false,
    val confirmationEmail: String? = null
)
