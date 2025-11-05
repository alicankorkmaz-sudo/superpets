package com.superpets.mobile.core.image

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Decode ByteArray to ImageBitmap for displaying in Compose UI
 */
expect fun ByteArray.toImageBitmap(): ImageBitmap
