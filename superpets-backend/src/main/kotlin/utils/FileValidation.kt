package com.alicankorkmaz.utils

/**
 * File validation utilities for upload endpoints.
 */
object FileValidation {

    // Maximum file size: 10MB
    const val MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024L

    // Allowed image MIME types
    val ALLOWED_CONTENT_TYPES = setOf(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/webp",
        "image/gif"
    )

    // Allowed file extensions
    val ALLOWED_EXTENSIONS = setOf(
        "jpg",
        "jpeg",
        "png",
        "webp",
        "gif"
    )

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    /**
     * Validate file size.
     */
    fun validateFileSize(fileSize: Long): ValidationResult {
        return if (fileSize > MAX_FILE_SIZE_BYTES) {
            ValidationResult(
                isValid = false,
                errorMessage = "File size exceeds maximum allowed size of ${MAX_FILE_SIZE_BYTES / 1024 / 1024}MB. Your file is ${fileSize / 1024 / 1024}MB."
            )
        } else if (fileSize <= 0) {
            ValidationResult(
                isValid = false,
                errorMessage = "File is empty or invalid."
            )
        } else {
            ValidationResult(isValid = true)
        }
    }

    /**
     * Validate content type.
     */
    fun validateContentType(contentType: String): ValidationResult {
        val normalizedContentType = contentType.lowercase().trim()
        return if (normalizedContentType in ALLOWED_CONTENT_TYPES) {
            ValidationResult(isValid = true)
        } else {
            ValidationResult(
                isValid = false,
                errorMessage = "Invalid file type: $contentType. Allowed types: ${ALLOWED_CONTENT_TYPES.joinToString(", ")}"
            )
        }
    }

    /**
     * Validate file extension from filename.
     */
    fun validateFileExtension(filename: String): ValidationResult {
        val extension = filename.substringAfterLast('.', "").lowercase()
        return if (extension.isEmpty()) {
            ValidationResult(
                isValid = false,
                errorMessage = "File has no extension. Please upload a valid image file."
            )
        } else if (extension in ALLOWED_EXTENSIONS) {
            ValidationResult(isValid = true)
        } else {
            ValidationResult(
                isValid = false,
                errorMessage = "Invalid file extension: .$extension. Allowed extensions: ${ALLOWED_EXTENSIONS.joinToString(", ") { ".$it" }}"
            )
        }
    }

    /**
     * Comprehensive file validation.
     */
    fun validateFile(
        fileSize: Long,
        contentType: String,
        filename: String
    ): ValidationResult {
        // Check file size
        val sizeValidation = validateFileSize(fileSize)
        if (!sizeValidation.isValid) {
            return sizeValidation
        }

        // Check content type
        val contentTypeValidation = validateContentType(contentType)
        if (!contentTypeValidation.isValid) {
            return contentTypeValidation
        }

        // Check file extension
        val extensionValidation = validateFileExtension(filename)
        if (!extensionValidation.isValid) {
            return extensionValidation
        }

        return ValidationResult(isValid = true)
    }
}
