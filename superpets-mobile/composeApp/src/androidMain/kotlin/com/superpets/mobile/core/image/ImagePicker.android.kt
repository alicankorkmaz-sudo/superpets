package com.superpets.mobile.core.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

actual class ImagePicker {
    @Composable
    actual fun rememberCameraLauncher(
        onImageCaptured: (ByteArray) -> Unit,
        onPermissionDenied: (() -> Unit)?
    ): () -> Unit {
        val context = LocalContext.current

        // Track if we should launch camera after permission grant
        var shouldLaunchCamera by remember { mutableStateOf(false) }

        // Create a temporary file to store the captured image
        val photoFile = remember {
            File.createTempFile(
                "camera_",
                ".jpg",
                context.cacheDir
            ).apply {
                deleteOnExit()
            }
        }

        val photoUri = remember {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
        }

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                shouldLaunchCamera = false
                if (success) {
                    // Read the captured image from the file and convert to ByteArray
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                            if (bitmap != null) {
                                // Compress to JPEG with 80% quality to match iOS
                                val outputStream = ByteArrayOutputStream()
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                                val byteArray = outputStream.toByteArray()

                                withContext(Dispatchers.Main) {
                                    onImageCaptured(byteArray)
                                }

                                // Clean up bitmap
                                bitmap.recycle()
                            } else {
                                Napier.e("Failed to decode captured image")
                            }

                            // Clean up the temporary file
                            photoFile.delete()
                        } catch (e: Exception) {
                            Napier.e("Failed to process captured image: ${e.message}", e)
                            photoFile.delete()
                        }
                    }
                } else {
                    Napier.d("Camera capture cancelled")
                    photoFile.delete()
                }
            }
        )

        // Camera permission launcher
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    Napier.d("Camera permission granted")
                    if (shouldLaunchCamera) {
                        cameraLauncher.launch(photoUri)
                    }
                } else {
                    Napier.w("Camera permission denied")
                    shouldLaunchCamera = false
                    onPermissionDenied?.invoke()
                }
            }
        )

        return {
            // Check if permission is already granted
            val permissionStatus = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            )

            if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, launch camera
                cameraLauncher.launch(photoUri)
            } else {
                // Check if we should show rationale (permission was denied before but not permanently)
                val activity = context as? android.app.Activity
                val shouldShowRationale = activity?.let {
                    androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        android.Manifest.permission.CAMERA
                    )
                } ?: false

                // Request permission first, then launch camera
                shouldLaunchCamera = true
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
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
