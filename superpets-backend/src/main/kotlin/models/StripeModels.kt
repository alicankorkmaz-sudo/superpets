package com.alicankorkmaz.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateCheckoutSessionRequest(
    val credits: Int
)

@Serializable
data class CreateCheckoutSessionResponse(
    val sessionId: String,
    val url: String
)
