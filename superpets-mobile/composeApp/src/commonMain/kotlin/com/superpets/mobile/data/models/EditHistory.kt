package com.superpets.mobile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditHistory(
    @SerialName("id") val id: String,
    @SerialName("userId") val userId: String,
    @SerialName("prompt") val prompt: String,
    @SerialName("inputImages") val inputImages: List<String>,
    @SerialName("outputImages") val outputImages: List<String>,
    @SerialName("creditsCost") val creditsCost: Int,
    @SerialName("timestamp") val timestamp: String
)

@Serializable
data class EditHistoryResponse(
    @SerialName("edits") val edits: List<EditHistory>
)
