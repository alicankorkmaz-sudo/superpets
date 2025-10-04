package com.alicankorkmaz.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import io.ktor.server.application.*

class FirebaseAuthService(private val application: Application) {

    fun verifyIdToken(idToken: String): FirebaseToken? {
        return try {
            FirebaseAuth.getInstance().verifyIdToken(idToken)
        } catch (e: Exception) {
            application.log.error("Failed to verify Firebase ID token", e)
            null
        }
    }

    fun getUserId(idToken: String): String? {
        return verifyIdToken(idToken)?.uid
    }

    fun getUserEmail(idToken: String): String? {
        return verifyIdToken(idToken)?.email
    }
}
