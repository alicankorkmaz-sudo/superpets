package com.alicankorkmaz.services

import com.alicankorkmaz.models.FalAiEditRequest
import com.alicankorkmaz.models.InitiateUploadRequest
import com.alicankorkmaz.models.InitiateUploadResponse
import com.alicankorkmaz.models.NanoBananaEditRequest
import com.alicankorkmaz.models.NanoBananaEditResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json

class NanoBananaService(private val application: Application) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        // Increase timeout for fal.ai API (requests can take 5-15 seconds)
        install(io.ktor.client.plugins.HttpTimeout) {
            requestTimeoutMillis = 60000  // 60 seconds
            connectTimeoutMillis = 10000  // 10 seconds
            socketTimeoutMillis = 60000   // 60 seconds
        }
    }

    private val apiKey: String by lazy {
        application.environment.config.propertyOrNull("fal.apiKey")?.getString()
            ?: throw IllegalStateException("FAL_API_KEY environment variable not set")
    }

    suspend fun editImage(prompt: String, request: NanoBananaEditRequest): NanoBananaEditResponse {
        // Create a proper serializable request for the fal.ai API
        // Note: fal.ai API expects image_urls as an array, so we wrap the single URL in a list
        val apiRequest = FalAiEditRequest(
            prompt = prompt,
            imageUrls = listOf(request.imageUrl),
            numImages = request.numImages,
            outputFormat = request.outputFormat,
            syncMode = request.syncMode
        )

        return client.post("https://fal.run/fal-ai/nano-banana/edit") {
            header("Authorization", "Key $apiKey")
            contentType(ContentType.Application.Json)
            setBody(apiRequest)
        }.body()
    }

    suspend fun uploadFile(fileBytes: ByteArray, fileName: String, contentType: String): String {
        // Step 1: Initiate upload to get upload URL
        val initiateResponse: InitiateUploadResponse = client.post("https://rest.alpha.fal.ai/storage/upload/initiate") {
            header("Authorization", "Key $apiKey")
            contentType(ContentType.Application.Json)
            parameter("storage_type", "fal-cdn-v3")
            setBody(InitiateUploadRequest(
                contentType = contentType,
                fileName = fileName
            ))
        }.body()

        // Step 2: Upload file to the returned upload URL
        val uploadResponse: HttpResponse = client.put(initiateResponse.uploadUrl) {
            header("Content-Type", contentType)
            setBody(fileBytes)
        }

        // Verify upload was successful
        if (!uploadResponse.status.isSuccess()) {
            throw Exception("Failed to upload file: ${uploadResponse.status}")
        }

        // Step 3: Return the file URL for use in edit requests
        return initiateResponse.fileUrl
    }

    fun close() {
        client.close()
    }
}