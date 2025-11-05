package com.superpets.mobile.core.image

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

actual class ImagePicker {
    @Composable
    actual fun rememberCameraLauncher(
        onImageCaptured: (ByteArray) -> Unit
    ): () -> Unit {
        val context = LocalContext.current

        // Note: For camera, we need to use TakePicture contract
        // This requires saving to a file first, then reading it back
        // For now, we'll show a message that camera isn't implemented yet
        return {
            Napier.w("Camera picker not yet implemented on Android")
            // TODO: Implement camera capture with TakePicture contract
            // This requires creating a temporary file URI and reading it back
        }
    }

    @Composable
    actual fun rememberGalleryLauncher(
        maxImages: Int,
        onImagesSelected: (List<ByteArray>) -> Unit
    ): () -> Unit {
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxImages),
            onResult = { uris ->
                if (uris.isNotEmpty()) {
                    // Convert URIs to ByteArrays in background
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val byteArrays = uris.mapNotNull { uri ->
                                uriToByteArray(context, uri)
                            }
                            withContext(Dispatchers.Main) {
                                onImagesSelected(byteArrays)
                            }
                        } catch (e: Exception) {
                            Napier.e("Failed to convert URIs to ByteArrays: ${e.message}", e)
                        }
                    }
                }
            }
        )

        return {
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private suspend fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val buffer = ByteArrayOutputStream()
                    val data = ByteArray(16384) // 16KB buffer
                    var nRead: Int
                    while (inputStream.read(data, 0, data.size).also { nRead = it } != -1) {
                        buffer.write(data, 0, nRead)
                    }
                    buffer.flush()
                    buffer.toByteArray()
                }
            } catch (e: Exception) {
                Napier.e("Failed to read URI: ${e.message}", e)
                null
            }
        }
    }
}
