package com.superpets.mobile.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

/**
 * Superpets Room Database
 *
 * Currently uses placeholder entity - reserved for future local caching (Phase 6):
 * - Cache heroes locally
 * - Cache edit history
 * - Offline viewing
 *
 * Note: PlaceholderEntity exists only to satisfy Room's requirement
 * of at least one entity. It will be removed in Phase 6.
 */
@Database(entities = [PlaceholderEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    // DAOs will be added here when implementing local caching
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
