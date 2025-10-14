package com.superpets.mobile.screens.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.superpets.mobile.data.auth.AuthState
import com.superpets.mobile.screens.auth.AuthViewModel
import com.superpets.mobile.ui.components.buttons.LargePrimaryButton
import com.superpets.mobile.ui.theme.spacing

/**
 * Landing/Onboarding screen
 *
 * Shows app value proposition and features.
 * After user taps "Get Started", marks onboarding as complete and navigates based on auth status.
 */
@Composable
fun LandingScreen(
    landingViewModel: LandingViewModel,
    authViewModel: AuthViewModel,
    onOnboardingComplete: () -> Unit
) {
    val spacing = MaterialTheme.spacing
    val authState by authViewModel.authState.collectAsState()

    // When onboarding is marked complete, navigate
    // The SplashViewModel will handle the navigation based on auth state
    LaunchedEffect(Unit) {
        // This screen doesn't need to react to auth state changes
        // The navigation will be handled by the SplashViewModel's logic
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = spacing.screenPadding)
            .padding(vertical = spacing.space8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(spacing.space8))

            // App logo/icon
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SP",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.5,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(spacing.space8))

            // App name
            Text(
                text = "Superpets",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(spacing.space3))

            // Tagline
            Text(
                text = "Transform Your Pets Into Superheroes",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(spacing.space8))

            // Features list
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.space5)
            ) {
                FeatureItem(
                    icon = Icons.Default.CheckCircle,
                    title = "AI-Powered Magic",
                    description = "Using Google's Nano Banana model via fal.ai for stunning transformations"
                )

                FeatureItem(
                    icon = Icons.Default.Star,
                    title = "29+ Unique Heroes",
                    description = "Choose from classic superheroes or unique characters for your pet"
                )

                FeatureItem(
                    icon = Icons.Default.CheckCircle,
                    title = "Lightning Fast",
                    description = "Get your superhero pet images in seconds, not minutes"
                )
            }

            Spacer(modifier = Modifier.height(spacing.space8))

            // Free credits callout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(spacing.space4),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.size(spacing.space2))
                    Text(
                        text = "Start with 5 Free Credits!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bottom section with CTA button
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(spacing.space8))

            LargePrimaryButton(
                text = "Get Started",
                onClick = {
                    landingViewModel.completeOnboarding()
                    onOnboardingComplete()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.space4))

            // Terms text
            Text(
                text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Feature item component
 */
@Composable
private fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        // Text content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
