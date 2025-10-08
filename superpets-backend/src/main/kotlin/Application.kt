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

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
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
