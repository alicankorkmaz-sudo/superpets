package com.alicankorkmaz.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.FileInputStream

object FirebaseConfig {
    fun initialize(application: Application) {
        try {
            val serviceAccountPath = application.environment.config
                .property("firebase.serviceAccountPath")
                .getString()

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(FileInputStream(serviceAccountPath)))
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                application.log.info("Firebase initialized successfully")
            }
        } catch (e: Exception) {
            application.log.error("Failed to initialize Firebase", e)
            throw e
        }
    }
}
