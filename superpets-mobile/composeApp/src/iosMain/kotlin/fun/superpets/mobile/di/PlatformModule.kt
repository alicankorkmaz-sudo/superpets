package fun.superpets.mobile.di

import androidx.room.Room
import androidx.room.RoomDatabase
import fun.superpets.mobile.data.AppDatabase
import fun.superpets.mobile.data.DB_FILE_NAME
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
internal actual val platformModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val dbFilePath = getDocumentDirectoryPath() + "/$DB_FILE_NAME"
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun getDocumentDirectoryPath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
} 