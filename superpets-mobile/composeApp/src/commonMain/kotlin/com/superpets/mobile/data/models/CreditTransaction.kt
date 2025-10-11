package com.superpets.mobile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreditTransaction(
    @SerialName("id") val id: String,
    @SerialName("userId") val userId: String,
    @SerialName("amount") val amount: Int,
    @SerialName("type") val type: String, // PURCHASE, SIGNUP_BONUS, DEDUCTION, etc.
    @SerialName("description") val description: String,
    @SerialName("timestamp") val timestamp: String
)

@Serializable
data class TransactionsResponse(
    @SerialName("transactions") val transactions: List<CreditTransaction>
)

@Serializable
data class CreditsResponse(
    @SerialName("credits") val credits: Int
)
