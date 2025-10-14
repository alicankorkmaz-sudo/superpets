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
 * Login screen
 *
 * Allows users to sign in with email and password.
 * Includes password reset functionality.
 */
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToSignup: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    val uiState by viewModel.loginUiState.collectAsState()
    val spacing = MaterialTheme.spacing
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showResetPasswordDialog by remember { mutableStateOf(false) }

    // Navigate when authenticated
    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onLoginSuccess()
        }
    }

    // Show error snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearLoginError()
        }
    }

    // Show success message
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearLoginError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
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
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(spacing.space2))

            Text(
                text = "Sign in to continue transforming your pets into superheroes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(spacing.space8))

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
                placeholder = "Enter your password",
                imeAction = ImeAction.Done,
                onImeAction = { viewModel.signIn(email, password) },
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(spacing.space2))

            // Forgot password link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    text = "Forgot Password?",
                    onClick = { showResetPasswordDialog = true },
                    enabled = !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(spacing.space6))

            // Sign in button
            PrimaryButton(
                text = "Sign In",
                onClick = { viewModel.signIn(email, password) },
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(spacing.space6))

            // Sign up link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(spacing.space1))
                TextButton(
                    text = "Sign Up",
                    onClick = onNavigateToSignup,
                    enabled = !uiState.isLoading
                )
            }

            Spacer(modifier = Modifier.height(spacing.space8))

            // Free credits badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(spacing.space2))
                Text(
                    text = "Get 5 free credits when you sign up!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(spacing.space8))
        }
    }

    // Password reset dialog
    if (showResetPasswordDialog) {
        ResetPasswordDialog(
            onDismiss = { showResetPasswordDialog = false },
            onResetPassword = { resetEmail ->
                viewModel.resetPassword(resetEmail)
                showResetPasswordDialog = false
            }
        )
    }
}

/**
 * Reset password dialog
 */
@Composable
private fun ResetPasswordDialog(
    onDismiss: () -> Unit,
    onResetPassword: (String) -> Unit
) {
    var resetEmail by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = "Enter your email address and we'll send you a link to reset your password.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                EmailTextField(
                    value = resetEmail,
                    onValueChange = { resetEmail = it },
                    placeholder = "Enter your email"
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = { onResetPassword(resetEmail) }
            ) {
                Text("Send Reset Link")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
