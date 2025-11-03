package com.superpets.mobile.screens.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.superpets.mobile.data.models.Hero
import org.koin.compose.viewmodel.koinViewModel

/**
 * Hero Selection Screen
 * Displays heroes in a grid with Classic Heroes and Unique Heroes tabs
 * Follows Stitch design from stitch_superpets/hero_selection_screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroSelectionScreen(
    viewModel: EditorViewModel,
    onNavigateBack: () -> Unit,
    onHeroSelected: (Hero) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }

    // Filter heroes by category and search
    val classicHeroes = remember(uiState.heroes, searchQuery) {
        uiState.heroes
            .filter { it.category == "classics" }
            .filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val uniqueHeroes = remember(uiState.heroes, searchQuery) {
        uiState.heroes
            .filter { it.category == "uniques" }
            .filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val displayedHeroes = if (selectedTab == 0) classicHeroes else uniqueHeroes

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Choose your hero",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            // Continue button
            Button(
                onClick = {
                    uiState.selectedHero?.let { hero ->
                        onHeroSelected(hero)
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC629),
                    disabledContainerColor = Color(0xFFFFE6A3)
                ),
                enabled = uiState.selectedHero != null
            ) {
                Text(
                    text = "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFDF8)) // Light beige background
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                placeholder = {
                    Text(
                        text = "Search for a hero",
                        color = Color(0xFFFFC629) // Yellow
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFFFFC629)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color(0xFFFFC629),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Classic Heroes",
                            fontSize = 16.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) Color(0xFFFFC629) else Color.Gray
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Unique Heroes",
                            fontSize = 16.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) Color(0xFFFFC629) else Color.Gray
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Heroes Grid
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoadingHeroes -> {
                        CircularProgressIndicator(
                            color = Color(0xFFFFC629),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    uiState.error != null -> {
                        val errorMsg = uiState.error
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error loading heroes",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMsg ?: "Unknown error",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                    displayedHeroes.isEmpty() -> {
                        Text(
                            text = if (searchQuery.isEmpty()) {
                                "No heroes available.\nTotal heroes loaded: ${uiState.heroes.size}"
                            } else {
                                "No heroes found for \"$searchQuery\""
                            },
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp)
                        )
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(displayedHeroes) { hero ->
                                HeroCard(
                                    hero = hero,
                                    isSelected = uiState.selectedHero?.id == hero.id,
                                    onClick = { viewModel.selectHero(hero) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroCard(
    hero: Hero,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (hero.category == "classics") Color(0xFFFFF9E6) // Light yellow
                else Color(0xFF2D5F5D) // Teal
            )
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = if (isSelected) Color(0xFFFFC629) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // Selected checkmark
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color(0xFFFFC629),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(4.dp)
            )
        }

        // Hero name at bottom
        Text(
            text = hero.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (hero.category == "classics") Color.Black else Color.White,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}
