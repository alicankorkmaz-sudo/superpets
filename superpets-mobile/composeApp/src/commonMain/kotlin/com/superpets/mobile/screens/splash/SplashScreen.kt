package com.superpets.mobile.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    navigateToOnboarding: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToAuth: () -> Unit,
    viewModel: SplashViewModel
) {
    val navigationTarget by viewModel.navigationTarget.collectAsState()

    // Show loading indicator while checking auth state
    // (Native splash screens already show the app icon)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

    LaunchedEffect(navigationTarget) {
        when (navigationTarget) {
            NavigationTarget.Landing -> navigateToOnboarding()
            NavigationTarget.Auth -> navigateToAuth()
            NavigationTarget.Main -> navigateToHome()
            null -> {
                // Do nothing, wait for the value to be loaded
            }
        }
    }
} 