package com.superpets.mobile.screens.home

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

/**
 * Format recent activity timestamp as "X days ago"
 */
fun formatRecentActivity(timestamp: String?): String {
    if (timestamp == null) return "Never"

    return try {
        val instant = Instant.parse(timestamp)
        val now = Clock.System.now()
        val duration = now - instant

        when {
            duration.inWholeDays == 0L -> "Today"
            duration.inWholeDays == 1L -> "1 day ago"
            duration.inWholeDays < 7 -> "${duration.inWholeDays} days ago"
            duration.inWholeDays < 30 -> "${duration.inWholeDays / 7} week${if (duration.inWholeDays / 7 > 1) "s" else ""} ago"
            else -> "${duration.inWholeDays / 30} month${if (duration.inWholeDays / 30 > 1) "s" else ""} ago"
        }
    } catch (e: Exception) {
        "Recently"
    }
}
