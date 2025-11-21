package com.alicankorkmaz.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Client request model (from frontend)
@Serializable
data class NanoBananaEditRequest(
    @SerialName("hero_id")
    val heroId: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("num_images")
    val numImages: Int = 1,
    @SerialName("output_format")
    val outputFormat: String = "jpeg",
    @SerialName("sync_mode")
    val syncMode: Boolean = false
)

// fal.ai API request model (to external API)
@Serializable
data class FalAiEditRequest(
    val prompt: String,
    @SerialName("image_urls")
    val imageUrls: List<String>,
    @SerialName("num_images")
    val numImages: Int = 1,
    @SerialName("output_format")
    val outputFormat: String = "jpeg",
    @SerialName("sync_mode")
    val syncMode: Boolean = false
)

@Serializable
data class ImageFile(
    val url: String
)

@Serializable
data class NanoBananaEditResponse(
    val images: List<ImageFile>,
    val description: String
)

// fal.ai Storage Upload Models
@Serializable
data class InitiateUploadRequest(
    @SerialName("content_type")
    val contentType: String,
    @SerialName("file_name")
    val fileName: String
)

@Serializable
data class InitiateUploadResponse(
    @SerialName("upload_url")
    val uploadUrl: String,
    @SerialName("file_url")
    val fileUrl: String
)

@Serializable
data class FalAiQueueResponse(
    @SerialName("request_id")
    val requestId: String
)

@Serializable
data class FalAiStatusResponse(
    val status: String,
    val images: List<ImageFile>? = null,
    val description: String? = null
)