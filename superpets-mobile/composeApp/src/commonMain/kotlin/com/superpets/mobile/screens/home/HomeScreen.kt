package com.superpets.mobile.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.superpets.mobile.data.models.EditHistory
import com.superpets.mobile.ui.theme.*
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val spacing = MaterialTheme.spacing

    // Refresh data when screen becomes visible (to show new generations)
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(spacing.space5)
    ) {
        // Header with title and credits
        item {
            HomeHeader(credits = uiState.credits)
        }

        // Create New button
        item {
            CreateNewButton(onClick = onNavigateToCreate)
        }

        // Quick Stats
        item {
            QuickStatsSection(
                totalCreations = uiState.totalEditCount,
                lastActivityTimestamp = uiState.recentEdits.firstOrNull()?.timestamp
            )
        }

        // Recent Creations
        if (uiState.recentEdits.isNotEmpty()) {
            item {
                RecentCreationsSection(edits = uiState.recentEdits.take(4))
            }
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
                ErrorBanner(
                    message = errorMessage,
                    onDismiss = { viewModel.clearError() }
                )
            }
        }
    }
}

/**
 * Header with "Superpets" title and credit badge (matching Stitch design)
 */
@Composable
private fun HomeHeader(credits: Int) {
    val spacing = MaterialTheme.spacing

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title
        Text(
            text = "Superpets",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Credits badge
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = Primary.copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💎",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$credits credits",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }
        }
    }
}

/**
 * Large yellow "Create New" button with camera icon (matching Stitch design)
 */
@Composable
private fun CreateNewButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Camera icon (simplified - using Add icon as placeholder)
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Create New",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/**
 * Quick Stats section with 2-column grid (matching Stitch design)
 */
@Composable
private fun QuickStatsSection(
    totalCreations: Int,
    lastActivityTimestamp: String?
) {
    val spacing = MaterialTheme.spacing

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.space3)
    ) {
        Text(
            text = "Quick Stats",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing.space3)
        ) {
            // Total creations card
            StatCard(
                label = "Total creations",
                value = "$totalCreations",
                modifier = Modifier.weight(1f)
            )

            // Recent activity card
            StatCard(
                label = "Recent activity",
                value = formatRecentActivity(lastActivityTimestamp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(spacing.space4)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing.space1)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Recent Creations 2x2 grid (matching Stitch design)
 */
@Composable
private fun RecentCreationsSection(edits: List<EditHistory>) {
    val spacing = MaterialTheme.spacing

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.space3)
    ) {
        Text(
            text = "Recent Creations",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // 2x2 grid of images
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.space3)
        ) {
            edits.chunked(2).forEach { rowEdits ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.space3)
                ) {
                    rowEdits.forEach { edit ->
                        CreationImageCard(
                            imageUrl = edit.outputImages.firstOrNull() ?: "",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Fill empty space if odd number
                    if (rowEdits.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CreationImageCard(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Gray200
        )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Creation",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * Error banner for displaying errors
 */

@Composable
private fun ErrorBanner(
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
