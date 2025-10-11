package com.superpets.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Superpets Light Color Scheme
 *
 * Material 3 color scheme for light mode based on Superpets design tokens.
 */
private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = Primary,
    onPrimary = TextOnPrimary,
    primaryContainer = Primary.copy(alpha = 0.2f),
    onPrimaryContainer = TextPrimaryLight,

    // Secondary colors (using primary with reduced opacity)
    secondary = Primary.copy(alpha = 0.7f),
    onSecondary = TextOnPrimary,
    secondaryContainer = Primary.copy(alpha = 0.1f),
    onSecondaryContainer = TextPrimaryLight,

    // Tertiary colors
    tertiary = Gray600,
    onTertiary = TextOnPrimary,
    tertiaryContainer = Gray100,
    onTertiaryContainer = TextPrimaryLight,

    // Background
    background = BackgroundLight,
    onBackground = TextPrimaryLight,

    // Surface
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,

    // Surface tints
    surfaceTint = Primary,

    // Inverse colors
    inverseSurface = Gray900,
    inverseOnSurface = Gray50,
    inversePrimary = Primary,

    // Error colors
    error = Error,
    onError = TextOnPrimary,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error,

    // Outline colors
    outline = BorderLight,
    outlineVariant = DividerLight,

    // Scrim
    scrim = ScrimLight
)

/**
 * Superpets Dark Color Scheme
 *
 * Material 3 color scheme for dark mode based on Superpets design tokens.
 */
private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = Primary,
    onPrimary = TextOnPrimary,
    primaryContainer = Primary.copy(alpha = 0.3f),
    onPrimaryContainer = TextPrimaryDark,

    // Secondary colors (using primary with reduced opacity)
    secondary = Primary.copy(alpha = 0.7f),
    onSecondary = TextOnPrimary,
    secondaryContainer = Primary.copy(alpha = 0.2f),
    onSecondaryContainer = TextPrimaryDark,

    // Tertiary colors
    tertiary = Gray400,
    onTertiary = TextOnPrimary,
    tertiaryContainer = Gray800,
    onTertiaryContainer = TextPrimaryDark,

    // Background
    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    // Surface
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,

    // Surface tints
    surfaceTint = Primary,

    // Inverse colors
    inverseSurface = Gray100,
    inverseOnSurface = Gray900,
    inversePrimary = Primary,

    // Error colors
    error = Error,
    onError = TextOnPrimary,
    errorContainer = Error.copy(alpha = 0.2f),
    onErrorContainer = Error,

    // Outline colors
    outline = BorderDark,
    outlineVariant = DividerDark,

    // Scrim
    scrim = ScrimDark
)

/**
 * Superpets Theme
 *
 * Main theme composable that wraps the app with custom Material 3 theming.
 * Supports both light and dark modes with custom colors, typography, shapes, and spacing.
 *
 * @param darkTheme Whether to use dark theme (defaults to system setting)
 * @param content The content to be themed
 */
@Composable
fun SuperpetsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val spacing = Spacing()

    CompositionLocalProvider(
        LocalSpacing provides spacing
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = SuperpetsTypography,
            shapes = SuperpetsShapes,
            content = content
        )
    }
}

/**
 * Extension property to access spacing throughout the app
 *
 * Usage:
 * ```
 * val spacing = MaterialTheme.spacing
 * Spacer(modifier = Modifier.height(spacing.medium))
 * ```
 */
val MaterialTheme.spacing: Spacing
    @Composable
    get() = LocalSpacing.current
