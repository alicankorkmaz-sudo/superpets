package com.superpets.mobile.ui.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.superpets.mobile.ui.theme.CustomShapes
import com.superpets.mobile.ui.theme.spacing

/**
 * Hero card for hero selection screen.
 *
 * Displays hero image with gradient overlay and name.
 * Shows selection indicator when selected.
 *
 * @param heroName Name of the hero
 * @param imageUrl URL of the hero image
 * @param isSelected Whether this hero is currently selected
 * @param onClick Click handler
 * @param modifier Optional modifier
 */
@Composable
fun HeroCard(
    heroName: String,
    imageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CustomShapes.large)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CustomShapes.large
                    )
                } else {
                    Modifier.border(
                        width = 2.dp,
                        color = Color.Transparent,
                        shape = CustomShapes.large
                    )
                }
            )
            .clickable(onClick = onClick)
    ) {
        // Hero image
        AsyncImage(
            model = imageUrl,
            contentDescription = heroName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Hero name at bottom
        Text(
            text = heroName,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(spacing.small)
        )

        // Selected indicator
        if (isSelected) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(spacing.space2)
                    .size(24.dp),
                shape = CustomShapes.full,
                color = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

/**
 * History card for displaying past edit generations.
 *
 * @param imageUrl URL of the generated image
 * @param heroName Name of the hero used
 * @param timestamp Timestamp of generation
 * @param creditsUsed Number of credits used
 * @param onClick Click handler
 * @param modifier Optional modifier
 */
@Composable
fun HistoryCard(
    imageUrl: String,
    heroName: String,
    timestamp: String,
    creditsUsed: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = CustomShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image
            AsyncImage(
                model = imageUrl,
                contentDescription = "Generated image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(CustomShapes.large),
                contentScale = ContentScale.Crop
            )

            // Info
            Column(
                modifier = Modifier.padding(spacing.small)
            ) {
                Text(
                    text = heroName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(spacing.space1))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "-$creditsUsed credits",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Credit package card for pricing screen.
 *
 * @param packageName Name of the credit package (e.g., "Starter", "Popular")
 * @param credits Number of credits in the package
 * @param price Price of the package
 * @param imageUrl Optional image URL
 * @param isSelected Whether this package is selected
 * @param isBestValue Whether to show "Best Value" badge
 * @param onClick Click handler
 * @param modifier Optional modifier
 */
@Composable
fun CreditPackageCard(
    packageName: String,
    credits: Int,
    price: String,
    imageUrl: String?,
    isSelected: Boolean,
    isBestValue: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = CustomShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Package image
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = packageName,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CustomShapes.medium),
                        contentScale = ContentScale.Crop
                    )
                }

                // Package info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = packageName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(spacing.space0_5))
                    Text(
                        text = "$credits credits",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(spacing.space0_5))
                    Text(
                        text = price,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Best Value badge
            if (isBestValue) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    color = MaterialTheme.colorScheme.primary,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(
                        bottomStart = CustomShapes.radiusMD
                    )
                ) {
                    Text(
                        text = "Best Value",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(
                            horizontal = spacing.small,
                            vertical = spacing.space1
                        )
                    )
                }
            }
        }
    }
}

/**
 * Stats card for dashboard/home screen.
 *
 * @param label Label text (e.g., "Total creations")
 * @param value Value to display (e.g., "12")
 * @param modifier Optional modifier
 */
@Composable
fun StatsCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier,
        shape = CustomShapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.space1)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
