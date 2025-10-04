package com.alicankorkmaz.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val credits: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class CreditTransaction(
    val userId: String = "",
    val amount: Long = 0,
    val type: TransactionType = TransactionType.PURCHASE,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
    PURCHASE,    // User bought credits
    DEDUCTION,   // Credits used for image edit
    REFUND,      // Credits refunded
    BONUS        // Free credits/promotional
}

@Serializable
data class UserProfile(
    val uid: String,
    val email: String,
    val credits: Long,
    val createdAt: Long
)

@Serializable
data class AddCreditsRequest(
    val amount: Long,
    val description: String = "Credit purchase"
)

@Serializable
data class AddCreditsResponse(
    val success: Boolean,
    val credits: Long?
)

@Serializable
data class CreditBalanceResponse(
    val credits: Long
)

@Serializable
data class TransactionHistoryResponse(
    val transactions: List<CreditTransaction>
)

@Serializable
data class ErrorResponse(
    val error: String
)

@Serializable
data class EditHistory(
    val id: String = "",
    val userId: String = "",
    val prompt: String = "",
    val inputImages: List<String> = emptyList(),
    val outputImages: List<String> = emptyList(),
    val creditsCost: Long = 1,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class EditHistoryResponse(
    val edits: List<EditHistory>
)

@Serializable
data class StatusResponse(
    val status: String,
    val service: String,
    val version: String,
    val timestamp: Long
)
