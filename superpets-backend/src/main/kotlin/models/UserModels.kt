package com.alicankorkmaz.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val credits: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isAdmin: Boolean = false
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
    val createdAt: Long,
    val isAdmin: Boolean = false
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

// Admin-specific models
@Serializable
data class AdminStats(
    val totalUsers: Long,
    val totalCreditsDistributed: Long,
    val totalEdits: Long,
    val totalRevenue: Long,
    val activeUsersToday: Long,
    val activeUsersWeek: Long,
    val editsToday: Long,
    val editsWeek: Long
)

@Serializable
data class AdminUserDetails(
    val uid: String,
    val email: String,
    val credits: Long,
    val createdAt: Long,
    val isAdmin: Boolean,
    val totalEdits: Long,
    val totalCreditsUsed: Long,
    val totalCreditsPurchased: Long,
    val lastActivity: Long?
)

@Serializable
data class AdminUsersResponse(
    val users: List<AdminUserDetails>,
    val total: Long
)

@Serializable
data class AdminUpdateUserRequest(
    val userId: String,
    val isAdmin: Boolean? = null,
    val credits: Long? = null
)

@Serializable
data class AdminUpdateUserResponse(
    val success: Boolean,
    val user: User
)
