package com.alicankorkmaz.services

import com.alicankorkmaz.database.CreditTransactionsTable
import com.alicankorkmaz.database.EditHistoryTable
import com.alicankorkmaz.database.UsersTable
import com.alicankorkmaz.models.CreditTransaction
import com.alicankorkmaz.models.EditHistory
import com.alicankorkmaz.models.TransactionType
import com.alicankorkmaz.models.User
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class SupabaseService(private val application: Application) {

    suspend fun getUser(userId: String): User? {
        return try {
            transaction {
                UsersTable.select { UsersTable.uid eq userId }
                    .mapNotNull { rowToUser(it) }
                    .singleOrNull()
            }
        } catch (e: Exception) {
            application.log.error("Failed to get user: $userId", e)
            null
        }
    }

    suspend fun createUser(userId: String, email: String, initialCredits: Long = 0): User {
        val user = User(
            uid = userId,
            email = email,
            credits = initialCredits,
            createdAt = System.currentTimeMillis()
        )

        try {
            transaction {
                UsersTable.insert {
                    it[uid] = user.uid
                    it[UsersTable.email] = user.email
                    it[credits] = user.credits
                    it[createdAt] = user.createdAt
                }
            }
            application.log.info("Created user: $userId with $initialCredits credits")
        } catch (e: Exception) {
            application.log.error("Failed to create user: $userId", e)
            throw e
        }

        return user
    }

    suspend fun getUserOrCreate(userId: String, email: String, initialCredits: Long = 5): User {
        return getUser(userId) ?: createUser(userId, email, initialCredits)
    }

    suspend fun getUserCredits(userId: String): Long? {
        return getUser(userId)?.credits
    }

    suspend fun addCredits(userId: String, amount: Long, type: TransactionType, description: String): Boolean {
        return try {
            transaction {
                // Get current credits
                val currentCredits = UsersTable.select { UsersTable.uid eq userId }
                    .singleOrNull()
                    ?.get(UsersTable.credits)
                    ?: throw IllegalStateException("User not found: $userId")

                val newCredits = currentCredits + amount

                // Update credits
                UsersTable.update({ UsersTable.uid eq userId }) {
                    it[credits] = newCredits
                }

                // Log transaction
                CreditTransactionsTable.insert {
                    it[CreditTransactionsTable.userId] = userId
                    it[CreditTransactionsTable.amount] = amount
                    it[CreditTransactionsTable.type] = type.name
                    it[CreditTransactionsTable.description] = description
                    it[timestamp] = System.currentTimeMillis()
                }
            }

            application.log.info("Added $amount credits to user $userId. Type: $type")
            true
        } catch (e: Exception) {
            application.log.error("Failed to add credits to user: $userId", e)
            false
        }
    }

    suspend fun deductCredits(userId: String, amount: Long, description: String): Boolean {
        return try {
            transaction {
                // Get current credits
                val currentCredits = UsersTable.select { UsersTable.uid eq userId }
                    .singleOrNull()
                    ?.get(UsersTable.credits)
                    ?: throw IllegalStateException("User not found: $userId")

                if (currentCredits < amount) {
                    throw IllegalStateException("Insufficient credits. Has: $currentCredits, Needs: $amount")
                }

                val newCredits = currentCredits - amount

                // Update credits
                UsersTable.update({ UsersTable.uid eq userId }) {
                    it[credits] = newCredits
                }

                // Log transaction
                CreditTransactionsTable.insert {
                    it[CreditTransactionsTable.userId] = userId
                    it[CreditTransactionsTable.amount] = -amount
                    it[type] = TransactionType.DEDUCTION.name
                    it[CreditTransactionsTable.description] = description
                    it[timestamp] = System.currentTimeMillis()
                }
            }

            application.log.info("Deducted $amount credits from user $userId")
            true
        } catch (e: Exception) {
            application.log.error("Failed to deduct credits from user: $userId - ${e.message}", e)
            false
        }
    }

    suspend fun getTransactionHistory(userId: String, limit: Int = 50): List<CreditTransaction> {
        return try {
            transaction {
                CreditTransactionsTable
                    .select { CreditTransactionsTable.userId eq userId }
                    .orderBy(CreditTransactionsTable.timestamp to SortOrder.DESC)
                    .limit(limit)
                    .map { rowToCreditTransaction(it) }
            }
        } catch (e: Exception) {
            application.log.error("Failed to get transaction history for user: $userId", e)
            emptyList()
        }
    }

    suspend fun saveEditHistory(
        userId: String,
        prompt: String,
        inputImages: List<String>,
        outputImages: List<String>,
        creditsCost: Long
    ): String? {
        return try {
            transaction {
                val id = UUID.randomUUID()
                EditHistoryTable.insert {
                    it[EditHistoryTable.id] = id
                    it[EditHistoryTable.userId] = userId
                    it[EditHistoryTable.prompt] = prompt
                    it[EditHistoryTable.inputImages] = Json.encodeToString(inputImages)
                    it[EditHistoryTable.outputImages] = Json.encodeToString(outputImages)
                    it[EditHistoryTable.creditsCost] = creditsCost
                    it[timestamp] = System.currentTimeMillis()
                }
                application.log.info("Saved edit history for user: $userId")
                id.toString()
            }
        } catch (e: Exception) {
            application.log.error("Failed to save edit history for user: $userId", e)
            null
        }
    }

    suspend fun getEditHistory(userId: String, limit: Int = 50): List<EditHistory> {
        return try {
            transaction {
                EditHistoryTable
                    .select { EditHistoryTable.userId eq userId }
                    .orderBy(EditHistoryTable.timestamp to SortOrder.DESC)
                    .limit(limit)
                    .map { rowToEditHistory(it) }
            }
        } catch (e: Exception) {
            application.log.error("Failed to get edit history for user: $userId", e)
            emptyList()
        }
    }

    // Helper functions to map database rows to model objects
    private fun rowToUser(row: ResultRow): User {
        return User(
            uid = row[UsersTable.uid],
            email = row[UsersTable.email],
            credits = row[UsersTable.credits],
            createdAt = row[UsersTable.createdAt]
        )
    }

    private fun rowToCreditTransaction(row: ResultRow): CreditTransaction {
        return CreditTransaction(
            userId = row[CreditTransactionsTable.userId],
            amount = row[CreditTransactionsTable.amount],
            type = TransactionType.valueOf(row[CreditTransactionsTable.type]),
            description = row[CreditTransactionsTable.description],
            timestamp = row[CreditTransactionsTable.timestamp]
        )
    }

    private fun rowToEditHistory(row: ResultRow): EditHistory {
        return EditHistory(
            id = row[EditHistoryTable.id].toString(),
            userId = row[EditHistoryTable.userId],
            prompt = row[EditHistoryTable.prompt],
            inputImages = Json.decodeFromString<List<String>>(row[EditHistoryTable.inputImages]),
            outputImages = Json.decodeFromString<List<String>>(row[EditHistoryTable.outputImages]),
            creditsCost = row[EditHistoryTable.creditsCost],
            timestamp = row[EditHistoryTable.timestamp]
        )
    }
}
