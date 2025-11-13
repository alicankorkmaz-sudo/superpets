package com.superpets.mobile.core.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.superpets.mobile.core.ContextProvider

/**
 * Android implementation for permission utilities
 */
actual object PermissionUtils {
    /**
     * Opens the app's settings page where users can manage permissions
     */
    actual fun openAppSettings() {
        val context = ContextProvider.get()

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(intent)
    }
}
