package com.superpets.mobile.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun SplashScreen(
    navigateToOnboarding: () -> Unit,
    navigateToHome: () -> Unit,
    viewModel: SplashViewModel
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()

    LaunchedEffect(isOnboardingCompleted) {
        when (isOnboardingCompleted) {
            true -> navigateToHome()
            false -> navigateToOnboarding()
            null -> {
                // Do nothing, wait for the value to be loaded
            }
        }
    }
} 