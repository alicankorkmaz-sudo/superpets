package com.superpets.mobile.screens.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import coil3.compose.AsyncImage
import com.superpets.mobile.core.image.ImagePicker
import com.superpets.mobile.core.image.toImageBitmap
import com.superpets.mobile.data.models.Hero
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

/**
 * Editor Screen - Main image upload and hero selection screen
 * Follows Stitch design from stitch_superpets/image_upload/editor_screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHeroSelection: () -> Unit,
    onNavigateToGeneration: (heroId: String, numOutputs: Int, images: List<ByteArray>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Inject ImagePicker
    val imagePicker: ImagePicker = koinInject()

    // Camera launcher
    val cameraLauncher = imagePicker.rememberCameraLauncher { imageData ->
        viewModel.addImage(imageData)
    }

    // Gallery launcher (max 10 images)
    val galleryLauncher = imagePicker.rememberGalleryLauncher(maxImages = 10) { imageDataList ->
        viewModel.setImages(imageDataList)
    }

    val onCameraClick: () -> Unit = {
        cameraLauncher()
    }

    val onGalleryClick: () -> Unit = {
        galleryLauncher()
    }

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
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFDF8)) // Light beige background
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Upload your pet's photo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Image Preview or Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2D5F5D)), // Teal background
                contentAlignment = Alignment.Center
            ) {
                if (uiState.selectedImages.isEmpty()) {
                    Text(
                        text = "No image selected",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                } else {
                    // Display the first selected image
                    val firstImage = remember(uiState.selectedImages) {
                        uiState.selectedImages.firstOrNull()?.toImageBitmap()
                    }

                    if (firstImage != null) {
                        Image(
                            bitmap = firstImage,
                            contentDescription = "Selected image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Show image count badge if more than 1 image
                    if (uiState.selectedImages.size > 1) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFC629))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${uiState.selectedImages.size} images",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Camera and Gallery buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Camera Button
                OutlinedButton(
                    onClick = onCameraClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFFFFC629) // Yellow
                    ),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFC629))
                ) {
                    Text(
                        text = "📷 Camera",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Gallery Button
                Button(
                    onClick = onGalleryClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC629) // Yellow
                    )
                ) {
                    Text(
                        text = "🖼️ Gallery",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Image count hint
            Text(
                text = if (uiState.selectedImages.isEmpty()) {
                    "1-10 images allowed"
                } else {
                    "${uiState.selectedImages.size} of 10 images selected"
                },
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Selected Hero Section
            Text(
                text = "Selected Hero",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hero card or placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFF9E6)) // Light yellow background
                    .clickable { onNavigateToHeroSelection() }
                    .padding(16.dp)
            ) {
                val hero = uiState.selectedHero
                if (hero != null) {
                    SelectedHeroCard(hero = hero)
                } else {
                    Text(
                        text = "Tap to select a hero",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Change Hero Button
            TextButton(
                onClick = { onNavigateToHeroSelection() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Change Hero",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFC629) // Yellow
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Number of outputs
            Text(
                text = "Number of outputs",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Output slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Slider(
                    value = uiState.numOutputs.toFloat(),
                    onValueChange = { viewModel.updateNumOutputs(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFFC629),
                        activeTrackColor = Color(0xFFFFC629),
                        inactiveTrackColor = Color(0xFFFFE6A3)
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = uiState.numOutputs.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cost display
            Text(
                text = "Cost: ${uiState.numOutputs} credits",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Generate Button
            Button(
                onClick = {
                    viewModel.generateImages { response ->
                        onNavigateToGeneration(
                            uiState.selectedHero?.id ?: "",
                            uiState.numOutputs,
                            uiState.selectedImages
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC629),
                    disabledContainerColor = Color(0xFFFFE6A3)
                ),
                enabled = uiState.selectedImages.isNotEmpty() &&
                        uiState.selectedHero != null &&
                        !uiState.isGenerating &&
                        (uiState.userProfile?.credits ?: 0) >= uiState.numOutputs
            ) {
                if (uiState.isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = "Generate Images",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Error Snackbar
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Show error to user
                kotlinx.coroutines.delay(3000)
                viewModel.clearError()
            }
        }
    }
}

@Composable
private fun SelectedHeroCard(hero: Hero) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = hero.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = hero.identity,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Placeholder hero icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF2D5F5D)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "HERO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC629)
            )
        }
    }
}
