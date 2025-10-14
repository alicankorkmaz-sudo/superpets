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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.superpets.mobile.data.auth.AuthState
import com.superpets.mobile.screens.auth.AuthViewModel
import com.superpets.mobile.ui.components.buttons.LargePrimaryButton
import com.superpets.mobile.ui.components.buttons.SecondaryButton
import com.superpets.mobile.ui.components.badges.FreeCreditsGradientBadge
import com.superpets.mobile.ui.theme.spacing
import com.superpets.mobile.ui.theme.Primary

/**
 * Landing/Onboarding screen matching Stitch design
 *
 * Shows app value proposition with hero image showcase.
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(spacing.screenPadding)
    ) {
        // Top bar with logo and free credits badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo with mascot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.space2)
            ) {
                // Mascot placeholder (TODO: Replace with actual mascot image)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🦸",
                        fontSize = 24.sp
                    )
                }
                Text(
                    text = "Superpets",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Free credits badge with gradient
            FreeCreditsGradientBadge(credits = 5)
        }

        // Main content centered
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(spacing.space8))

            // Hero image showcase (placeholder cards)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(4f / 3f)
                    .padding(vertical = spacing.space6)
            ) {
                // Left card (rotated)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight()
                        .rotate(-8f)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .align(Alignment.CenterStart)
                        .padding(start = spacing.space4),
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Replace with actual before image
                    Text(
                        text = "Before",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Right card (rotated, elevated)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight()
                        .rotate(8f)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .align(Alignment.CenterEnd)
                        .padding(end = spacing.space4),
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Replace with actual after image
                    Text(
                        text = "After",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }

            // Headline with colored "Superhero"
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Black
                        )
                    ) {
                        append("Turn Your Pet into\na ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Primary,
                            fontWeight = FontWeight.Black
                        )
                    ) {
                        append("Superhero")
                    }
                },
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(spacing.space4))

            // Value proposition
            Text(
                text = "AI-Powered • 29+ Heroes • Lightning Fast",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.space8))

            // CTA Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.space4),
                verticalArrangement = Arrangement.spacedBy(spacing.space4)
            ) {
                LargePrimaryButton(
                    text = "Get Started",
                    onClick = {
                        landingViewModel.completeOnboarding()
                        onOnboardingComplete()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                SecondaryButton(
                    text = "Sign In",
                    onClick = {
                        landingViewModel.completeOnboarding()
                        onOnboardingComplete()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(spacing.space8))
        }
    }
}
