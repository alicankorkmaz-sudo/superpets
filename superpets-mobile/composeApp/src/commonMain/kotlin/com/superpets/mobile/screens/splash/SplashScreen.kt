package com.superpets.mobile.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen(
    navigateToOnboarding: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToAuth: () -> Unit,
    viewModel: SplashViewModel
) {
    val navigationTarget by viewModel.navigationTarget.collectAsState()

    // Show splash logo while loading
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SP",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
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