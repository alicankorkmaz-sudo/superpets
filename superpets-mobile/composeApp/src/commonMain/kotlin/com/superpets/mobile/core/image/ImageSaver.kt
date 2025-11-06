package com.superpets.mobile.core.image

/**
 * Platform-specific image saver utility.
 * Downloads and saves images from URLs to device gallery/photos.
 */
expect class ImageSaver() {
    /**
     * Downloads an image from a URL and saves it to the device gallery.
     *
     * @param imageUrl The URL of the image to download and save
     * @return Result indicating success or failure with error message
     */
    suspend fun saveImage(imageUrl: String): Result<Unit>
}
