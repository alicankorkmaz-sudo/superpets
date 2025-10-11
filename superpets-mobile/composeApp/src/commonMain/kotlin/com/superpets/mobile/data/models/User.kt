package com.superpets.mobile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("uid") val uid: String,
    @SerialName("email") val email: String,
    @SerialName("credits") val credits: Int,
    @SerialName("isAdmin") val isAdmin: Boolean = false,
    @SerialName("createdAt") val createdAt: String
)
