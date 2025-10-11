package com.superpets.mobile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Superpets Typography System
 *
 * Based on Be Vietnam Pro font family.
 * Font weights: 400 (Regular), 500 (Medium), 700 (Bold), 900 (Black)
 *
 * TODO: Add Be Vietnam Pro font files to resources and create FontFamily:
 * - Add font files to commonMain/resources/fonts/
 * - Create FontFamily with all weights
 * - Replace FontFamily.Default with custom font
 */

// TODO: Replace with custom Be Vietnam Pro font family
// val BeVietnamPro = FontFamily(
//     Font(resource = "fonts/be_vietnam_pro_regular.ttf", weight = FontWeight.Normal),
//     Font(resource = "fonts/be_vietnam_pro_medium.ttf", weight = FontWeight.Medium),
//     Font(resource = "fonts/be_vietnam_pro_bold.ttf", weight = FontWeight.Bold),
//     Font(resource = "fonts/be_vietnam_pro_black.ttf", weight = FontWeight.Black)
// )

private val defaultFontFamily = FontFamily.Default

val SuperpetsTypography = Typography(
    // Display Large - Hero headings (36sp, Black)
    displayLarge = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
        lineHeight = 43.2.sp,
        letterSpacing = 0.sp
    ),

    // Display Medium - Large headings (30sp, Black)
    displayMedium = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    // Display Small - Medium large headings (24sp, Black)
    displaySmall = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
        lineHeight = 31.2.sp,
        letterSpacing = 0.sp
    ),

    // Heading Large - Section headers (24sp, Bold)
    headlineLarge = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 31.2.sp,
        letterSpacing = 0.sp
    ),

    // Heading Medium - Subsection headers (20sp, Bold)
    headlineMedium = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),

    // Heading Small - Card titles (18sp, Bold)
    headlineSmall = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 25.2.sp,
        letterSpacing = 0.sp
    ),

    // Title Large - Large body text (18sp, Regular)
    titleLarge = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 27.sp,
        letterSpacing = 0.sp
    ),

    // Title Medium - Default body text (16sp, Regular)
    titleMedium = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),

    // Title Small - Small body text (14sp, Medium)
    titleSmall = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = 0.1.sp
    ),

    // Body Large - Large body (16sp, Regular)
    bodyLarge = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Body Medium - Default body (14sp, Regular)
    bodyMedium = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = 0.25.sp
    ),

    // Body Small - Small body (12sp, Regular)
    bodySmall = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 15.6.sp,
        letterSpacing = 0.4.sp
    ),

    // Label Large - Button text (16sp, Bold)
    labelLarge = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // Label Medium - Form labels (14sp, Medium)
    labelMedium = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = 0.5.sp
    ),

    // Label Small - Captions (12sp, Regular)
    labelSmall = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 15.6.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Additional custom text styles for specific use cases
 */

// Button Large - Large button text (18sp, Bold)
val ButtonLargeTextStyle = TextStyle(
    fontFamily = defaultFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 18.sp,
    letterSpacing = 0.sp
)

// Navigation Label - Bottom navigation labels (12sp, Medium)
val NavigationLabelTextStyle = TextStyle(
    fontFamily = defaultFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 15.6.sp,
    letterSpacing = 0.sp
)
