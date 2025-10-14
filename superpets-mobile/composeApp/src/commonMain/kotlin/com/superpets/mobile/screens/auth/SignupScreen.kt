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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.superpets.mobile.ui.components.buttons.GoogleSignInButton
import com.superpets.mobile.ui.components.buttons.AppleSignInButton
import com.superpets.mobile.ui.components.buttons.TextButton
import com.superpets.mobile.ui.components.input.EmailTextField
import com.superpets.mobile.ui.components.input.PasswordTextField
import com.superpets.mobile.ui.theme.spacing

/**
 * Signup screen matching Stitch design
 *
 * Allows users to create a new account with email/password or social login.
 * New users automatically receive 5 free credits.
 */
@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateBack: (() -> Unit)? = null,
    onSignupSuccess: () -> Unit
) {
    val authState by viewModel.authState.collectAsState()
    val uiState by viewModel.signupUiState.collectAsState()
    val spacing = MaterialTheme.spacing
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

    // Track if this is the first composition to clear stale state
    val isFirstComposition = remember { mutableStateOf(true) }

    // Clear any stale confirmation state when screen is first shown via navigation
    LaunchedEffect(Unit) {
        if (isFirstComposition.value) {
            // Clear stale confirmation state from previous navigation
            viewModel.clearConfirmationPending()
            isFirstComposition.value = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // Show confirmation pending screen if email confirmation is required
        if (uiState.confirmationPending) {
            EmailConfirmationPendingScreen(
                email = uiState.confirmationEmail ?: "",
                onBackToLogin = {
                    viewModel.clearConfirmationPending()
                    onNavigateToLogin()
                },
                onTryDifferentEmail = {
                    viewModel.clearConfirmationPending()
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Back button at top left
                if (onNavigateBack != null) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(spacing.space2)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Main content centered
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = spacing.screenPadding)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(spacing.space8))

                    // Heading
                    Text(
                        text = "Join Superpets",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(spacing.space2))

                    // Subheading
                    Text(
                        text = "Sign up or log in to continue",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(spacing.space8))

                    // Email field
                    EmailTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Email",
                        imeAction = ImeAction.Next,
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(spacing.space4))

                    // Password field
                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Password",
                        imeAction = ImeAction.Done,
                        onImeAction = { viewModel.signUp(email, password, password) },
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(spacing.space6))

                    // Sign up button
                    PrimaryButton(
                        text = "Sign Up",
                        onClick = { viewModel.signUp(email, password, password) },
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = uiState.isLoading,
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(spacing.space6))

                    // OR divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.space4)
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            text = "OR",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(spacing.space6))

                    // Google sign-in button
                    GoogleSignInButton(
                        onClick = { /* TODO: Implement Google sign-in */ },
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(spacing.space3))

                    // Apple sign-in button
                    AppleSignInButton(
                        onClick = { /* TODO: Implement Apple sign-in */ },
                        enabled = !uiState.isLoading
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
                            text = "Log in",
                            onClick = onNavigateToLogin,
                            enabled = !uiState.isLoading
                        )
                    }

                    Spacer(modifier = Modifier.height(spacing.space8))
                }
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
    onBackToLogin: () -> Unit,
    onTryDifferentEmail: () -> Unit
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

        // Try different email button
        PrimaryButton(
            text = "Try Different Email",
            onClick = onTryDifferentEmail,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(spacing.space3))

        // Back to login button
        com.superpets.mobile.ui.components.buttons.SecondaryButton(
            text = "Back to Login",
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(spacing.space8))
    }
}
