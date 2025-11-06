package com.superpets.mobile.core.image

/**
 * Platform-specific image sharing utility.
 * Opens the native share sheet to share images from URLs.
 */
expect class ImageSharer() {
    /**
     * Opens the native share sheet to share an image from a URL.
     *
     * @param imageUrl The URL of the image to share
     * @return Result indicating success or failure with error message
     */
    suspend fun shareImage(imageUrl: String): Result<Unit>
}
