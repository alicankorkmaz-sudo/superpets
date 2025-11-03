package com.superpets.mobile.core.image

/**
 * Platform-specific image compression utility.
 * Compresses images to max 2048x2048 to prevent exceeding backend's 10MB limit.
 */
expect class ImageCompressor() {
    /**
     * Compresses an image to a maximum dimension of 2048x2048.
     *
     * @param imageData The raw image data
     * @param maxDimension Maximum width/height (default: 2048)
     * @param quality JPEG quality 0-100 (default: 85)
     * @return Compressed image as ByteArray
     */
    suspend fun compress(
        imageData: ByteArray,
        maxDimension: Int = 2048,
        quality: Int = 85
    ): ByteArray
}
