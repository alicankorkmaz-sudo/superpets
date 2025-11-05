package com.superpets.mobile.core.image

import androidx.compose.runtime.Composable

/**
 * Platform-specific image picker for camera and gallery access.
 * Uses expect/actual pattern for Android (ActivityResultContracts) and iOS (PHPicker/UIImagePicker).
 */
expect class ImagePicker {
    /**
     * Launch camera to take a photo
     * @param onImageCaptured Callback with captured image as ByteArray
     */
    @Composable
    fun rememberCameraLauncher(
        onImageCaptured: (ByteArray) -> Unit
    ): () -> Unit

    /**
     * Launch gallery to select multiple photos
     * @param maxImages Maximum number of images to select
     * @param onImagesSelected Callback with selected images as List<ByteArray>
     */
    @Composable
    fun rememberGalleryLauncher(
        maxImages: Int = 10,
        onImagesSelected: (List<ByteArray>) -> Unit
    ): () -> Unit
}
