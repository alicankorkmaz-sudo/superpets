package com.superpets.mobile.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Superpets Shape System
 *
 * Defines border radius values for all UI components.
 * Uses Material 3's Shapes with our custom corner radius values.
 */
val SuperpetsShapes = Shapes(
    // Extra Small - Minimal rounding (2dp)
    extraSmall = RoundedCornerShape(2.dp),

    // Small - Small elements (4dp)
    small = RoundedCornerShape(4.dp),

    // Medium - Default rounding, inputs (8dp)
    medium = RoundedCornerShape(8.dp),

    // Large - Cards, images (12dp)
    large = RoundedCornerShape(12.dp),

    // Extra Large - Large cards (16dp)
    extraLarge = RoundedCornerShape(16.dp)
)

/**
 * Additional custom shapes for specific use cases
 */
object CustomShapes {
    // Radius values
    val radiusXS = 2.dp
    val radiusSM = 4.dp
    val radiusMD = 8.dp
    val radiusLG = 12.dp
    val radiusXL = 16.dp
    val radius2XL = 20.dp
    val radiusFull = 9999.dp

    // Predefined shapes
    val extraSmall = RoundedCornerShape(radiusXS)
    val small = RoundedCornerShape(radiusSM)
    val medium = RoundedCornerShape(radiusMD)
    val large = RoundedCornerShape(radiusLG)
    val extraLarge = RoundedCornerShape(radiusXL)
    val extraExtraLarge = RoundedCornerShape(radius2XL)
    val full = RoundedCornerShape(radiusFull)

    // Component-specific shapes
    val button = full  // Fully rounded buttons
    val buttonAlternate = large  // Alternative rounded buttons
    val card = large  // Cards
    val cardLarge = extraLarge  // Large cards
    val input = medium  // Input fields
    val image = large  // Image thumbnails
    val badge = full  // Badges, pills
    val avatar = full  // Avatar images

    // Bottom sheet with only top corners rounded
    val bottomSheet = RoundedCornerShape(
        topStart = radiusXL,
        topEnd = radiusXL,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    // Top sheet with only bottom corners rounded
    val topSheet = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = radiusXL,
        bottomEnd = radiusXL
    )
}
