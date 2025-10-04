package com.alicankorkmaz

import com.alicankorkmaz.config.FirebaseConfig
import com.alicankorkmaz.services.FirebaseAuthService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Initialize Firebase
    FirebaseConfig.initialize(this)

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        anyHost() // For development - restrict in production
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    val firebaseAuthService = FirebaseAuthService(this)

    install(Authentication) {
        bearer("firebase-auth") {
            authenticate { credential ->
                val decodedToken = firebaseAuthService.verifyIdToken(credential.token)
                if (decodedToken != null) {
                    UserIdPrincipal(decodedToken.uid)
                } else {
                    null
                }
            }
        }
    }

    configureRouting()
}
