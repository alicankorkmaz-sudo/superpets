package com.superpets.mobile.core.image

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage

/**
 * iOS implementation of ByteArray to ImageBitmap conversion
 */
@OptIn(ExperimentalForeignApi::class)
actual fun ByteArray.toImageBitmap(): ImageBitmap {
    // Convert ByteArray to NSData
    val nsData = this.usePinned { pinned ->
        NSData.create(
            bytes = pinned.addressOf(0),
            length = this.size.toULong()
        )
    }

    // Convert NSData to UIImage
    val uiImage = UIImage.imageWithData(nsData)
        ?: throw IllegalArgumentException("Failed to decode image data")

    // Convert UIImage to Skia Image, then to ImageBitmap
    // For iOS, we need to use UIImage's PNG representation and decode via Skia
    val pngData = platform.UIKit.UIImagePNGRepresentation(uiImage)
        ?: throw IllegalArgumentException("Failed to convert UIImage to PNG")

    val pngByteArray = ByteArray(pngData.length.toInt())
    pngByteArray.usePinned { pinned ->
        platform.posix.memcpy(pinned.addressOf(0), pngData.bytes, pngData.length)
    }

    val skiaImage = Image.makeFromEncoded(pngByteArray)
    return skiaImage.toComposeImageBitmap()
}
