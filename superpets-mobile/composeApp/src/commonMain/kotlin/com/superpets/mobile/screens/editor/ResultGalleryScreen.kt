package com.superpets.mobile.screens.editor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

/**
 * Result Gallery Screen
 * Shows generated images in a swipeable gallery with download/share/regenerate options
 * Follows Stitch design from stitch_superpets/result_gallery_screen
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultGalleryScreen(
    outputImages: List<String>,
    creditsCost: Int = 5,
    saveToHistory: Boolean = true,
    onSaveToHistoryChanged: (Boolean) -> Unit = {},
    onDownload: (String) -> Unit = {},
    onShare: (String) -> Unit = {},
    onRegenerate: () -> Unit = {},
    onGenerateMore: () -> Unit = {},
    onClose: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { outputImages.size })
    val currentPage = pagerState.currentPage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF8)) // Light beige background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Close button
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }

            // Image pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = outputImages[page],
                        contentDescription = "Generated superpet image ${page + 1}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Page indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(outputImages.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentPage) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == currentPage) Color(0xFFFFC629) // Yellow
                                else Color(0xFFFFE6A3) // Light yellow
                            )
                    )
                    if (index < outputImages.size - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action buttons
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF9E6) // Light yellow
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Download
                    ActionButton(
                        emoji = "⬇️",
                        label = "Download",
                        onClick = { onDownload(outputImages[currentPage]) }
                    )

                    // Share
                    ActionButton(
                        icon = Icons.Default.Share,
                        label = "Share",
                        onClick = { onShare(outputImages[currentPage]) }
                    )

                    // Regenerate
                    ActionButton(
                        icon = Icons.Default.Refresh,
                        label = "Regenerate",
                        onClick = onRegenerate
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save to history toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Save to History",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Switch(
                    checked = saveToHistory,
                    onCheckedChange = onSaveToHistoryChanged,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFFC629),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Credits used
            Text(
                text = "~$creditsCost credits used",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Generate More button
            Button(
                onClick = onGenerateMore,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC629)
                )
            ) {
                Text(
                    text = "Generate More",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    emoji: String? = null,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFFFFC629),
                modifier = Modifier.size(32.dp)
            )
        } else if (emoji != null) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFFFC629)
        )
    }
}
