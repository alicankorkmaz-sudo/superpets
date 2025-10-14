package com.superpets.mobile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import com.superpets.mobile.data.auth.AuthManager
import io.github.aakira.napier.Napier
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val authManager: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle deep link from intent
        handleDeepLink(intent)

        setContent {
            // Remove when https://issuetracker.google.com/issues/364713509 is fixed
            LaunchedEffect(isSystemInDarkTheme()) {
                enableEdgeToEdge()
            }

            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { data ->
            if (data.scheme == "superpets" && data.host == "auth") {
                Napier.d("Deep link received: ${data}")
                // Use Supabase's built-in handleDeeplinks method
                // This handles both implicit and PKCE flow
                authManager.supabaseClient.handleDeeplinks(intent) { session ->
                    val email = session.user?.email ?: ""
                    Napier.d("Session imported successfully from deep link: $email")
                    authManager.onDeepLinkSuccess(email)
                }
            }
        }
    }
}
