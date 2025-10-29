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

        try {
            val dataSource = createHikariDataSource(databaseUrl, application)
            application.log.info("HikariCP DataSource created successfully")

            val database = Database.connect(dataSource)
            application.log.info("Exposed Database instance created")

            // Create tables if they don't exist (optional - tables should already exist from migration SQL)
            application.log.info("Running schema creation/validation...")
            transaction(database) {
                SchemaUtils.create(UsersTable, CreditTransactionsTable, EditHistoryTable)
            }
            application.log.info("Schema validation completed")

            application.log.info("Successfully connected to Supabase database")
        } catch (e: Exception) {
            application.log.error("Failed to initialize database connection", e)
            throw e
        }
    }

    private fun createHikariDataSource(url: String, application: Application): HikariDataSource {
        // Parse postgresql://user:password@host:port/database format
        val regex = Regex("postgresql://([^:]+):([^@]+)@([^:]+):(\\d+)/(.+)")
        val matchResult = regex.matchEntire(url)
            ?: throw IllegalArgumentException("Invalid database URL format: $url")

        val (username, password, host, port, database) = matchResult.destructured

        // Add SSL and connection parameters for Supabase Transaction Pooler
        // prepareThreshold=0 disables prepared statements (required for PgBouncer/transaction pooler)
        // connectTimeout=10 - 10 seconds to establish connection (reduced from 60s)
        // socketTimeout=20 - 20 seconds for socket operations
        // See: https://github.com/pgbouncer/pgbouncer/issues/374
        val jdbcUrl = "jdbc:postgresql://$host:$port/$database?sslmode=require&prepareThreshold=0&connectTimeout=10&socketTimeout=20"

        application.log.info("Database connection details: host=$host, port=$port, database=$database, username=$username")
        application.log.info("JDBC URL: $jdbcUrl")

        val config = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.username = username
            this.password = password
            driverClassName = "org.postgresql.Driver"

            // Connection pool sizing - REDUCED to avoid hitting Supabase connection limits
            // Supabase free tier typically allows 60-100 direct connections
            // Using transaction pooler (port 6543) which has separate limits
            maximumPoolSize = 3   // Reduced to 3 to avoid max connections error during deployments
            minimumIdle = 1       // Keep minimal idle connections

            // Timeout configuration (reduced for faster failure detection)
            connectionTimeout = 15000  // 15 seconds max to get connection from pool
            idleTimeout = 300000       // 5 minutes - close idle connections sooner
            maxLifetime = 1800000      // 30 minutes - max connection lifetime

            // Keep-alive and validation
            keepaliveTime = 60000      // 1 minute - send keep-alive to prevent stale connections
            connectionTestQuery = "SELECT 1"  // Validate connections

            // Leak detection for debugging (30 seconds)
            leakDetectionThreshold = 30000

            // IMPORTANT: Transaction poolers (PgBouncer) require autocommit and don't support isolation level changes
            isAutoCommit = true
            // Do NOT set transactionIsolation - not supported by transaction poolers
        }

        application.log.info("Creating HikariCP connection pool...")
        return HikariDataSource(config)
    }
}
