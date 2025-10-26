package com.superpets.mobile.data.network

import com.superpets.mobile.data.auth.AuthTokenProvider
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured HttpClient instances
 */
object HttpClientFactory {

    /**
     * Backend API base URL
     */
    const val BASE_URL = "https://api.superpets.fun"

    /**
     * Create a configured HttpClient for Superpets API
     *
     * @param authTokenProvider Provider for authentication tokens
     * @param enableLogging Enable detailed HTTP logging for debugging
     * @return Configured HttpClient instance
     */
    fun create(
        authTokenProvider: AuthTokenProvider?,
        enableLogging: Boolean = true
    ): HttpClient {
        return HttpClient {
            // Base URL configuration
            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }

            // JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            // Authentication with Bearer token
            if (authTokenProvider != null) {
                install(Auth) {
                    bearer {
                        loadTokens {
                            val token = authTokenProvider.getToken()
                            token?.let {
                                BearerTokens(accessToken = it, refreshToken = "")
                            }
                        }

                        refreshTokens {
                            val token = authTokenProvider.getToken()
                            token?.let {
                                BearerTokens(accessToken = it, refreshToken = "")
                            }
                        }
                    }
                }
            }

            // Logging
            if (enableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.d(tag = "HttpClient", message = message)
                        }
                    }
                    level = LogLevel.INFO
                }
            }

            // Timeout configuration
            // Default timeouts for regular API calls (profile, credits, etc.)
            // Set to 45s to account for database connection pool delays under load
            // Image generation endpoints will override with longer timeouts
            install(HttpTimeout) {
                requestTimeoutMillis = 45_000 // 45 seconds default
                connectTimeoutMillis = 15_000 // 15 seconds to establish connection
                socketTimeoutMillis = 45_000  // 45 seconds for socket operations
            }

            // Default request configuration
            install(DefaultRequest) {
                header(HttpHeaders.Accept, ContentType.Application.Json)
            }
        }
    }
}
