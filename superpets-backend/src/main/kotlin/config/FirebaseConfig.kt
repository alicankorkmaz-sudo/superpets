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

            val credentials = GoogleCredentials.fromStream(FileInputStream(serviceAccountPath))

            val options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId("superpets-a42c5")
                .setDatabaseUrl("https://superpets-a42c5.firebaseio.com")
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                application.log.info("Firebase initialized successfully for project: superpets-a42c5")
            }
        } catch (e: Exception) {
            application.log.error("Failed to initialize Firebase", e)
            throw e
        }
    }
}
