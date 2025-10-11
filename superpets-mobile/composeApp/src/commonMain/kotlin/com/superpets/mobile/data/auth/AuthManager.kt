package com.superpets.mobile.data.auth

import io.github.aakira.napier.Napier
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages authentication state and operations using Supabase Auth
 */
class AuthManager : AuthTokenProvider {

    private val supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = SupabaseConfig.SUPABASE_URL,
        supabaseKey = SupabaseConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth)
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        // Initialize auth state
        checkAuthStatus()
    }

    /**
     * Check current authentication status
     */
    private fun checkAuthStatus() {
        try {
            val session = supabaseClient.auth.currentSessionOrNull()
            _authState.value = if (session != null) {
                AuthState.Authenticated(session.user?.email ?: "")
            } else {
                AuthState.Unauthenticated
            }
        } catch (e: Exception) {
            Napier.e("Error checking auth status", e)
            _authState.value = AuthState.Unauthenticated
        }
    }

    /**
     * Sign up with email and password
     */
    suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            _authState.value = AuthState.Authenticated(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Sign up failed", e)
            Result.failure(e)
        }
    }

    /**
     * Sign in with email and password
     */
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            _authState.value = AuthState.Authenticated(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Sign in failed", e)
            Result.failure(e)
        }
    }

    /**
     * Sign out
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            _authState.value = AuthState.Unauthenticated
            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Sign out failed", e)
            Result.failure(e)
        }
    }

    /**
     * Get current user's JWT token
     */
    override suspend fun getToken(): String? {
        return try {
            supabaseClient.auth.currentAccessTokenOrNull()
        } catch (e: Exception) {
            Napier.e("Error getting token", e)
            null
        }
    }

    /**
     * Check if user is authenticated
     */
    override suspend fun isAuthenticated(): Boolean {
        return supabaseClient.auth.currentSessionOrNull() != null
    }

    /**
     * Get current user email
     */
    fun getCurrentUserEmail(): String? {
        return supabaseClient.auth.currentUserOrNull()?.email
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return supabaseClient.auth.currentUserOrNull()?.id
    }

    /**
     * Reset password (send reset email)
     */
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            supabaseClient.auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Password reset failed", e)
            Result.failure(e)
        }
    }
}

/**
 * Authentication state
 */
sealed class AuthState {
    data object Loading : AuthState()
    data object Unauthenticated : AuthState()
    data class Authenticated(val email: String) : AuthState()
}
