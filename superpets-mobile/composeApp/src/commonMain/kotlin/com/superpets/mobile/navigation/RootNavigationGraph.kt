package com.superpets.mobile.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.superpets.mobile.screens.splash.SplashViewModel
import com.superpets.mobile.screens.splash.SplashScreen

@Composable
fun RootNavigationGraph(navController: NavHostController, splashViewModel: SplashViewModel) {
    NavHost(
        navController = navController,
        startDestination = RootRoute.Splash,
    ) {
        composable<RootRoute.Splash> {
            SplashScreen(
                navigateToOnboarding = {
                    navController.navigate(RootRoute.Landing) {
                        popUpTo(RootRoute.Splash) { inclusive = true }
                    }
                },
                navigateToHome = {
                    navController.navigate(RootRoute.Main) {
                        popUpTo(RootRoute.Splash) { inclusive = true }
                    }
                },
                viewModel = splashViewModel
            )
        }

        composable<RootRoute.Landing> {
            // TODO: Implement LandingScreen (Phase 2)
            PlaceholderScreen(text = "Landing Screen\n\nComing soon...")
        }

        composable<RootRoute.Auth> {
            // TODO: Implement AuthScreen (Phase 2)
            PlaceholderScreen(text = "Auth Screen\n\nComing soon...")
        }

        composable<RootRoute.Main> {
            MainScreen(rootNavController = navController)
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