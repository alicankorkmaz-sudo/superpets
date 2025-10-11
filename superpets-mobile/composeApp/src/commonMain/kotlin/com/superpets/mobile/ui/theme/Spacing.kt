package com.superpets.mobile.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Superpets Spacing System
 *
 * Based on a 4dp grid system (Tailwind's default spacing scale).
 * All spacing values are multiples of 4dp for consistent visual rhythm.
 */
data class Spacing(
    val space0: Dp = 0.dp,
    val space0_5: Dp = 2.dp,
    val space1: Dp = 4.dp,
    val space2: Dp = 8.dp,
    val space3: Dp = 12.dp,
    val space4: Dp = 16.dp,
    val space5: Dp = 20.dp,
    val space6: Dp = 24.dp,
    val space8: Dp = 32.dp,
    val space10: Dp = 40.dp,
    val space12: Dp = 48.dp,
    val space16: Dp = 64.dp,
    val space20: Dp = 80.dp,
    val space24: Dp = 96.dp,
) {
    // Common spacing patterns with semantic names
    val minimal: Dp = space0_5
    val tiny: Dp = space1
    val extraSmall: Dp = space2
    val small: Dp = space3
    val medium: Dp = space4
    val mediumLarge: Dp = space5
    val large: Dp = space6
    val extraLarge: Dp = space8
    val huge: Dp = space10
    val massive: Dp = space12

    // Component-specific spacing
    val screenPadding: Dp = space4          // 16dp - Default screen padding
    val cardPadding: Dp = space4            // 16dp - Card internal padding
    val sectionGap: Dp = space6             // 24dp - Gap between sections
    val sectionGapLarge: Dp = space8        // 32dp - Large gap between sections
    val listItemSpacing: Dp = space3        // 12dp - Spacing between list items
    val listItemSpacingLarge: Dp = space4   // 16dp - Large spacing between list items

    // Button spacing
    val buttonPaddingVertical: Dp = space3      // 12dp
    val buttonPaddingHorizontal: Dp = space4    // 16dp
    val buttonPaddingHorizontalLarge: Dp = space5  // 20dp
    val buttonGap: Dp = space2                  // 8dp - Gap between button text and icon

    // Input spacing
    val inputPaddingVertical: Dp = space3       // 12dp
    val inputPaddingHorizontal: Dp = space4     // 16dp

    // Icon spacing
    val iconGap: Dp = space1                    // 4dp - Tight gap near icons
    val iconGapMedium: Dp = space2              // 8dp - Medium gap near icons

    // Navigation spacing
    val navBarPadding: Dp = space4              // 16dp
    val navBarHeight: Dp = 64.dp                // Bottom navigation bar height
    val topBarHeight: Dp = 56.dp                // Top app bar height
}

// Make spacing available throughout the app
val LocalSpacing = staticCompositionLocalOf { Spacing() }
