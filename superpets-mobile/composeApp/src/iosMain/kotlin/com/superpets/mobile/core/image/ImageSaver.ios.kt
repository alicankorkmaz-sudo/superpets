package com.superpets.mobile.core.image

import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Photos.PHAssetChangeRequest
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual class ImageSaver {
    actual suspend fun saveImage(imageUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Napier.d("Downloading image from: $imageUrl")

            // Download image from URL
            val url = NSURL.URLWithString(imageUrl)
                ?: return@withContext Result.failure(Exception("Invalid URL"))

            val imageData = NSData.create(contentsOfURL = url)
                ?: return@withContext Result.failure(Exception("Failed to download image"))

            val image = UIImage.imageWithData(imageData)
                ?: return@withContext Result.failure(Exception("Failed to decode image"))

            Napier.d("Image downloaded successfully")

            // Convert to JPEG data
            val jpegData = UIImageJPEGRepresentation(image, 0.95)
                ?: return@withContext Result.failure(Exception("Failed to convert image to JPEG"))

            // Check photo library authorization
            val authStatus = PHPhotoLibrary.authorizationStatus()
            if (authStatus != platform.Photos.PHAuthorizationStatusAuthorized) {
                // Request authorization
                val granted = suspendCancellableCoroutine { continuation ->
                    PHPhotoLibrary.requestAuthorization { status ->
                        continuation.resume(status == platform.Photos.PHAuthorizationStatusAuthorized)
                    }
                }

                if (!granted) {
                    return@withContext Result.failure(Exception("Photo library access denied"))
                }
            }

            // Save to photo library
            suspendCancellableCoroutine { continuation ->
                PHPhotoLibrary.sharedPhotoLibrary().performChanges(
                    changeBlock = {
                        PHAssetChangeRequest.creationRequestForAssetFromImage(image)
                    },
                    completionHandler = { success, error ->
                        if (success) {
                            Napier.d("Image saved to photo library successfully")
                            continuation.resume(Result.success(Unit))
                        } else {
                            val errorMsg = error?.localizedDescription ?: "Unknown error"
                            Napier.e("Failed to save image to photo library: $errorMsg")
                            continuation.resume(Result.failure(Exception("Failed to save image: $errorMsg")))
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Napier.e("Failed to save image: ${e.message}", e)
            Result.failure(Exception("Failed to save image: ${e.message}"))
        }
    }
}
