package com.superpets.mobile.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.superpets.mobile.data.models.EditHistory
import org.koin.compose.viewmodel.koinViewModel

/**
 * History Screen - Shows user's edit history
 * Follows Stitch design from stitch_superpets/edit_history_screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: HistoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDateFilterDialog by remember { mutableStateOf(false) }
    var showHeroFilterDialog by remember { mutableStateOf(false) }

    // Filter dialogs
    if (showDateFilterDialog) {
        DateFilterDialog(
            currentFilter = uiState.dateFilter,
            onDismiss = { showDateFilterDialog = false },
            onFilterSelected = { filter ->
                viewModel.setDateFilter(filter)
                showDateFilterDialog = false
            }
        )
    }

    if (showHeroFilterDialog) {
        HeroFilterDialog(
            heroes = viewModel.getUniqueHeroNames(),
            selectedHero = uiState.selectedHeroFilter,
            onDismiss = { showHeroFilterDialog = false },
            onHeroSelected = { hero ->
                viewModel.setHeroFilter(hero)
                showHeroFilterDialog = false
            },
            onClearFilter = {
                viewModel.setHeroFilter(null)
                showHeroFilterDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFFDF8)) // Light beige background
            ) {
                // Header
                TopAppBar(
                    title = {
                        Text(
                            text = "History",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        // Empty spacer to keep title centered
                        Spacer(modifier = Modifier.width(48.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFFFDF8)
                    )
                )

                // Filter buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Date filter
                    FilterButton(
                        text = uiState.dateFilter.label,
                        isActive = uiState.dateFilter != DateFilter.NEWEST_FIRST,
                        onClick = { showDateFilterDialog = true }
                    )

                    // Hero filter
                    FilterButton(
                        text = uiState.selectedHeroFilter ?: "Hero",
                        isActive = uiState.selectedHeroFilter != null,
                        onClick = { showHeroFilterDialog = true }
                    )

                    // Clear filters (only show if any filter is active)
                    if (uiState.dateFilter != DateFilter.NEWEST_FIRST || uiState.selectedHeroFilter != null) {
                        TextButton(
                            onClick = { viewModel.clearFilters() },
                            modifier = Modifier.height(40.dp),
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color(0xFFFFC629)
                            )
                        ) {
                            Text(
                                text = "Clear",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFDF8)) // Light beige background
        ) {
            when {
                uiState.isLoading -> {
                    // Loading state
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFFFC629)
                    )
                }

                uiState.error != null -> {
                    // Error state
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            color = Color.Red,
                            fontSize = 16.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.refresh() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC629)
                            )
                        ) {
                            Text("Retry", color = Color.Black)
                        }
                    }
                }

                uiState.filteredEdits.isEmpty() -> {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No history yet",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Create your first Superpet to see it here!",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                else -> {
                    // Grid of history items
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.filteredEdits) { edit ->
                            HistoryItem(edit = edit)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Filter button component
 */
@Composable
private fun FilterButton(
    text: String,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(999.dp), // Fully rounded
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color(0xFFFFC629) else Color(0xFFFFC629).copy(alpha = 0.1f),
            contentColor = if (isActive) Color.Black else Color(0xFFFFC629)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * History item card component
 */
@Composable
private fun HistoryItem(edit: EditHistory) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Image
        AsyncImage(
            model = edit.outputImages.firstOrNull(),
            contentDescription = "Generated superpet image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF2D5F5D)),
            contentScale = ContentScale.Crop
        )

        // Details
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Extract hero name from prompt or use "Unknown Hero"
            val heroName = extractHeroName(edit.prompt)
            Text(
                text = heroName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Format timestamp
            val formattedDate = formatTimestamp(edit.timestamp)
            Text(
                text = formattedDate,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "Credits: ${edit.creditsCost}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Extract hero name from prompt
 * Example: "Transform the pet into Batman..." -> "Batman"
 */
private fun extractHeroName(prompt: String): String {
    val regex = """Transform the pet into ([\w\s]+)\.""".toRegex()
    val match = regex.find(prompt)
    return match?.groupValues?.getOrNull(1)?.trim() ?: "Superpet"
}

/**
 * Format ISO timestamp to readable date
 * Example: "2024-07-26T14:30:00Z" -> "2024-07-26 14:30"
 */
private fun formatTimestamp(timestamp: String): String {
    return try {
        // Simple parsing - replace T with space and remove Z and milliseconds
        timestamp
            .replace("T", " ")
            .replace("Z", "")
            .substringBefore(".")
            .take(16) // Take only YYYY-MM-DD HH:MM
    } catch (e: Exception) {
        timestamp
    }
}

/**
 * Date filter dialog
 */
@Composable
private fun DateFilterDialog(
    currentFilter: DateFilter,
    onDismiss: () -> Unit,
    onFilterSelected: (DateFilter) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Sort by Date",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                DateFilter.entries.forEach { filter ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFilterSelected(filter) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = filter == currentFilter,
                            onClick = { onFilterSelected(filter) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFFFC629)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = filter.label,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFFFFC629))
            }
        },
        containerColor = Color.White
    )
}

/**
 * Hero filter dialog
 */
@Composable
private fun HeroFilterDialog(
    heroes: List<String>,
    selectedHero: String?,
    onDismiss: () -> Unit,
    onHeroSelected: (String) -> Unit,
    onClearFilter: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filter by Hero",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                // Clear filter option
                if (selectedHero != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClearFilter() }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = false,
                            onClick = { onClearFilter() },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFFFC629)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "All Heroes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider()
                }

                // Hero list
                heroes.forEach { hero ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onHeroSelected(hero) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = hero == selectedHero,
                            onClick = { onHeroSelected(hero) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFFFC629)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = hero,
                            fontSize = 16.sp
                        )
                    }
                }

                if (heroes.isEmpty()) {
                    Text(
                        text = "No heroes found in your history",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFFFFC629))
            }
        },
        containerColor = Color.White
    )
}
