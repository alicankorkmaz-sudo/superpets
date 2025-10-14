package com.superpets.mobile.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.superpets.mobile.ui.theme.CustomShapes
import com.superpets.mobile.ui.theme.spacing

/**
 * Primary button component matching Superpets design system.
 *
 * Features:
 * - Full width by default
 * - Rounded corners (fully rounded)
 * - Primary color background
 * - Optional loading state
 * - Optional icon
 *
 * @param text Button text
 * @param onClick Click handler
 * @param modifier Optional modifier
 * @param enabled Whether button is enabled
 * @param isLoading Whether to show loading indicator
 * @param icon Optional leading icon
 * @param fullWidth Whether button should fill max width
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    fullWidth: Boolean = true
) {
    val spacing = MaterialTheme.spacing

    Button(
        onClick = onClick,
        modifier = if (fullWidth) {
            modifier.fillMaxWidth().height(48.dp)
        } else {
            modifier.height(48.dp)
        },
        enabled = enabled && !isLoading,
        shape = CustomShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(
            horizontal = spacing.buttonPaddingHorizontal,
            vertical = spacing.buttonPaddingVertical
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(spacing.buttonGap))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Large primary button with bigger text and padding.
 * Used for prominent actions like "Get Started" on landing page.
 */
@Composable
fun LargePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null
) {
    val spacing = MaterialTheme.spacing

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled && !isLoading,
        shape = CustomShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = PaddingValues(
            horizontal = spacing.buttonPaddingHorizontalLarge,
            vertical = spacing.space4
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(spacing.buttonGap))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 18.sp
                )
            }
        }
    }
}

/**
 * Secondary button component matching Superpets design system.
 *
 * Features:
 * - Full width by default
 * - Rounded corners (fully rounded)
 * - Primary color with low opacity background
 * - Primary color text
 * - Optional icon
 *
 * @param text Button text
 * @param onClick Click handler
 * @param modifier Optional modifier
 * @param enabled Whether button is enabled
 * @param icon Optional leading icon
 * @param fullWidth Whether button should fill max width
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    fullWidth: Boolean = true
) {
    val spacing = MaterialTheme.spacing

    Button(
        onClick = onClick,
        modifier = if (fullWidth) {
            modifier.fillMaxWidth().height(48.dp)
        } else {
            modifier.height(48.dp)
        },
        enabled = enabled,
        shape = CustomShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        ),
        contentPadding = PaddingValues(
            horizontal = spacing.buttonPaddingHorizontal,
            vertical = spacing.buttonPaddingVertical
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(spacing.buttonGap))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/**
 * Outlined button for tertiary actions.
 * Uses primary color for border and text.
 */
@Composable
fun TertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    fullWidth: Boolean = true
) {
    val spacing = MaterialTheme.spacing

    OutlinedButton(
        onClick = onClick,
        modifier = if (fullWidth) {
            modifier.fillMaxWidth().height(48.dp)
        } else {
            modifier.height(48.dp)
        },
        enabled = enabled,
        shape = CustomShapes.button,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(
            horizontal = spacing.buttonPaddingHorizontal,
            vertical = spacing.buttonPaddingVertical
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(spacing.buttonGap))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/**
 * Text button for minimal actions (like "Forgot password?").
 */
@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = color
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

/**
 * Google sign-in button matching Stitch design.
 * White background with Google logo and text.
 */
@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val spacing = MaterialTheme.spacing

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        shape = CustomShapes.large,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        contentPadding = PaddingValues(
            horizontal = spacing.buttonPaddingHorizontal,
            vertical = spacing.buttonPaddingVertical
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Google icon placeholder (you'll need to add the actual Google logo asset)
            Text(text = "G", fontSize = 20.sp, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(spacing.space2))
            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/**
 * Apple sign-in button matching Stitch design.
 * Black background with Apple logo and white text.
 */
@Composable
fun AppleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val spacing = MaterialTheme.spacing

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = enabled,
        shape = CustomShapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(
            horizontal = spacing.buttonPaddingHorizontal,
            vertical = spacing.buttonPaddingVertical
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Apple icon placeholder (you'll need to add the actual Apple logo asset)
            Text(text = "", fontSize = 20.sp, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(spacing.space2))
            Text(
                text = "Continue with Apple",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
