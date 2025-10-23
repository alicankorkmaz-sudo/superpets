package com.superpets.mobile.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.superpets.mobile.ui.theme.*
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val spacing = MaterialTheme.spacing

    Scaffold(
        topBar = {
            ProfileTopBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(spacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(spacing.space5)
        ) {
            // User info section (avatar, name, email, joined date)
            item {
                UserInfoSection(
                    email = uiState.user?.email ?: "",
                    createdAt = uiState.user?.createdAt
                )
            }

            // Credits card
            item {
                CreditsCard(
                    credits = uiState.user?.credits ?: 0,
                    onBuyMore = { /* TODO: Navigate to pricing */ }
                )
            }

            // Account section
            item {
                AccountSection(onNavigateToPrivacy = {}, onNavigateToTerms = {})
            }

            // History section
            item {
                HistorySection(onNavigateToEditHistory = {})
            }

            // Log Out button
            item {
                LogOutButton(
                    onLogOut = { viewModel.signOut() },
                    isLoading = uiState.isSigningOut
                )
            }

            // Loading state
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            // Error message
            uiState.error?.let { errorMessage ->
                item {
                    ErrorCard(
                        message = errorMessage,
                        onDismiss = { viewModel.clearError() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO: Navigate back */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/**
 * User info section with centered avatar, name, username, and joined date
 */
@Composable
private fun UserInfoSection(
    email: String,
    createdAt: String?
) {
    val spacing = MaterialTheme.spacing

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.space2)
    ) {
        // Avatar (first letter of email)
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = email.firstOrNull()?.uppercase() ?: "U",
                style = MaterialTheme.typography.displayLarge,
                color = Primary,
                fontWeight = FontWeight.Bold
            )
        }

        // Name (derived from email)
        val displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Username
        Text(
            text = "@${email.substringBefore("@")}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Joined date
        createdAt?.let {
            Text(
                text = "Joined ${formatYear(it)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Credits card with icon, balance, and "Buy More" button
 */
@Composable
private fun CreditsCard(
    credits: Int,
    onBuyMore: () -> Unit
) {
    val spacing = MaterialTheme.spacing

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.space4),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.space3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon background
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Primary.copy(alpha = 0.1f)
                ) {
                    Box(
                        modifier = Modifier.padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "💎",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                // Credits info
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "$credits Credits",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Your current balance",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Buy More button
            Button(
                onClick = onBuyMore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Buy More",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Account section with settings options
 */
@Composable
private fun AccountSection(
    onNavigateToPrivacy: () -> Unit,
    onNavigateToTerms: () -> Unit
) {
    val spacing = MaterialTheme.spacing

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.space2)
    ) {
        Text(
            text = "Account",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Notifications toggle
                SettingsRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    onClick = null,
                    trailing = {
                        // TODO: Implement toggle state
                        Switch(
                            checked = true,
                            onCheckedChange = {},
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = Primary
                            )
                        )
                    }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Privacy
                SettingsRow(
                    icon = Icons.Default.Lock,
                    title = "Privacy",
                    onClick = onNavigateToPrivacy,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                // Terms of Service
                SettingsRow(
                    icon = Icons.Default.Info,
                    title = "Terms of Service",
                    onClick = onNavigateToTerms,
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }
    }
}

/**
 * History section with Edit History link
 */
@Composable
private fun HistorySection(onNavigateToEditHistory: () -> Unit) {
    val spacing = MaterialTheme.spacing

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.space2)
    ) {
        Text(
            text = "History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            SettingsRow(
                icon = Icons.Default.DateRange,
                title = "Edit History",
                onClick = onNavigateToEditHistory,
                trailing = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}

/**
 * Reusable settings row component
 */
@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: (() -> Unit)?,
    trailing: @Composable () -> Unit
) {
    val modifier = if (onClick != null) {
        Modifier
            .fillMaxWidth()
    } else {
        Modifier.fillMaxWidth()
    }

    Surface(
        onClick = onClick ?: {},
        modifier = modifier,
        enabled = onClick != null,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            trailing()
        }
    }
}

/**
 * Log Out button (gray)
 */
@Composable
private fun LogOutButton(
    onLogOut: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = onLogOut,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Gray200,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            Text(
                text = "Log Out",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Error card
 */
@Composable
private fun ErrorCard(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Error.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Error,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = Error)
            }
        }
    }
}

/**
 * Format year from ISO timestamp
 */
private fun formatYear(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        dateTime.year.toString()
    } catch (e: Exception) {
        "2023"
    }
}
