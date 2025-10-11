package com.superpets.mobile.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.superpets.mobile.screens.detail.DetailScreen
import com.superpets.mobile.screens.list.ListScreen
import com.superpets.mobile.screens.profile.ProfileScreen

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
    val allRoutes = listOf<MainRoute>(MainRoute.Home, MainRoute.Profile)

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
        navigation<MainRoute.Home>(
            startDestination = HomeRoute.Master
        ) {
            composable<HomeRoute.Master> {
                ListScreen(
                    navigateToDetails = { objectId ->
                        navController.navigate(HomeRoute.Slave(objectId.toString()))
                    }
                )
            }
            composable<HomeRoute.Slave> { backStackEntry ->
                val details = backStackEntry.toRoute<HomeRoute.Slave>()
                DetailScreen(
                    objectId = details.objectId.toInt(),
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable<MainRoute.Profile>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            ProfileScreen(
                onLogout = {
                    rootNavController.navigate(RootRoute.Onboarding) {
                        popUpTo(RootRoute.MainRoute) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

