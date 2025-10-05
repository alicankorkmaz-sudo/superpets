package com.alicankorkmaz.database

import org.jetbrains.exposed.sql.Table

/**
 * Users table - stores user profiles and credit balances
 */
object Users : Table("users") {
    val uid = varchar("uid", 128)
    val email = varchar("email", 255)
    val credits = long("credits").default(0)
    val createdAt = long("created_at")

    override val primaryKey = PrimaryKey(uid)
}

/**
 * Credit transactions table - stores all credit additions and deductions
 */
object CreditTransactions : Table("credit_transactions") {
    val id = uuid("id").autoGenerate()
    val userId = varchar("user_id", 128).references(Users.uid)
    val amount = long("amount")
    val type = varchar("type", 50) // PURCHASE, DEDUCTION, REFUND, BONUS
    val description = text("description")
    val timestamp = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}

/**
 * Edit history table - stores all image edit operations
 */
object EditHistory : Table("edit_history") {
    val id = uuid("id").autoGenerate()
    val userId = varchar("user_id", 128).references(Users.uid)
    val prompt = text("prompt")
    val inputImages = text("input_images") // JSON array stored as text
    val outputImages = text("output_images") // JSON array stored as text
    val creditsCost = long("credits_cost")
    val timestamp = long("timestamp")

    override val primaryKey = PrimaryKey(id)
}
