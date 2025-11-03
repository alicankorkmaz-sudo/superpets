package com.superpets.mobile.screens.editor

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Generation Progress Screen
 * Shows animated loading indicator while generating images
 * Follows Stitch design from stitch_superpets/generation_progress_screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerationProgressScreen(
    progress: Float = 0.5f,
    estimatedTimeRemaining: Int = 120, // in seconds
    onCancel: () -> Unit
) {
    val displayProgress = (progress * 100).toInt()
    val minutes = estimatedTimeRemaining / 60
    val seconds = estimatedTimeRemaining % 60

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Superpets",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancel"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFDF8)), // Light beige background
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Animated bubbles
            FloatingBubbles()

            Spacer(modifier = Modifier.height(60.dp))

            // Title
            Text(
                text = "Generating your Superpet",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Progress percentage
            Text(
                text = "$displayProgress%",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC629) // Yellow
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
                    .height(8.dp),
                color = Color(0xFFFFC629),
                trackColor = Color(0xFFFFE6A3),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estimated time
            Text(
                text = "Estimated time remaining: $minutes minutes",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Did you know section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Did you know?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Superpets uses advanced AI to create unique superhero versions of your pet, ensuring each image is a one-of-a-kind masterpiece.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Cancel button
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFFFC629)
                ),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFC629))
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Animated floating bubbles component
 * Recreates the purple bubble animation from the design
 */
@Composable
private fun FloatingBubbles() {
    val infiniteTransition = rememberInfiniteTransition()

    // Different animation values for each bubble to create organic movement
    val bubble1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val bubble2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val bubble3Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val purpleColor = Color(0xFF8B7FFF)

            // Large center bubble
            drawCircle(
                color = purpleColor.copy(alpha = 0.3f),
                radius = 60.dp.toPx(),
                center = Offset(centerX, centerY)
            )

            // Medium bubble (animated)
            val bubble1X = centerX + cos(Math.toRadians(bubble1Offset.toDouble())).toFloat() * 80.dp.toPx()
            val bubble1Y = centerY + sin(Math.toRadians(bubble1Offset.toDouble())).toFloat() * 50.dp.toPx()
            drawCircle(
                color = purpleColor.copy(alpha = 0.4f),
                radius = 40.dp.toPx(),
                center = Offset(bubble1X, bubble1Y)
            )

            // Small bubble 1 (animated)
            val bubble2X = centerX + cos(Math.toRadians(bubble2Offset.toDouble() + 90)).toFloat() * 60.dp.toPx()
            val bubble2Y = centerY + sin(Math.toRadians(bubble2Offset.toDouble() + 90)).toFloat() * 40.dp.toPx()
            drawCircle(
                color = purpleColor.copy(alpha = 0.5f),
                radius = 25.dp.toPx(),
                center = Offset(bubble2X, bubble2Y)
            )

            // Small bubble 2 (animated)
            val bubble3X = centerX + cos(Math.toRadians(bubble3Offset.toDouble() + 180)).toFloat() * 70.dp.toPx()
            val bubble3Y = centerY + sin(Math.toRadians(bubble3Offset.toDouble() + 180)).toFloat() * 45.dp.toPx()
            drawCircle(
                color = purpleColor.copy(alpha = 0.6f),
                radius = 30.dp.toPx(),
                center = Offset(bubble3X, bubble3Y)
            )

            // Tiny bubble (animated)
            val bubble4X = centerX + cos(Math.toRadians(bubble1Offset.toDouble() + 270)).toFloat() * 90.dp.toPx()
            val bubble4Y = centerY - 80.dp.toPx()
            drawCircle(
                color = purpleColor.copy(alpha = 0.7f),
                radius = 20.dp.toPx(),
                center = Offset(bubble4X, bubble4Y)
            )
        }
    }
}
