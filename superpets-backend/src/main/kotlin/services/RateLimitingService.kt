package com.alicankorkmaz.services

import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

/**
 * Rate limiting service that tracks requests per user and per IP address.
 * Uses in-memory storage with sliding window algorithm.
 */
class RateLimitingService {

    // Store request timestamps: key -> list of timestamps
    private val requestHistory = ConcurrentHashMap<String, MutableList<Long>>()

    data class RateLimitResult(
        val allowed: Boolean,
        val remainingRequests: Int,
        val resetTime: Long
    )

    /**
     * Check if a request should be allowed based on rate limits.
     *
     * @param key Unique identifier (userId or IP address)
     * @param maxRequests Maximum number of requests allowed in the time window
     * @param windowSeconds Time window in seconds
     * @return RateLimitResult indicating if request is allowed
     */
    fun checkRateLimit(
        key: String,
        maxRequests: Int,
        windowSeconds: Long
    ): RateLimitResult {
        val now = Instant.now().epochSecond
        val windowStart = now - windowSeconds

        // Get or create request history for this key
        val timestamps = requestHistory.getOrPut(key) { mutableListOf() }

        synchronized(timestamps) {
            // Remove timestamps outside the current window
            timestamps.removeIf { it < windowStart }

            // Check if limit is exceeded
            val currentRequests = timestamps.size
            val allowed = currentRequests < maxRequests

            if (allowed) {
                // Add current request timestamp
                timestamps.add(now)
            }

            // Calculate remaining requests and reset time
            val remainingRequests = maxOf(0, maxRequests - timestamps.size)
            val resetTime = if (timestamps.isNotEmpty()) {
                timestamps.minOrNull()!! + windowSeconds
            } else {
                now + windowSeconds
            }

            return RateLimitResult(allowed, remainingRequests, resetTime)
        }
    }

    /**
     * Check per-user rate limit.
     */
    fun checkUserRateLimit(userId: String, maxRequests: Int, windowSeconds: Long): RateLimitResult {
        return checkRateLimit("user:$userId", maxRequests, windowSeconds)
    }

    /**
     * Check per-IP rate limit.
     */
    fun checkIpRateLimit(ipAddress: String, maxRequests: Int, windowSeconds: Long): RateLimitResult {
        return checkRateLimit("ip:$ipAddress", maxRequests, windowSeconds)
    }

    /**
     * Clear rate limit history for a specific key (useful for testing).
     */
    fun clearRateLimit(key: String) {
        requestHistory.remove(key)
    }

    /**
     * Clean up old entries to prevent memory leaks.
     * Should be called periodically (e.g., every hour).
     */
    fun cleanup(maxAgeSeconds: Long = 3600) {
        val now = Instant.now().epochSecond
        val cutoff = now - maxAgeSeconds

        requestHistory.forEach { (key, timestamps) ->
            synchronized(timestamps) {
                timestamps.removeIf { it < cutoff }
                // Remove empty entries
                if (timestamps.isEmpty()) {
                    requestHistory.remove(key)
                }
            }
        }
    }
}
