package com.superpets.mobile.core.image

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.FileProvider
import com.superpets.mobile.core.ContextProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

actual class ImageSharer {
    private val context by lazy { ContextProvider.get() }
    actual suspend fun shareImage(imageUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Napier.d("Downloading image for sharing from: $imageUrl")

            // Download image from URL
            val imageBytes = URL(imageUrl).readBytes()
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: return@withContext Result.failure(Exception("Failed to decode image"))

            Napier.d("Image downloaded, preparing to share")

            // Save to cache directory
            val cacheDir = File(context.cacheDir, "shared_images")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val timestamp = System.currentTimeMillis()
            val file = File(cacheDir, "superpet_$timestamp.jpg")

            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
            }

            bitmap.recycle()

            // Get URI using FileProvider
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            // Create share intent
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Check out my Superpet! Created with Superpets app.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            // Launch share sheet
            val chooserIntent = Intent.createChooser(shareIntent, "Share Superpet")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(chooserIntent)

            Napier.d("Share sheet opened successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Failed to share image: ${e.message}", e)
            Result.failure(Exception("Failed to share image: ${e.message}"))
        }
    }
}
