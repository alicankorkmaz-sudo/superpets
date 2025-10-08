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

    suspend fun createUser(userId: String, email: String, initialCredits: Long = 0, isAdmin: Boolean = false): User {
        val user = User(
            uid = userId,
            email = email,
            credits = initialCredits,
            createdAt = System.currentTimeMillis(),
            isAdmin = isAdmin
        )

        try {
            transaction {
                UsersTable.insert {
                    it[uid] = user.uid
                    it[UsersTable.email] = user.email
                    it[credits] = user.credits
                    it[createdAt] = user.createdAt
                    it[UsersTable.isAdmin] = user.isAdmin
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
            createdAt = row[UsersTable.createdAt],
            isAdmin = row[UsersTable.isAdmin]
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

    // Admin-specific functions
    suspend fun updateUserAdmin(userId: String, isAdmin: Boolean): Boolean {
        return try {
            transaction {
                UsersTable.update({ UsersTable.uid eq userId }) {
                    it[UsersTable.isAdmin] = isAdmin
                }
            }
            application.log.info("Updated admin status for user $userId to $isAdmin")
            true
        } catch (e: Exception) {
            application.log.error("Failed to update admin status for user: $userId", e)
            false
        }
    }

    suspend fun updateUserCredits(userId: String, newCredits: Long): Boolean {
        return try {
            transaction {
                UsersTable.update({ UsersTable.uid eq userId }) {
                    it[credits] = newCredits
                }
            }
            application.log.info("Updated credits for user $userId to $newCredits")
            true
        } catch (e: Exception) {
            application.log.error("Failed to update credits for user: $userId", e)
            false
        }
    }

    suspend fun getAllUsersWithDetails(limit: Int = 100, offset: Int = 0): List<com.alicankorkmaz.models.AdminUserDetails> {
        return try {
            transaction {
                val users = UsersTable.selectAll()
                    .orderBy(UsersTable.createdAt to SortOrder.DESC)
                    .limit(limit, offset.toLong())
                    .map { userRow ->
                        val userId = userRow[UsersTable.uid]

                        // Get total edits
                        val totalEdits = EditHistoryTable.select { EditHistoryTable.userId eq userId }.count()

                        // Get total credits used (sum of negative transactions)
                        val totalCreditsUsed = CreditTransactionsTable
                            .slice(CreditTransactionsTable.amount.sum())
                            .select {
                                (CreditTransactionsTable.userId eq userId) and
                                (CreditTransactionsTable.amount less 0)
                            }
                            .map { it[CreditTransactionsTable.amount.sum()] ?: 0L }
                            .firstOrNull() ?: 0L

                        // Get total credits purchased (sum of positive transactions)
                        val totalCreditsPurchased = CreditTransactionsTable
                            .slice(CreditTransactionsTable.amount.sum())
                            .select {
                                (CreditTransactionsTable.userId eq userId) and
                                (CreditTransactionsTable.amount greater 0)
                            }
                            .map { it[CreditTransactionsTable.amount.sum()] ?: 0L }
                            .firstOrNull() ?: 0L

                        // Get last activity (most recent edit or transaction)
                        val lastEdit = EditHistoryTable
                            .slice(EditHistoryTable.timestamp)
                            .select { EditHistoryTable.userId eq userId }
                            .orderBy(EditHistoryTable.timestamp to SortOrder.DESC)
                            .limit(1)
                            .map { it[EditHistoryTable.timestamp] }
                            .firstOrNull()

                        val lastTransaction = CreditTransactionsTable
                            .slice(CreditTransactionsTable.timestamp)
                            .select { CreditTransactionsTable.userId eq userId }
                            .orderBy(CreditTransactionsTable.timestamp to SortOrder.DESC)
                            .limit(1)
                            .map { it[CreditTransactionsTable.timestamp] }
                            .firstOrNull()

                        val lastActivity = listOfNotNull(lastEdit, lastTransaction).maxOrNull()

                        com.alicankorkmaz.models.AdminUserDetails(
                            uid = userId,
                            email = userRow[UsersTable.email],
                            credits = userRow[UsersTable.credits],
                            createdAt = userRow[UsersTable.createdAt],
                            isAdmin = userRow[UsersTable.isAdmin],
                            totalEdits = totalEdits,
                            totalCreditsUsed = -totalCreditsUsed,
                            totalCreditsPurchased = totalCreditsPurchased,
                            lastActivity = lastActivity
                        )
                    }
                users
            }
        } catch (e: Exception) {
            application.log.error("Failed to get all users with details", e)
            emptyList()
        }
    }

    suspend fun getAdminStats(): com.alicankorkmaz.models.AdminStats {
        return try {
            transaction {
                // Total users
                val totalUsers = UsersTable.selectAll().count()

                // Total credits distributed
                val totalCreditsDistributed = CreditTransactionsTable
                    .slice(CreditTransactionsTable.amount.sum())
                    .select { CreditTransactionsTable.amount greater 0 }
                    .map { it[CreditTransactionsTable.amount.sum()] ?: 0L }
                    .firstOrNull() ?: 0L

                // Total edits
                val totalEdits = EditHistoryTable.selectAll().count()

                // Total revenue (assuming each credit purchased = revenue, you can adjust the logic)
                val totalRevenue = CreditTransactionsTable
                    .slice(CreditTransactionsTable.amount.sum())
                    .select { CreditTransactionsTable.type eq TransactionType.PURCHASE.name }
                    .map { it[CreditTransactionsTable.amount.sum()] ?: 0L }
                    .firstOrNull() ?: 0L

                // Active users today (users with edits or transactions in last 24 hours)
                val oneDayAgo = System.currentTimeMillis() - 24 * 60 * 60 * 1000
                val activeUsersToday = (EditHistoryTable
                    .slice(EditHistoryTable.userId)
                    .select { EditHistoryTable.timestamp greater oneDayAgo }
                    .map { it[EditHistoryTable.userId] }
                    .toSet() + CreditTransactionsTable
                    .slice(CreditTransactionsTable.userId)
                    .select { CreditTransactionsTable.timestamp greater oneDayAgo }
                    .map { it[CreditTransactionsTable.userId] }
                    .toSet()).size.toLong()

                // Active users this week
                val oneWeekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
                val activeUsersWeek = (EditHistoryTable
                    .slice(EditHistoryTable.userId)
                    .select { EditHistoryTable.timestamp greater oneWeekAgo }
                    .map { it[EditHistoryTable.userId] }
                    .toSet() + CreditTransactionsTable
                    .slice(CreditTransactionsTable.userId)
                    .select { CreditTransactionsTable.timestamp greater oneWeekAgo }
                    .map { it[CreditTransactionsTable.userId] }
                    .toSet()).size.toLong()

                // Edits today
                val editsToday = EditHistoryTable
                    .select { EditHistoryTable.timestamp greater oneDayAgo }
                    .count()

                // Edits this week
                val editsWeek = EditHistoryTable
                    .select { EditHistoryTable.timestamp greater oneWeekAgo }
                    .count()

                com.alicankorkmaz.models.AdminStats(
                    totalUsers = totalUsers,
                    totalCreditsDistributed = totalCreditsDistributed,
                    totalEdits = totalEdits,
                    totalRevenue = totalRevenue,
                    activeUsersToday = activeUsersToday,
                    activeUsersWeek = activeUsersWeek,
                    editsToday = editsToday,
                    editsWeek = editsWeek
                )
            }
        } catch (e: Exception) {
            application.log.error("Failed to get admin stats", e)
            com.alicankorkmaz.models.AdminStats(0, 0, 0, 0, 0, 0, 0, 0)
        }
    }

    suspend fun getTotalUsers(): Long {
        return try {
            transaction {
                UsersTable.selectAll().count()
            }
        } catch (e: Exception) {
            application.log.error("Failed to get total users", e)
            0L
        }
    }

    suspend fun getAllTransactions(limit: Int = 100, offset: Int = 0): List<CreditTransaction> {
        return try {
            transaction {
                CreditTransactionsTable
                    .selectAll()
                    .orderBy(CreditTransactionsTable.timestamp to SortOrder.DESC)
                    .limit(limit, offset.toLong())
                    .map { rowToCreditTransaction(it) }
            }
        } catch (e: Exception) {
            application.log.error("Failed to get all transactions", e)
            emptyList()
        }
    }

    suspend fun getAllEdits(limit: Int = 100, offset: Int = 0): List<EditHistory> {
        return try {
            transaction {
                EditHistoryTable
                    .selectAll()
                    .orderBy(EditHistoryTable.timestamp to SortOrder.DESC)
                    .limit(limit, offset.toLong())
                    .map { rowToEditHistory(it) }
            }
        } catch (e: Exception) {
            application.log.error("Failed to get all edits", e)
            emptyList()
        }
    }
}
