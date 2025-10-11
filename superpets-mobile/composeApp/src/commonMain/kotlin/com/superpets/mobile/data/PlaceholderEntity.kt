package com.superpets.mobile.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Placeholder entity to satisfy Room Database requirement.
 * Will be removed when implementing actual entities in Phase 6.
 *
 * Room Database requires at least one entity to compile.
 * This placeholder ensures the database infrastructure works
 * until we implement real caching (heroes, edit history, etc.)
 */
@Entity(tableName = "placeholder")
internal data class PlaceholderEntity(
    @PrimaryKey val id: Int = 1
)
