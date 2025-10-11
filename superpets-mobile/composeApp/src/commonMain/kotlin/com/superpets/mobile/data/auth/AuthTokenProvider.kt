package com.superpets.mobile.data.auth

/**
 * Interface for providing authentication tokens
 * Implementation will use Supabase Auth SDK to get current user's JWT token
 */
interface AuthTokenProvider {
    /**
     * Get the current user's authentication token
     * @return JWT token or null if user is not authenticated
     */
    suspend fun getToken(): String?

    /**
     * Check if user is currently authenticated
     */
    suspend fun isAuthenticated(): Boolean
}
