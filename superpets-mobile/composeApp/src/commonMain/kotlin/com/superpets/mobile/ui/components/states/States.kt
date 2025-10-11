package com.superpets.mobile.ui.components.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.superpets.mobile.ui.components.buttons.PrimaryButton
import com.superpets.mobile.ui.components.buttons.SecondaryButton
import com.superpets.mobile.ui.theme.spacing

/**
 * Empty state component for when there's no content to display.
 *
 * @param title Title text
 * @param message Description message
 * @param icon Optional icon
 * @param actionText Optional action button text
 * @param onActionClick Optional action button click handler
 * @param modifier Optional modifier
 */
@Composable
fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.Default.Search,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    val spacing = MaterialTheme.spacing

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.sectionGap)
        ) {
            // Icon
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            // Message
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Action button
            if (actionText != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(spacing.space2))
                PrimaryButton(
                    text = actionText,
                    onClick = onActionClick,
                    fullWidth = false,
                    modifier = Modifier.padding(horizontal = spacing.sectionGap)
                )
            }
        }
    }
}

/**
 * Error state component for displaying errors.
 *
 * @param title Error title
 * @param message Error message
 * @param modifier Optional modifier
 * @param icon Optional error icon
 * @param retryText Retry button text
 * @param onRetryClick Retry button click handler
 * @param dismissText Optional dismiss button text
 * @param onDismissClick Optional dismiss button click handler
 */
@Composable
fun ErrorState(
    title: String,
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Warning,
    retryText: String = "Try Again",
    dismissText: String? = null,
    onDismissClick: (() -> Unit)? = null
) {
    val spacing = MaterialTheme.spacing

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.sectionGap)
        ) {
            // Error icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.error
            )

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            // Message
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Action buttons
            Spacer(modifier = Modifier.height(spacing.space2))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.space2),
                modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.sectionGap)
            ) {
                PrimaryButton(
                    text = retryText,
                    onClick = onRetryClick,
                    fullWidth = true
                )

                if (dismissText != null && onDismissClick != null) {
                    SecondaryButton(
                        text = dismissText,
                        onClick = onDismissClick,
                        fullWidth = true
                    )
                }
            }
        }
    }
}

/**
 * Insufficient credits error state.
 * Specialized error state for when user doesn't have enough credits.
 *
 * @param creditsNeeded Number of credits needed
 * @param currentCredits User's current credit balance
 * @param onBuyCreditsClick Buy credits button click handler
 * @param onDismissClick Dismiss button click handler
 * @param modifier Optional modifier
 */
@Composable
fun InsufficientCreditsState(
    creditsNeeded: Int,
    currentCredits: Int,
    onBuyCreditsClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing
    val deficit = creditsNeeded - currentCredits

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.sectionGap)
        ) {
            // Warning icon with emoji
            Text(
                text = "💳",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 80.sp
                )
            )

            // Title
            Text(
                text = "Not Enough Credits",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            // Message
            Text(
                text = "You need $deficit more credit${if (deficit > 1) "s" else ""} to generate this image.\n\nCurrent balance: $currentCredits credits\nRequired: $creditsNeeded credits",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Action buttons
            Spacer(modifier = Modifier.height(spacing.space2))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.space2),
                modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.sectionGap)
            ) {
                PrimaryButton(
                    text = "Buy More Credits",
                    onClick = onBuyCreditsClick,
                    fullWidth = true
                )

                SecondaryButton(
                    text = "Go Back",
                    onClick = onDismissClick,
                    fullWidth = true
                )
            }
        }
    }
}

/**
 * Network error state with specific messaging.
 *
 * @param onRetryClick Retry button click handler
 * @param modifier Optional modifier
 */
@Composable
fun NetworkErrorState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorState(
        title = "Connection Error",
        message = "Unable to connect to the internet. Please check your connection and try again.",
        icon = Icons.Default.Info,
        onRetryClick = onRetryClick,
        modifier = modifier
    )
}

/**
 * No results found state for search/filter scenarios.
 *
 * @param searchQuery The search query that returned no results
 * @param onClearClick Clear search button click handler
 * @param modifier Optional modifier
 */
@Composable
fun NoResultsState(
    searchQuery: String,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EmptyState(
        title = "No Results Found",
        message = "We couldn't find any results for \"$searchQuery\".\nTry adjusting your search.",
        icon = Icons.Default.Search,
        actionText = "Clear Search",
        onActionClick = onClearClick,
        modifier = modifier
    )
}
