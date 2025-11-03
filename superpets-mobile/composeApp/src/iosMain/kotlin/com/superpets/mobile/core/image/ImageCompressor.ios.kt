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

        val cgImage = originalImage.CGImage
            ?: throw IllegalArgumentException("Failed to get CGImage")

        // Get original dimensions
        val width = CGImageGetWidth(cgImage).toDouble()
        val height = CGImageGetHeight(cgImage).toDouble()

        // Calculate new dimensions
        val maxDim = maxDimension.toDouble()
        val scale = if (width > height) {
            maxDim / width
        } else {
            maxDim / height
        }

        val newSize = if (width > maxDim || height > maxDim) {
            val newWidth = width * scale
            val newHeight = height * scale
            CGSizeMake(newWidth, newHeight)
        } else {
            CGSizeMake(width, height)
        }

        // Resize image
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        originalImage.drawInRect(CGRectMake(0.0, 0.0, newSize.width, newSize.height))
        val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        if (resizedImage == null) {
            throw IllegalStateException("Failed to resize image")
        }

        // Compress to JPEG
        val compressionQuality = (quality / 100.0)
        val compressedData = resizedImage.JPEGRepresentationWithCompressionQuality(compressionQuality)
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
