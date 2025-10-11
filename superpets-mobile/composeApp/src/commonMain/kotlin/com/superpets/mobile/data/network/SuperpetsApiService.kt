package com.superpets.mobile.data.network

import com.superpets.mobile.data.models.*
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * API service for Superpets backend
 * Provides all REST API endpoints for the mobile app
 */
class SuperpetsApiService(private val httpClient: HttpClient) {

    /**
     * Get all available heroes (public endpoint, no auth required)
     */
    suspend fun getHeroes(): Result<HeroesResponse> = safeApiCall {
        httpClient.get("/heroes").body()
    }

    /**
     * Get user profile (auto-creates with 5 credits if first time)
     */
    suspend fun getUserProfile(): Result<User> = safeApiCall {
        httpClient.get("/user/profile").body()
    }

    /**
     * Get user's credit balance
     */
    suspend fun getUserCredits(): Result<CreditsResponse> = safeApiCall {
        httpClient.get("/user/credits").body()
    }

    /**
     * Get user's transaction history
     */
    suspend fun getTransactions(): Result<TransactionsResponse> = safeApiCall {
        httpClient.get("/user/transactions").body()
    }

    /**
     * Get user's edit history
     */
    suspend fun getEditHistory(): Result<EditHistoryResponse> = safeApiCall {
        httpClient.get("/user/edits").body()
    }

    /**
     * Edit images from URLs
     * @param inputImages List of image URLs to edit
     * @param heroId ID of the hero to transform pet into
     * @param numImages Number of output images to generate (1-10)
     */
    suspend fun editImage(
        inputImages: List<String>,
        heroId: String,
        numImages: Int = 1
    ): Result<EditImageResponse> = safeApiCall {
        val request = EditImageRequest(
            inputImages = inputImages,
            heroId = heroId,
            numImages = numImages
        )
        httpClient.post("/nano-banana/edit") {
            setBody(request)
        }.body()
    }

    /**
     * Upload and edit images (multipart/form-data)
     * @param imageData List of image byte arrays
     * @param heroId ID of the hero to transform pet into
     * @param numImages Number of output images to generate (1-10)
     */
    suspend fun uploadAndEditImages(
        imageData: List<ByteArray>,
        heroId: String,
        numImages: Int = 1
    ): Result<EditImageResponse> = safeApiCall {
        httpClient.submitFormWithBinaryData(
            url = "/nano-banana/upload-and-edit",
            formData = formData {
                // Add hero_id and num_images
                append("hero_id", heroId)
                append("num_images", numImages.toString())

                // Add image files
                imageData.forEachIndexed { index, bytes ->
                    append("images", bytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"image_$index.jpg\"")
                    })
                }
            }
        ) {
            onUpload { bytesSentTotal, contentLength ->
                contentLength?.let {
                    val progress = (bytesSentTotal.toFloat() / it.toFloat()) * 100
                    Napier.d("Upload progress: $progress%")
                }
            }
        }.body()
    }

    /**
     * Create Stripe checkout session for purchasing credits
     */
    suspend fun createCheckoutSession(
        priceId: String,
        successUrl: String,
        cancelUrl: String
    ): Result<CheckoutSessionResponse> = safeApiCall {
        val request = CheckoutSessionRequest(
            priceId = priceId,
            successUrl = successUrl,
            cancelUrl = cancelUrl
        )
        httpClient.post("/stripe/create-checkout-session") {
            setBody(request)
        }.body()
    }

    /**
     * Safe API call wrapper that catches exceptions and returns Result
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: ClientRequestException) {
            // 4xx errors (client errors)
            val errorMessage = try {
                val errorBody = e.response.bodyAsText()
                Napier.e("API client error: ${e.response.status} - $errorBody", e)
                when (e.response.status) {
                    HttpStatusCode.Unauthorized -> "Unauthorized. Please log in again."
                    HttpStatusCode.PaymentRequired -> "Insufficient credits. Please purchase more credits."
                    HttpStatusCode.TooManyRequests -> "Rate limit exceeded. Please try again later."
                    else -> "Request failed: ${e.response.status.description}"
                }
            } catch (ex: Exception) {
                "Request failed: ${e.response.status.description}"
            }
            Result.failure(ApiException(errorMessage, e.response.status.value))
        } catch (e: ServerResponseException) {
            // 5xx errors (server errors)
            Napier.e("API server error: ${e.response.status}", e)
            Result.failure(ApiException("Server error. Please try again later.", e.response.status.value))
        } catch (e: Exception) {
            // Network errors, timeouts, etc.
            Napier.e("API call failed", e)
            Result.failure(ApiException("Network error: ${e.message ?: "Unknown error"}", 0))
        }
    }
}

/**
 * Custom exception for API errors
 */
class ApiException(message: String, val statusCode: Int) : Exception(message)
