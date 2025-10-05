package com.alicankorkmaz.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init(application: Application) {
        val databaseUrl = System.getenv("SUPABASE_DB_URL")
            ?: throw IllegalStateException("SUPABASE_DB_URL environment variable is not set")

        application.log.info("Connecting to Supabase database...")

        val database = Database.connect(createHikariDataSource(databaseUrl))

        // Create tables if they don't exist (optional - tables should already exist from migration SQL)
        transaction(database) {
            SchemaUtils.create(UsersTable, CreditTransactionsTable, EditHistoryTable)
        }

        application.log.info("Successfully connected to Supabase database")
    }

    private fun createHikariDataSource(url: String): HikariDataSource {
        // Parse postgresql://user:password@host:port/database format
        val regex = Regex("postgresql://([^:]+):([^@]+)@([^:]+):(\\d+)/(.+)")
        val matchResult = regex.matchEntire(url)
            ?: throw IllegalArgumentException("Invalid database URL format: $url")

        val (username, password, host, port, database) = matchResult.destructured
        // Add SSL and connection parameters for Supabase
        val jdbcUrl = "jdbc:postgresql://$host:$port/$database?sslmode=require"

        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            minimumIdle = 2
            idleTimeout = 600000
            connectionTimeout = 30000
            maxLifetime = 1800000
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        return HikariDataSource(config)
    }
}
