package com.superpets.mobile.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.superpets.mobile.data.auth.AuthManager
import com.superpets.mobile.data.auth.AuthState
import com.superpets.mobile.screens.editor.EditorScreen
import com.superpets.mobile.screens.editor.EditorViewModel
import com.superpets.mobile.screens.editor.HeroSelectionScreen
import com.superpets.mobile.screens.editor.GenerationProgressScreen
import com.superpets.mobile.screens.editor.ResultGalleryScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    rootNavController: NavController
) {
    val mainNavController = rememberNavController()
    val authManager: AuthManager = koinInject()
    val authState by authManager.authState.collectAsState()

    // Observe auth state and navigate to auth flow when user logs out
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            rootNavController.navigate(RootRoute.Auth) {
                popUpTo(RootRoute.Main) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = { BottomBar(navController = mainNavController) }
    ) { paddingValues ->
        BottomNavGraph(
            rootNavController = rootNavController,
            navController = mainNavController,
            paddingValues = paddingValues
        )
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val allRoutes = listOf<MainRoute>(
        MainRoute.Home,
        MainRoute.Create,
        MainRoute.History,
        MainRoute.Profile
    )

    BottomAppBar {
        allRoutes.forEach { route ->
            NavigationBarItem(
                label = { Text(text = route.title) },
                icon = { Icon(imageVector = route.icon, contentDescription = route.title) },
                selected = currentRoute == route.toString(),
                onClick = {
                    if (currentRoute != route.toString()) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavGraph(
    rootNavController: NavController,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Home,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable<MainRoute.Home>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            com.superpets.mobile.screens.home.HomeScreen(
                onNavigateToCreate = {
                    navController.navigate(MainRoute.Create)
                }
            )
        }

        composable<MainRoute.Create>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            // Get ViewModel scoped to this nav entry so it's shared with child screens
            val editorViewModel: EditorViewModel = koinViewModel()

            EditorScreen(
                viewModel = editorViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHeroSelection = {
                    navController.navigate(FeatureRoute.HeroSelection)
                },
                onNavigateToGeneration = { heroId, numOutputs, images ->
                    navController.navigate(FeatureRoute.GenerationProgress)
                }
            )
        }

        // Hero Selection Screen
        composable<FeatureRoute.HeroSelection>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            // Get the same ViewModel instance from the parent (Create screen)
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.Create)
            }
            val editorViewModel: EditorViewModel = koinViewModel(viewModelStoreOwner = parentEntry)

            HeroSelectionScreen(
                viewModel = editorViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onHeroSelected = { hero ->
                    // Hero is already updated in the shared ViewModel
                }
            )
        }

        // Generation Progress Screen
        composable<FeatureRoute.GenerationProgress>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            // Get the same ViewModel instance from the parent (Create screen)
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.Create)
            }
            val editorViewModel: EditorViewModel = koinViewModel(viewModelStoreOwner = parentEntry)

            GenerationProgressScreen(
                viewModel = editorViewModel,
                onCancel = {
                    navController.popBackStack()
                },
                onGenerationComplete = { imageUrls ->
                    // Navigate to ResultGallery to show generated images
                    navController.navigate(FeatureRoute.ResultGallery)
                }
            )
        }

        // Result Gallery Screen
        composable<FeatureRoute.ResultGallery>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            // Get the same ViewModel instance from the parent (Create screen)
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.Create)
            }
            val editorViewModel: EditorViewModel = koinViewModel(viewModelStoreOwner = parentEntry)
            val uiState by editorViewModel.uiState.collectAsState()

            ResultGalleryScreen(
                outputImages = uiState.generatedImageUrls ?: emptyList(),
                creditsCost = uiState.numOutputs,
                onDownload = { imageUrl ->
                    // TODO: Implement platform-specific download
                    // For now, this is a placeholder
                },
                onShare = { imageUrl ->
                    // TODO: Implement platform-specific share
                    // For now, this is a placeholder
                },
                onClose = {
                    // Go back to editor and reset everything
                    editorViewModel.reset()
                    navController.popBackStack(MainRoute.Create, inclusive = false)
                },
                onGenerateMore = {
                    // Go back to editor and keep current settings (clear only results)
                    editorViewModel.clearGenerationResults()
                    navController.popBackStack(MainRoute.Create, inclusive = false)
                },
                onRegenerate = {
                    // Go back to editor, clear results, and regenerate with same settings
                    editorViewModel.clearGenerationResults()
                    navController.popBackStack(MainRoute.Create, inclusive = false)
                    // The user can then press Generate again with the same settings
                }
            )
        }

        composable<MainRoute.History>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            com.superpets.mobile.screens.history.HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<MainRoute.Profile>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            com.superpets.mobile.screens.profile.ProfileScreen()
        }
    }
}

/**
 * Temporary placeholder screen for routes not yet implemented
 */
@Composable
private fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

