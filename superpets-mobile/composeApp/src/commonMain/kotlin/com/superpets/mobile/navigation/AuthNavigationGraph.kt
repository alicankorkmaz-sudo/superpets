package com.superpets.mobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.superpets.mobile.screens.auth.AuthViewModel
import com.superpets.mobile.screens.auth.LoginScreen
import com.superpets.mobile.screens.auth.SignupScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Authentication navigation graph
 *
 * Handles navigation between Login and Signup screens.
 * When authentication is successful, navigates to the main app.
 */
@Composable
fun AuthNavigationGraph(
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: () -> Unit
) {
    val authViewModel: AuthViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = AuthRoute.Login,
    ) {
        composable<AuthRoute.Login> {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToSignup = {
                    navController.navigate(AuthRoute.Signup)
                },
                onLoginSuccess = onAuthSuccess
            )
        }

        composable<AuthRoute.Signup> {
            SignupScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSignupSuccess = onAuthSuccess
            )
        }
    }
}
