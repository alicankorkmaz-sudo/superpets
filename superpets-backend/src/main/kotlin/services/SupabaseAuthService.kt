package com.alicankorkmaz.services

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import javax.crypto.SecretKey

class SupabaseAuthService(private val application: Application) {

    private val supabaseUrl = System.getenv("SUPABASE_URL")
        ?: throw IllegalStateException("SUPABASE_URL environment variable is not set")

    private val supabaseJwtSecret = System.getenv("SUPABASE_JWT_SECRET")
        ?: throw IllegalStateException("SUPABASE_JWT_SECRET environment variable is not set")

    private val secretKey: SecretKey = Keys.hmacShaKeyFor(supabaseJwtSecret.toByteArray())

    // Supabase JWT claims contain user information
    @Serializable
    data class SupabaseUser(
        val sub: String,  // user ID
        val email: String? = null,
        val role: String? = null
    )

    fun verifyToken(token: String): SupabaseUser? {
        return try {
            val claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload

            SupabaseUser(
                sub = claims.subject,
                email = claims["email"] as? String,
                role = claims["role"] as? String
            )
        } catch (e: Exception) {
            application.log.error("Failed to verify Supabase token", e)
            null
        }
    }

    fun getUserId(token: String): String? {
        return verifyToken(token)?.sub
    }

    fun getUserEmail(token: String): String? {
        return verifyToken(token)?.email
    }
}
