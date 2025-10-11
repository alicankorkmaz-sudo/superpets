package com.superpets.mobile.ui.components.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.superpets.mobile.ui.theme.spacing

/**
 * Simple circular loading indicator with primary color.
 *
 * @param modifier Optional modifier
 * @param size Size of the indicator
 * @param strokeWidth Width of the circular stroke
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    strokeWidth: Dp = 4.dp
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = strokeWidth
    )
}

/**
 * Full screen loading overlay with centered spinner and optional message.
 *
 * @param message Optional loading message
 * @param modifier Optional modifier
 */
@Composable
fun LoadingScreen(
    message: String? = null,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            LoadingIndicator(size = 48.dp, strokeWidth = 4.dp)

            if (message != null) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

/**
 * Generation progress screen with custom loading animation.
 * Used during image generation to show progress.
 *
 * @param progress Optional progress value (0.0 to 1.0)
 * @param message Progress message
 * @param modifier Optional modifier
 */
@Composable
fun GenerationLoadingScreen(
    progress: Float? = null,
    message: String = "Generating your superhero...",
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.sectionGap),
            modifier = Modifier.padding(spacing.screenPadding)
        ) {
            // Animated icon or illustration
            AnimatedLoadingIcon(size = 120.dp)

            // Progress text
            Text(
                text = message,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Progress indicator
            if (progress != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing.space2)
                ) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .size(width = 200.dp, height = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                LoadingIndicator(size = 48.dp)
            }
        }
    }
}

/**
 * Animated loading icon with rotation.
 * Can be customized with different content.
 *
 * @param size Size of the icon
 * @param modifier Optional modifier
 */
@Composable
fun AnimatedLoadingIcon(
    size: Dp = 80.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .size(size)
            .rotate(rotation),
        contentAlignment = Alignment.Center
    ) {
        // Placeholder for animated icon
        // TODO: Replace with actual pet/superhero icon
        Text(
            text = "🦸",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = (size.value * 0.6).dp.value.sp
            )
        )
    }
}

/**
 * Skeleton loader for cards while content is loading.
 * Shows a pulsing gray box as placeholder.
 *
 * @param modifier Optional modifier
 */
@Composable
fun SkeletonCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha)
                )
        )
    }
}
