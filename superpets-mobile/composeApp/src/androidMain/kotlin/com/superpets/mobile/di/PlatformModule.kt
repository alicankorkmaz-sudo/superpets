package com.superpets.mobile.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.superpets.mobile.core.image.ImagePicker
import com.superpets.mobile.data.AppDatabase
import com.superpets.mobile.data.DB_FILE_NAME
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
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

    // Platform-specific Ktor engine for Android (OkHttp)
    single<HttpClientEngine> {
        OkHttp.create()
    }

    // ImagePicker for Android
    single<ImagePicker> { ImagePicker() }
} 