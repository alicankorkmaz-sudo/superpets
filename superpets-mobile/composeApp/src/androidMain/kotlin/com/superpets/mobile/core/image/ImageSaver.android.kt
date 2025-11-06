package com.superpets.mobile.core.image

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.superpets.mobile.core.ContextProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

actual class ImageSaver {
    private val context by lazy { ContextProvider.get() }
    actual suspend fun saveImage(imageUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Napier.d("Downloading image from: $imageUrl")

            // Download image from URL
            val imageBytes = URL(imageUrl).readBytes()
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: return@withContext Result.failure(Exception("Failed to decode image"))

            Napier.d("Image downloaded, size: ${imageBytes.size} bytes")

            // Generate filename with timestamp
            val timestamp = System.currentTimeMillis()
            val fileName = "superpet_$timestamp.jpg"

            // Save to gallery using MediaStore (Android 10+) or legacy method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImageMediaStore(bitmap, fileName)
            } else {
                saveImageLegacy(bitmap, fileName)
            }

            bitmap.recycle()
            Napier.d("Image saved successfully: $fileName")
            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Failed to save image: ${e.message}", e)
            Result.failure(Exception("Failed to save image: ${e.message}"))
        }
    }

    private fun saveImageMediaStore(bitmap: Bitmap, fileName: String) {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Superpets")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val uri = context.contentResolver.insert(collection, values)
            ?: throw Exception("Failed to create MediaStore entry")

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
        }

        // Mark as not pending
        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        context.contentResolver.update(uri, values, null, null)
    }

    @Suppress("DEPRECATION")
    private fun saveImageLegacy(bitmap: Bitmap, fileName: String) {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val superPetsDir = File(picturesDir, "Superpets")

        if (!superPetsDir.exists()) {
            superPetsDir.mkdirs()
        }

        val file = File(superPetsDir, fileName)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
        }

        // Add to media scanner
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DATA, file.absolutePath)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }
}
