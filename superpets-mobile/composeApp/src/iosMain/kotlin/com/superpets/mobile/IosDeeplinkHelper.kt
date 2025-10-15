package com.superpets.mobile

import com.superpets.mobile.data.auth.AuthManager
import io.github.aakira.napier.Napier
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.parseFragmentAndImportSession
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

/**
 * iOS deeplink handler for OAuth authentication
 * This function should be called from Swift when a deeplink is received
 *
 * @param url The deeplink URL as a string (e.g., "superpets://auth#access_token=...")
 */
fun handleIOSDeepLink(url: String) {
    DeepLinkHandler.handleDeepLink(url)
}

/**
 * Helper object to handle deeplinks on iOS
 * This mimics the Android handleDeeplinks function but works with URL strings
 */
private object DeepLinkHandler : KoinComponent {
    private val authManager: AuthManager by inject()
    private val scope = MainScope()

    @OptIn(SupabaseInternal::class)
    fun handleDeepLink(urlString: String) {
        Napier.d("iOS Deep link received: $urlString")

        // Parse URL components manually
        if (!urlString.startsWith("superpets://auth")) {
            Napier.w("Deep link URL does not match expected scheme/host")
            return
        }

        when (authManager.supabaseClient.auth.config.flowType) {
            FlowType.IMPLICIT -> {
                // Extract fragment (everything after #)
                val fragment = urlString.substringAfter("#", "")
                if (fragment.isEmpty()) {
                    Napier.w("No fragment found in implicit flow deeplink")
                    return
                }

                Napier.d("Parsing implicit flow fragment: $fragment")
                authManager.supabaseClient.auth.parseFragmentAndImportSession(fragment) { session ->
                    val email = session.user?.email ?: ""
                    Napier.d("Session imported successfully from deep link: $email")
                    authManager.onDeepLinkSuccess(email)
                }
            }
            FlowType.PKCE -> {
                // Extract query parameter 'code' (everything after ? and before &)
                val query = urlString.substringAfter("?", "")
                val code = query.split("&")
                    .find { it.startsWith("code=") }
                    ?.substringAfter("code=")

                if (code.isNullOrEmpty()) {
                    Napier.w("No code parameter found in PKCE flow deeplink")
                    return
                }

                Napier.d("Exchanging PKCE code for session")
                scope.launch {
                    try {
                        authManager.supabaseClient.auth.exchangeCodeForSession(code)
                        val session = authManager.supabaseClient.auth.currentSessionOrNull()
                        if (session != null) {
                            val email = session.user?.email ?: ""
                            Napier.d("Session imported successfully from PKCE flow: $email")
                            authManager.onDeepLinkSuccess(email)
                        } else {
                            Napier.e("No session available after PKCE code exchange")
                        }
                    } catch (e: Exception) {
                        Napier.e("Failed to exchange PKCE code for session", e)
                    }
                }
            }
        }
    }
}
