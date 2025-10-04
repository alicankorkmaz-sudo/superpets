package com.alicankorkmaz.services

import com.alicankorkmaz.models.CreditTransaction
import com.alicankorkmaz.models.TransactionType
import com.alicankorkmaz.models.User
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import io.ktor.server.application.*

class FirestoreService(private val application: Application) {

    private val firestore: Firestore
        get() = FirestoreClient.getFirestore()

    suspend fun getUser(userId: String): User? {
        return try {
            val docRef = firestore.collection("users").document(userId)
            val docSnapshot = docRef.get().get()

            if (docSnapshot.exists()) {
                docSnapshot.toObject(User::class.java)
            } else {
                null
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
            firestore.collection("users").document(userId).set(user).get()
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
            val userRef = firestore.collection("users").document(userId)

            // Use Firestore transaction to ensure atomicity
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef).get()

                if (!snapshot.exists()) {
                    throw IllegalStateException("User not found: $userId")
                }

                val currentCredits = snapshot.getLong("credits") ?: 0L
                val newCredits = currentCredits + amount

                transaction.update(userRef, "credits", newCredits)

                // Log transaction
                val transactionDoc = CreditTransaction(
                    userId = userId,
                    amount = amount,
                    type = type,
                    description = description
                )

                val transactionRef = firestore
                    .collection("users")
                    .document(userId)
                    .collection("transactions")
                    .document()

                transaction.set(transactionRef, transactionDoc)
            }.get()

            application.log.info("Added $amount credits to user $userId. Type: $type")
            true
        } catch (e: Exception) {
            application.log.error("Failed to add credits to user: $userId", e)
            false
        }
    }

    suspend fun deductCredits(userId: String, amount: Long, description: String): Boolean {
        return try {
            val userRef = firestore.collection("users").document(userId)

            // Use Firestore transaction to ensure atomicity
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef).get()

                if (!snapshot.exists()) {
                    throw IllegalStateException("User not found: $userId")
                }

                val currentCredits = snapshot.getLong("credits") ?: 0L

                if (currentCredits < amount) {
                    throw IllegalStateException("Insufficient credits. Has: $currentCredits, Needs: $amount")
                }

                val newCredits = currentCredits - amount
                transaction.update(userRef, "credits", newCredits)

                // Log transaction
                val transactionDoc = CreditTransaction(
                    userId = userId,
                    amount = -amount,
                    type = TransactionType.DEDUCTION,
                    description = description
                )

                val transactionRef = firestore
                    .collection("users")
                    .document(userId)
                    .collection("transactions")
                    .document()

                transaction.set(transactionRef, transactionDoc)
            }.get()

            application.log.info("Deducted $amount credits from user $userId")
            true
        } catch (e: Exception) {
            application.log.error("Failed to deduct credits from user: $userId - ${e.message}", e)
            false
        }
    }

    suspend fun getTransactionHistory(userId: String, limit: Int = 50): List<CreditTransaction> {
        return try {
            val querySnapshot = firestore
                .collection("users")
                .document(userId)
                .collection("transactions")
                .orderBy("timestamp", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .get()

            querySnapshot.documents.mapNotNull { it.toObject(CreditTransaction::class.java) }
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
            val editRef = firestore
                .collection("users")
                .document(userId)
                .collection("edits")
                .document()

            val editHistory = com.alicankorkmaz.models.EditHistory(
                id = editRef.id,
                userId = userId,
                prompt = prompt,
                inputImages = inputImages,
                outputImages = outputImages,
                creditsCost = creditsCost,
                timestamp = System.currentTimeMillis()
            )

            editRef.set(editHistory).get()
            application.log.info("Saved edit history for user: $userId")
            editRef.id
        } catch (e: Exception) {
            application.log.error("Failed to save edit history for user: $userId", e)
            null
        }
    }

    suspend fun getEditHistory(userId: String, limit: Int = 50): List<com.alicankorkmaz.models.EditHistory> {
        return try {
            val querySnapshot = firestore
                .collection("users")
                .document(userId)
                .collection("edits")
                .orderBy("timestamp", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .get()

            querySnapshot.documents.mapNotNull { it.toObject(com.alicankorkmaz.models.EditHistory::class.java) }
        } catch (e: Exception) {
            application.log.error("Failed to get edit history for user: $userId", e)
            emptyList()
        }
    }
}
