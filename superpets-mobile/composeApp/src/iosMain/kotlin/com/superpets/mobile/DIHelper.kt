package com.superpets.mobile

import com.superpets.mobile.data.auth.AuthManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Helper object to access Koin dependencies from iOS Swift code
 *
 * This allows Swift code to access Kotlin dependencies injected by Koin,
 * specifically for handling deep links in the iOS app.
 */
object DIHelper : KoinComponent {
    /**
     * Get AuthManager instance for handling authentication and deep links
     */
    fun getAuthManager(): AuthManager {
        val authManager: AuthManager by inject()
        return authManager
    }
}
