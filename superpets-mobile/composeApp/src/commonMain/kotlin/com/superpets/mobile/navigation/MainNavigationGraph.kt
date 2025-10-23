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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen(
    rootNavController: NavController
) {
    val mainNavController = rememberNavController()

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
        ) {
            // TODO: Implement CreateScreen (Phase 3)
            PlaceholderScreen(text = "Create Screen\n\nComing soon...")
        }

        composable<MainRoute.History>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            // TODO: Implement HistoryScreen (Phase 4)
            PlaceholderScreen(text = "History Screen\n\nComing soon...")
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

