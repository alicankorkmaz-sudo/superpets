package com.superpets.mobile.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.superpets.mobile.data.auth.AuthState
import com.superpets.mobile.ui.components.buttons.PrimaryButton
import com.superpets.mobile.ui.components.buttons.TextButton
import com.superpets.mobile.ui.components.input.EmailTextField
import com.superpets.mobile.ui.components.input.PasswordTextField
import com.superpets.mobile.ui.theme.spacing

/**
 * Signup screen
 *
 * Allows users to create a new account with email and password.
 * New users automatically receive 5 free credits.
 */
@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    val uiState by viewModel.signupUiState.collectAsState()
    val spacing = MaterialTheme.spacing
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Navigate when authenticated
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onSignupSuccess()
        }
    }

    // Show error snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSignupError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // Show confirmation pending screen if email confirmation is required
        if (uiState.confirmationPending) {
            EmailConfirmationPendingScreen(
                email = uiState.confirmationEmail ?: "",
                onBackToLogin = onNavigateToLogin
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = spacing.screenPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
            Spacer(modifier = Modifier.height(spacing.space8))

            // Logo/App Icon (placeholder)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SP",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(spacing.space6))

            // Welcome text
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(spacing.space2))

            Text(
                text = "Join Superpets and start transforming your pets into superheroes!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.space8))

            // Free credits badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(spacing.space2))
                Text(
                    text = "Get 5 Free Credits!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(spacing.space6))

            // Email field
            EmailTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter your email",
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(spacing.space4))

            // Password field
            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Create a password (min 6 characters)",
                imeAction = ImeAction.Next,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(spacing.space4))

            // Confirm password field
            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = "Confirm Password",
                placeholder = "Re-enter your password",
                imeAction = ImeAction.Done,
                onImeAction = { viewModel.signUp(email, password, confirmPassword) },
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(spacing.space6))

            // Sign up button
            PrimaryButton(
                text = "Create Account",
                onClick = { viewModel.signUp(email, password, confirmPassword) },
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(spacing.space4))

            // Terms and privacy text
            Text(
                text = "By signing up, you agree to our Terms of Service and Privacy Policy",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacing.space4)
            )

            Spacer(modifier = Modifier.height(spacing.space6))

            // Login link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(spacing.space1))
                TextButton(
                    text = "Sign In",
                    onClick = onNavigateToLogin,
                    enabled = !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(spacing.space8))

            // Features list
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.space3)
            ) {
                FeatureItem(
                    text = "29+ unique superhero transformations",
                    icon = Icons.Default.Star
                )
                FeatureItem(
                    text = "AI-powered image editing with Google's Nano Banana",
                    icon = Icons.Default.Star
                )
                FeatureItem(
                    text = "Lightning-fast results in seconds",
                    icon = Icons.Default.Star
                )
            }

            Spacer(modifier = Modifier.height(spacing.space8))
            }
        }
    }
}

/**
 * Email confirmation pending screen
 */
@Composable
private fun EmailConfirmationPendingScreen(
    email: String,
    onBackToLogin: () -> Unit
) {
    val spacing = MaterialTheme.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spacing.screenPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(spacing.space8))

        // Email icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(spacing.space6))

        // Title
        Text(
            text = "Check Your Email",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(spacing.space4))

        // Email address
        Text(
            text = "We've sent a confirmation link to",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(spacing.space2))

        Text(
            text = email,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(spacing.space6))

        // Instructions
        Text(
            text = "Please click the link in the email to verify your account and complete the signup process.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(spacing.space6))

        // Important notice
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(spacing.space4)
        ) {
            Column {
                Text(
                    text = "Important",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(spacing.space2))
                Text(
                    text = "The confirmation link will open this app automatically. Make sure to click it on this device.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing.space8))

        // Back to login button
        PrimaryButton(
            text = "Back to Login",
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(spacing.space8))
    }
}

/**
 * Feature item with icon and text
 */
@Composable
private fun FeatureItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.space2))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
