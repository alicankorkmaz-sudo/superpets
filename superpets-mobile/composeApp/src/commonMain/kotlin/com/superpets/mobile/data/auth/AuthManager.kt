package com.superpets.mobile.data.auth

import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

/**
 * Manages authentication state and operations using Supabase Auth
 *
 * @param settings Settings instance for session persistence
 * @param httpClientEngine Platform-specific Ktor engine (OkHttp for Android, Darwin for iOS)
 */
class AuthManager(
    private val settings: Settings,
    private val httpClientEngine: HttpClientEngine? = null
) : AuthTokenProvider {

    private val supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = SupabaseConfig.SUPABASE_URL,
        supabaseKey = SupabaseConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth) {
            // Use custom session manager for persistence across app restarts
            sessionManager = SupabaseSessionManager(settings)

            // Enable automatic session restoration and token refresh
            alwaysAutoRefresh = true
            autoLoadFromStorage = true

            // Configure for email confirmation flow
            scheme = "superpets"
            host = "auth"
        }

        // Configure with platform-specific Ktor engine if provided
        httpClientEngine?.let {
            httpEngine = it
        }
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        // Wait for session to load from storage before checking status
        // Supabase loads the session asynchronously, so we need to wait for it
        MainScope().launch {
            delay(500) // Give Supabase time to load from storage
            checkAuthStatus()
        }
    }

    /**
     * Check current authentication status
     */
    private fun checkAuthStatus() {
        try {
            val session = supabaseClient.auth.currentSessionOrNull()
            _authState.value = if (session != null) {
                Napier.d("Auth check: User authenticated (${session.user?.email})")
                AuthState.Authenticated(session.user?.email ?: "")
            } else {
                Napier.d("Auth check: User not authenticated")
                AuthState.Unauthenticated
            }
        } catch (e: Exception) {
            Napier.e("Error checking auth status", e)
            _authState.value = AuthState.Unauthenticated
        }
    }

    /**
     * Sign up with email and password
     * Returns SignUpResult indicating whether email confirmation is required
     */
    suspend fun signUp(email: String, password: String): Result<SignUpResult> {
        return try {
            val response = supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            // Check if email confirmation is required
            // If user exists but no session, email confirmation is enabled
            val requiresConfirmation = response != null && supabaseClient.auth.currentSessionOrNull() == null

            if (requiresConfirmation) {
                Napier.d("Sign up successful - email confirmation required")
                _authState.value = AuthState.Unauthenticated
                Result.success(SignUpResult.ConfirmationRequired(email))
            } else {
                Napier.d("Sign up successful - auto authenticated")
                _authState.value = AuthState.Authenticated(email)
                Result.success(SignUpResult.Authenticated)
            }
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

    /**
     * Handle deep link from email confirmation
     * @param url Deep link URL containing access_token and refresh_token in fragment
     * Example: superpets://auth#access_token=...&refresh_token=...&type=signup
     */
    suspend fun handleDeepLink(url: String): Result<Unit> {
        return try {
            Napier.d("Handling deep link: $url")

            // Extract tokens from URL fragment (after #)
            val fragment = url.substringAfter("#", "")
            if (fragment.isEmpty()) {
                Napier.e("No fragment found in deep link URL")
                return Result.failure(Exception("Invalid deep link format"))
            }

            // Parse fragment parameters
            val params = fragment.split("&").associate {
                val (key, value) = it.split("=")
                key to value
            }

            val accessToken = params["access_token"]
            val refreshToken = params["refresh_token"]
            val expiresIn = params["expires_in"]?.toLongOrNull() ?: 3600L
            val type = params["type"]

            Napier.d("Deep link type: $type, has access token: ${accessToken != null}, has refresh token: ${refreshToken != null}")

            if (accessToken == null || refreshToken == null) {
                Napier.e("Missing required tokens in deep link")
                return Result.failure(Exception("Missing authentication tokens"))
            }

            // Calculate expiration instant
            val expiresAt = Clock.System.now().plus(expiresIn.seconds)

            // Manually create and save the session
            // First, we need to fetch user info using the access token
            try {
                // Set the access token temporarily to fetch user info
                val tempSession = UserSession(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    expiresIn = expiresIn,
                    expiresAt = expiresAt,
                    tokenType = "bearer",
                    user = null // Will be populated when we fetch user
                )

                // Save the session - this will make the auth client use this token
                supabaseClient.auth.sessionManager.saveSession(tempSession)
                Napier.d("Session saved, waiting for auth client to load it...")

                // Give the auth client time to reload the session from storage
                // and fetch user info automatically
                delay(1000)

                // Force the auth client to reload the session by getting current user
                // This will trigger the session loading mechanism
                var session = supabaseClient.auth.currentSessionOrNull()

                // If still no user info, try refreshing using the refresh token
                if (session?.user == null && refreshToken.isNotEmpty()) {
                    Napier.d("User info not loaded yet, attempting manual refresh with token...")
                    try {
                        // Use the refresh token to get a fresh session with user info
                        supabaseClient.auth.refreshSession(refreshToken)
                        Napier.d("Session refreshed successfully")

                        // Get the updated session
                        session = supabaseClient.auth.currentSessionOrNull()
                    } catch (refreshError: Exception) {
                        Napier.e("Error manually refreshing with token", refreshError)
                    }
                }

                // Check final session state
                if (session?.user != null) {
                    val email = session.user?.email ?: ""
                    _authState.value = AuthState.Authenticated(email)
                    Napier.d("Deep link handled successfully - user authenticated: $email")
                    Result.success(Unit)
                } else {
                    Napier.w("Session created but user info not available, will retry on next auth check")
                    // The session is still valid even without immediate user details
                    // Schedule a check after a delay
                    MainScope().launch {
                        delay(2000)
                        checkAuthStatus()
                    }
                    Result.success(Unit)
                }
            } catch (sessionError: Exception) {
                Napier.e("Error creating session from tokens", sessionError)
                Result.failure(sessionError)
            }
        } catch (e: Exception) {
            Napier.e("Failed to handle deep link", e)
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

/**
 * Sign up result
 */
sealed class SignUpResult {
    data object Authenticated : SignUpResult()
    data class ConfirmationRequired(val email: String) : SignUpResult()
}
