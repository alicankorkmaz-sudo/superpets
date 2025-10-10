package fun.superpets.mobile.di

import androidx.room.Room
import androidx.room.RoomDatabase
import fun.superpets.mobile.data.AppDatabase
import fun.superpets.mobile.data.DB_FILE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val context = androidContext()
        val dbFile = context.getDatabasePath(DB_FILE_NAME)
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath,
        )
    }
} 