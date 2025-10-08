package com.alicankorkmaz

import com.alicankorkmaz.database.DatabaseFactory
import com.alicankorkmaz.services.SupabaseAuthService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json
import io.sentry.Sentry
import io.sentry.SentryOptions

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Initialize Sentry for error tracking
    val sentryDsn = System.getenv("SENTRY_DSN")
    if (sentryDsn != null) {
        Sentry.init { options ->
            options.dsn = sentryDsn
            options.environment = System.getenv("SENTRY_ENVIRONMENT") ?: "production"
            options.release = "superpets-backend@0.0.1"
            options.tracesSampleRate = 1.0 // Capture 100% of transactions for performance monitoring
            options.isEnableUncaughtExceptionHandler = true
            options.isDebug = false
        }
        log.info("Sentry initialized for environment: ${System.getenv("SENTRY_ENVIRONMENT") ?: "production"}")
    } else {
        log.warn("SENTRY_DSN not configured - error tracking disabled")
    }

    // Initialize Supabase database connection
    DatabaseFactory.init(this)

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

        // Production domains
        allowHost("superpets.fun", schemes = listOf("https"))
        allowHost("superpets-ee0ab.web.app", schemes = listOf("https"))

        // Development domains
        allowHost("localhost:5173", schemes = listOf("http"))
        allowHost("localhost:8080", schemes = listOf("http"))
    }

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    val supabaseAuthService = SupabaseAuthService(this)

    install(Authentication) {
        bearer("supabase-auth") {
            authenticate { credential ->
                val user = supabaseAuthService.verifyToken(credential.token)
                if (user != null) {
                    UserIdPrincipal(user.sub)
                } else {
                    null
                }
            }
        }
    }

    configureRouting()
}
