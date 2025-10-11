package com.superpets.mobile.ui.components.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.superpets.mobile.ui.theme.CustomShapes
import com.superpets.mobile.ui.theme.PrimaryGradientEnd
import com.superpets.mobile.ui.theme.PrimaryGradientStart
import com.superpets.mobile.ui.theme.spacing

/**
 * Credit badge displaying user's credit count.
 *
 * Shows credit icon (💎) and credit count in a rounded badge.
 *
 * @param credits Number of credits
 * @param modifier Optional modifier
 * @param useGradient Whether to use gradient background (for special badges)
 */
@Composable
fun CreditBadge(
    credits: Int,
    modifier: Modifier = Modifier,
    useGradient: Boolean = false
) {
    val spacing = MaterialTheme.spacing

    Surface(
        modifier = modifier,
        shape = CustomShapes.full,
        color = if (useGradient) Color.Transparent else MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .then(
                    if (useGradient) {
                        Modifier.background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    PrimaryGradientStart,
                                    PrimaryGradientEnd
                                )
                            )
                        )
                    } else Modifier
                )
                .padding(
                    horizontal = spacing.small,
                    vertical = spacing.space2
                ),
            horizontalArrangement = Arrangement.spacedBy(spacing.space1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💎",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "$credits credits",
                style = MaterialTheme.typography.labelMedium,
                color = if (useGradient) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.primary
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Free credits badge with special gradient styling.
 * Used on landing/onboarding screens.
 *
 * @param credits Number of free credits
 * @param modifier Optional modifier
 */
@Composable
fun FreeCreditsGradientBadge(
    credits: Int,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Surface(
        modifier = modifier,
        shape = CustomShapes.full,
        color = Color.Transparent,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PrimaryGradientStart,
                            PrimaryGradientEnd
                        )
                    )
                )
                .padding(
                    horizontal = spacing.medium,
                    vertical = spacing.space2
                ),
            horizontalArrangement = Arrangement.spacedBy(spacing.space1),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Calendar icon placeholder (from Stitch design)
            Text(
                text = "📅",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "$credits Free Credits",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Status badge for various states (success, warning, error, info).
 *
 * @param text Badge text
 * @param status Badge status type
 * @param modifier Optional modifier
 */
@Composable
fun StatusBadge(
    text: String,
    status: BadgeStatus = BadgeStatus.INFO,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    val (backgroundColor, textColor) = when (status) {
        BadgeStatus.SUCCESS -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        BadgeStatus.WARNING -> Color(0xFFF59E0B) to Color.White
        BadgeStatus.ERROR -> MaterialTheme.colorScheme.error to MaterialTheme.colorScheme.onError
        BadgeStatus.INFO -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = modifier,
        shape = CustomShapes.full,
        color = backgroundColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = spacing.small,
                vertical = spacing.space1
            )
        )
    }
}

/**
 * Badge status types.
 */
enum class BadgeStatus {
    SUCCESS,
    WARNING,
    ERROR,
    INFO
}
