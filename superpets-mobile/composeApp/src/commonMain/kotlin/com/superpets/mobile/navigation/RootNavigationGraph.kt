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
import com.superpets.mobile.screens.auth.AuthViewModel
import com.superpets.mobile.screens.landing.LandingScreen
import com.superpets.mobile.screens.landing.LandingViewModel
import com.superpets.mobile.screens.splash.SplashViewModel
import com.superpets.mobile.screens.splash.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

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
                navigateToAuth = {
                    navController.navigate(RootRoute.Auth) {
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
            val landingViewModel: LandingViewModel = koinViewModel()
            val authViewModel: AuthViewModel = koinViewModel()

            LandingScreen(
                landingViewModel = landingViewModel,
                authViewModel = authViewModel,
                onOnboardingComplete = {
                    // After onboarding complete, navigate based on auth state
                    // The splash screen logic will handle this automatically via the settings change
                    navController.navigate(RootRoute.Auth) {
                        popUpTo(RootRoute.Landing) { inclusive = true }
                    }
                }
            )
        }

        composable<RootRoute.Auth> {
            AuthNavigationGraph(
                onAuthSuccess = {
                    navController.navigate(RootRoute.Main) {
                        popUpTo(RootRoute.Auth) { inclusive = true }
                    }
                }
            )
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