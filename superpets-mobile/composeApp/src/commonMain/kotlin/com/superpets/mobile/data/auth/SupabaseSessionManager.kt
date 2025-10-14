package com.superpets.mobile.data.auth

import com.russhwolf.settings.Settings
import io.github.aakira.napier.Napier
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Custom SessionManager for Supabase Auth that persists sessions using multiplatform Settings
 *
 * This enables session persistence across app restarts by storing the session
 * in platform-specific secure storage (Keychain on iOS, EncryptedSharedPreferences on Android)
 */
class SupabaseSessionManager(
    private val settings: Settings
) : SessionManager {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun deleteSession() {
        Napier.d("Deleting session from storage")
        settings.remove(SESSION_KEY)
    }

    override suspend fun loadSession(): UserSession? {
        Napier.d("loadSession() called - checking storage...")
        val sessionJson = settings.getStringOrNull(SESSION_KEY)

        if (sessionJson == null) {
            Napier.d("No session found in storage")
            return null
        }

        return try {
            val session = json.decodeFromString<UserSession>(sessionJson)
            Napier.d("✅ Successfully loaded session from storage! User: ${session.user?.email}")
            session
        } catch (e: Exception) {
            Napier.e("Failed to deserialize session, clearing invalid data", e)
            settings.remove(SESSION_KEY)
            null
        }
    }

    override suspend fun saveSession(session: UserSession) {
        try {
            val sessionJson = json.encodeToString(session)
            settings.putString(SESSION_KEY, sessionJson)
            Napier.d("Session saved to storage")
        } catch (e: Exception) {
            Napier.e("Failed to save session", e)
        }
    }

    companion object {
        private const val SESSION_KEY = "supabase_user_session"
    }
}
