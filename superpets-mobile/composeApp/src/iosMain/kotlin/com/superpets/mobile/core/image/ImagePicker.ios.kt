package com.superpets.mobile.core.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIImage
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
actual class ImagePicker {
    @Composable
    actual fun rememberCameraLauncher(
        onImageCaptured: (ByteArray) -> Unit,
        onPermissionDenied: (() -> Unit)?
    ): () -> Unit {
        val viewController = LocalUIViewController.current

        return remember {
            {
                val picker = UIImagePickerController()
                picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera

                val delegate = object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
                    override fun imagePickerController(
                        picker: UIImagePickerController,
                        didFinishPickingMediaWithInfo: Map<Any?, *>
                    ) {
                        // Get the captured image from info dictionary
                        val image = didFinishPickingMediaWithInfo.get(platform.UIKit.UIImagePickerControllerOriginalImage) as? UIImage
                        if (image != null) {
                            // Convert UIImage to JPEG data with 80% quality to match Android
                            val jpegData = platform.UIKit.UIImageJPEGRepresentation(image, 0.8)
                            if (jpegData != null) {
                                val byteArray = jpegData.toByteArray()
                                onImageCaptured(byteArray)
                            }
                        }
                        picker.dismissViewControllerAnimated(true, null)
                    }

                    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                        picker.dismissViewControllerAnimated(true, null)
                    }
                }

                picker.delegate = delegate
                viewController.presentViewController(picker, true, null)
            }
        }
    }

    @Composable
    actual fun rememberGalleryLauncher(
        maxImages: Int,
        onImagesSelected: (List<ByteArray>) -> Unit
    ): () -> Unit {
        val viewController = LocalUIViewController.current

        return remember {
            {
                val configuration = PHPickerConfiguration()
                configuration.selectionLimit = maxImages.toLong()
                configuration.filter = platform.PhotosUI.PHPickerFilter.imagesFilter

                val picker = PHPickerViewController(configuration)

                val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
                    override fun picker(
                        picker: PHPickerViewController,
                        didFinishPicking: List<*>
                    ) {
                        // Dismiss the picker first
                        picker.dismissViewControllerAnimated(true, null)

                        val results = didFinishPicking.filterIsInstance<PHPickerResult>()

                        // If no results (user cancelled), don't do anything
                        if (results.isEmpty()) {
                            return
                        }

                        val byteArrays = mutableListOf<ByteArray>()
                        var loadedCount = 0

                        results.forEach { result ->
                            result.itemProvider.loadDataRepresentationForTypeIdentifier(
                                typeIdentifier = "public.image",
                                completionHandler = { data, error ->
                                    loadedCount++

                                    if (error != null) {
                                        Napier.e("Failed to load image: ${error.localizedDescription}")
                                    } else if (data != null) {
                                        val byteArray = data.toByteArray()
                                        byteArrays.add(byteArray)
                                    }

                                    // Call callback when all images are loaded (or failed)
                                    if (loadedCount == results.size && byteArrays.isNotEmpty()) {
                                        onImagesSelected(byteArrays)
                                    }
                                }
                            )
                        }
                    }
                }

                picker.delegate = delegate
                viewController.presentViewController(picker, true, null)
            }
        }
    }

    // Extension to convert NSData to ByteArray
    private fun NSData.toByteArray(): ByteArray {
        return ByteArray(this.length.toInt()).apply {
            usePinned { pinned ->
                platform.posix.memcpy(pinned.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
            }
        }
    }
}

