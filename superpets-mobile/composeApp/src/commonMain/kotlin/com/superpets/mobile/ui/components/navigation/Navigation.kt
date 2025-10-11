package com.superpets.mobile.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.superpets.mobile.ui.components.badges.CreditBadge
import com.superpets.mobile.ui.theme.CustomShapes
import com.superpets.mobile.ui.theme.spacing

/**
 * Top app bar with optional back button, title, and credit badge.
 *
 * @param title Title text
 * @param modifier Optional modifier
 * @param onBackClick Optional back button click handler (if null, no back button shown)
 * @param onCloseClick Optional close button click handler (if null, no close button shown)
 * @param credits Optional credit count to display badge
 * @param actions Optional trailing actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperpetsTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    onCloseClick: (() -> Unit)? = null,
    credits: Int? = null,
    actions: @Composable () -> Unit = {}
) {
    val spacing = MaterialTheme.spacing

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier,
        navigationIcon = {
            when {
                onBackClick != null -> {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
                onCloseClick != null -> {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            }
        },
        actions = {
            if (credits != null) {
                CreditBadge(
                    credits = credits,
                    modifier = Modifier.padding(end = spacing.space2)
                )
            }
            actions()
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

/**
 * Header with logo, app name, and credit badge.
 * Used on landing and home screens.
 *
 * @param credits Credit count to display
 * @param modifier Optional modifier
 */
@Composable
fun SuperpetsHeader(
    credits: Int,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo and app name
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.space2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: Add actual logo image
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CustomShapes.medium,
                color = MaterialTheme.colorScheme.primary
            ) {
                // Placeholder for logo
            }
            Text(
                text = "Superpets",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Credit badge
        CreditBadge(credits = credits)
    }
}

/**
 * Navigation item data class.
 *
 * @param route Navigation route
 * @param icon Icon for the item
 * @param label Label text
 */
data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * Bottom navigation bar matching Superpets design.
 *
 * Features:
 * - Backdrop blur effect
 * - Primary color for selected items
 * - Custom icon sizes
 *
 * @param selectedRoute Currently selected route
 * @param items Navigation items
 * @param onItemSelected Item selection handler
 * @param modifier Optional modifier
 */
@Composable
fun SuperpetsBottomNavigationBar(
    selectedRoute: String,
    items: List<NavigationItem>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
        contentColor = MaterialTheme.colorScheme.onBackground,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedRoute == item.route,
                onClick = { onItemSelected(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

/**
 * Floating bottom navigation with prominent center action button.
 * Alternative navigation style with centered "Create" button.
 *
 * @param selectedRoute Currently selected route
 * @param items Navigation items (should be 4 items, center will be Create button)
 * @param onItemSelected Item selection handler
 * @param onCreateClick Create button click handler
 * @param modifier Optional modifier
 */
@Composable
fun SuperpetsFloatingBottomNav(
    selectedRoute: String,
    items: List<NavigationItem>,
    onItemSelected: (String) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(spacing.navBarHeight),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.medium, vertical = spacing.space2),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First 2 items
            items.take(2).forEach { item ->
                NavigationItemButton(
                    icon = item.icon,
                    label = item.label,
                    isSelected = selectedRoute == item.route,
                    onClick = { onItemSelected(item.route) }
                )
            }

            // Center Create button
            IconButton(
                onClick = onCreateClick,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        ),
                        shape = CustomShapes.full
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Last 2 items
            items.drop(2).take(2).forEach { item ->
                NavigationItemButton(
                    icon = item.icon,
                    label = item.label,
                    isSelected = selectedRoute == item.route,
                    onClick = { onItemSelected(item.route) }
                )
            }
        }
    }
}

@Composable
private fun NavigationItemButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.space0_5)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}
