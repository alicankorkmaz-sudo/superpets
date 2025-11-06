package com.superpets.mobile.core.image

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import kotlin.math.max

@OptIn(ExperimentalForeignApi::class)
actual class ImageCompressor {
    actual suspend fun compress(
        imageData: ByteArray,
        maxDimension: Int,
        quality: Int
    ): ByteArray = withContext(Dispatchers.IO) {
        // Convert ByteArray to NSData
        val nsData = imageData.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = imageData.size.toULong()
            )
        }

        // Create UIImage from NSData
        val originalImage = UIImage.imageWithData(nsData)
            ?: throw IllegalArgumentException("Invalid image data")

        // Get original dimensions from CGImage
        val cgImage = originalImage.CGImage
            ?: throw IllegalArgumentException("Could not get CGImage from UIImage")

        val originalWidth = CGImageGetWidth(cgImage).toDouble()
        val originalHeight = CGImageGetHeight(cgImage).toDouble()
        val maxDim = maxDimension.toDouble()

        // Calculate new dimensions
        val scale = if (originalWidth > originalHeight) {
            maxDim / originalWidth
        } else {
            maxDim / originalHeight
        }

        val (newWidth, newHeight) = if (originalWidth > maxDim || originalHeight > maxDim) {
            Pair(originalWidth * scale, originalHeight * scale)
        } else {
            Pair(originalWidth, originalHeight)
        }

        // Use UIGraphicsBeginImageContextWithOptions which respects orientation
        UIGraphicsBeginImageContextWithOptions(CGSizeMake(newWidth, newHeight), false, 1.0)
        // Draw the image - this automatically applies the correct orientation
        originalImage.drawInRect(CGRectMake(0.0, 0.0, newWidth, newHeight))
        val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        if (resizedImage == null) {
            throw IllegalStateException("Failed to resize image")
        }

        // Note: The resized image now has orientation = .up (normal) because
        // drawInRect baked the orientation into the pixel data

        // Compress to JPEG
        val compressionQuality = (quality / 100.0)
        val compressedData = platform.UIKit.UIImageJPEGRepresentation(resizedImage, compressionQuality)
            ?: throw IllegalStateException("Failed to compress image")

        // Convert NSData back to ByteArray
        ByteArray(compressedData.length.toInt()).apply {
            usePinned { pinned ->
                platform.posix.memcpy(
                    pinned.addressOf(0),
                    compressedData.bytes,
                    compressedData.length
                )
            }
        }
    }
}
