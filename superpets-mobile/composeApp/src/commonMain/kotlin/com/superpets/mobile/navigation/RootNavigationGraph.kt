package com.superpets.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.superpets.mobile.screens.onboarding.OnboardingScreen
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
                    navController.navigate(RootRoute.Onboarding) {
                        popUpTo(RootRoute.Splash) { inclusive = true }
                    }
                },
                navigateToHome = {
                    navController.navigate(RootRoute.MainRoute) {
                        popUpTo(RootRoute.Splash) { inclusive = true }
                    }
                },
                viewModel = splashViewModel
            )
        }

        composable<RootRoute.Onboarding> {
            OnboardingScreen(
                navigateToHome = {
                    navController.navigate(RootRoute.MainRoute) {
                        popUpTo(RootRoute.Onboarding) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<RootRoute.MainRoute> {
            MainScreen(rootNavController = navController)
        }
    }
} 