package com.superpets.mobile.core.image

import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
actual class ImageSharer {
    actual suspend fun shareImage(imageUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Napier.d("Downloading image for sharing from: $imageUrl")

            // Download image from URL
            val url = NSURL.URLWithString(imageUrl)
                ?: return@withContext Result.failure(Exception("Invalid URL"))

            val imageData = NSData.create(contentsOfURL = url)
                ?: return@withContext Result.failure(Exception("Failed to download image"))

            val image = UIImage.imageWithData(imageData)
                ?: return@withContext Result.failure(Exception("Failed to decode image"))

            Napier.d("Image downloaded, presenting share sheet")

            // Present share sheet on main thread using dispatch
            dispatch_async(dispatch_get_main_queue()) {
                @Suppress("UNCHECKED_CAST")
                val itemsArray = listOf(image, "Check out my Superpet! Created with Superpets app.") as List<Any?>

                val activityViewController = UIActivityViewController(
                    activityItems = itemsArray,
                    applicationActivities = null
                )

                // Get root view controller
                val keyWindow = UIApplication.sharedApplication.keyWindow
                val rootViewController = keyWindow?.rootViewController

                if (rootViewController != null) {
                    // Present the share sheet
                    // Note: On iPad, the sheet will be centered. Custom positioning via
                    // popoverPresentationController is not available in Kotlin/Native bindings.
                    rootViewController.presentViewController(
                        viewControllerToPresent = activityViewController,
                        animated = true,
                        completion = null
                    )

                    Napier.d("Share sheet presented successfully")
                } else {
                    Napier.e("Could not get root view controller")
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Napier.e("Failed to share image: ${e.message}", e)
            Result.failure(Exception("Failed to share image: ${e.message}"))
        }
    }
}
