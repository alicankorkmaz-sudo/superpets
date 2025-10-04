package com.alicankorkmaz.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import java.io.FileInputStream

object FirebaseConfig {
    fun initialize(application: Application) {
        try {
            // Force IPv4 for all network connections including gRPC/Firestore
            // This must be set before any gRPC channels are created
            System.setProperty("java.net.preferIPv4Stack", "true")
            System.setProperty("java.net.preferIPv4Addresses", "true")
            System.setProperty("io.grpc.internal.DnsNameResolverProvider.enable_service_config", "false")

            val serviceAccountPath = application.environment.config
                .property("firebase.serviceAccountPath")
                .getString()

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(FileInputStream(serviceAccountPath)))
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                application.log.info("Firebase initialized successfully with IPv4 enforcement")
            }
        } catch (e: Exception) {
            application.log.error("Failed to initialize Firebase", e)
            throw e
        }
    }
}
