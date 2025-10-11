package com.superpets.mobile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request model for editing images from URLs
 */
@Serializable
data class EditImageRequest(
    @SerialName("input_images") val inputImages: List<String>,
    @SerialName("hero_id") val heroId: String,
    @SerialName("num_images") val numImages: Int = 1
)

/**
 * Response model for image editing results
 */
@Serializable
data class EditImageResponse(
    @SerialName("outputs") val outputs: List<String>,
    @SerialName("prompt") val prompt: String? = null
)

/**
 * Generic API error response
 */
@Serializable
data class ApiError(
    @SerialName("error") val error: String,
    @SerialName("message") val message: String? = null
)

/**
 * Response model for Stripe checkout session
 */
@Serializable
data class CheckoutSessionRequest(
    @SerialName("priceId") val priceId: String,
    @SerialName("successUrl") val successUrl: String,
    @SerialName("cancelUrl") val cancelUrl: String
)

@Serializable
data class CheckoutSessionResponse(
    @SerialName("sessionId") val sessionId: String,
    @SerialName("url") val url: String
)
