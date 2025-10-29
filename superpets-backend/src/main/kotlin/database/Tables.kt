package com.alicankorkmaz.database

import org.jetbrains.exposed.sql.Table

// Users table (replaces Firestore 'users' collection)
object UsersTable : Table("users") {
    val uid = varchar("uid", 128)
    val email = varchar("email", 255)
    val credits = long("credits").default(0)
    val createdAt = long("created_at")
    val isAdmin = bool("is_admin").default(false)

    override val primaryKey = PrimaryKey(uid)
}

// Credit transactions table (replaces Firestore 'users/{userId}/transactions' subcollection)
object CreditTransactionsTable : Table("credit_transactions") {
    val id = uuid("id").autoGenerate()
    val userId = varchar("user_id", 128).references(UsersTable.uid)
    val amount = long("amount")
    val type = varchar("type", 50)
    val description = text("description")
    val timestamp = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}

// Edit history table (replaces Firestore 'users/{userId}/edits' subcollection)
object EditHistoryTable : Table("edit_history") {
    val id = uuid("id").autoGenerate()
    val userId = varchar("user_id", 128).references(UsersTable.uid)
    val prompt = text("prompt")
    // Store arrays as JSON text in PostgreSQL
    val inputImages = text("input_images")
    val outputImages = text("output_images")
    val creditsCost = long("credits_cost").default(1)
    val timestamp = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}
