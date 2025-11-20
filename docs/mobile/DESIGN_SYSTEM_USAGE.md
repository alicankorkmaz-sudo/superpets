# Superpets Design System - Usage Guide

This guide shows how to use the Superpets design system in your Compose Multiplatform code.

## Overview

The design system has been implemented with the following files:

- `ui/theme/Color.kt` - All color definitions (primary, backgrounds, text, borders)
- `ui/theme/Typography.kt` - Typography scale and text styles
- `ui/theme/Spacing.kt` - Spacing constants and semantic spacing
- `ui/theme/Shape.kt` - Border radius values and shapes
- `ui/theme/Theme.kt` - Main theme setup with Material 3 integration

## Quick Start

The `SuperpetsTheme` composable wraps your app and provides all theme values:

```kotlin
@Composable
fun App() {
    SuperpetsTheme(darkTheme = isDarkTheme) {
        // Your app content
    }
}
```

## Using Colors

### Access Material Theme Colors

```kotlin
@Composable
fun MyComponent() {
    // Access colors from Material theme
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .background(backgroundColor)
    ) {
        Text(
            text = "Hello Superpets!",
            color = textColor
        )
    }
}
```

### Direct Color Access

For colors not in the Material theme:

```kotlin
import com.superpets.mobile.ui.theme.*

@Composable
fun HeroCard() {
    Card(
        modifier = Modifier
            .border(2.dp, Primary, CustomShapes.large)
    ) {
        // Card content
    }
}
```

## Using Typography

### Material Theme Typography

```kotlin
@Composable
fun MyScreen() {
    Column {
        // Display Large - Hero headings
        Text(
            text = "Turn Your Pet into a Superhero",
            style = MaterialTheme.typography.displayLarge
        )

        // Headline Large - Section headers
        Text(
            text = "Choose Your Hero",
            style = MaterialTheme.typography.headlineLarge
        )

        // Body Large - Body text
        Text(
            text = "AI-Powered • 29+ Heroes • Lightning Fast",
            style = MaterialTheme.typography.bodyLarge
        )

        // Label Large - Button text
        Button(onClick = { }) {
            Text(
                text = "Get Started",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
```

### Custom Text Styles

```kotlin
import com.superpets.mobile.ui.theme.ButtonLargeTextStyle

@Composable
fun LargeButton() {
    Button(onClick = { }) {
        Text(
            text = "Generate Images",
            style = ButtonLargeTextStyle
        )
    }
}
```

## Using Spacing

### Access Spacing

```kotlin
@Composable
fun MyLayout() {
    val spacing = MaterialTheme.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.screenPadding)
    ) {
        // Screen content with 16dp padding

        Spacer(modifier = Modifier.height(spacing.sectionGap))

        Card(
            modifier = Modifier.padding(spacing.cardPadding)
        ) {
            // Card content with 16dp padding
        }
    }
}
```

### Common Spacing Patterns

```kotlin
@Composable
fun SpacingExamples() {
    val spacing = MaterialTheme.spacing

    Column {
        // Screen padding (16dp)
        Column(modifier = Modifier.padding(spacing.screenPadding)) { }

        // Section gap (24dp)
        Spacer(modifier = Modifier.height(spacing.sectionGap))

        // Large section gap (32dp)
        Spacer(modifier = Modifier.height(spacing.sectionGapLarge))

        // Button with proper padding
        Button(
            onClick = { },
            contentPadding = PaddingValues(
                horizontal = spacing.buttonPaddingHorizontal,
                vertical = spacing.buttonPaddingVertical
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(spacing.buttonGap))
            Text("Create New")
        }

        // List items
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(spacing.listItemSpacing)
        ) {
            items(heroes) { hero ->
                HeroCard(hero)
            }
        }
    }
}
```

### Direct Spacing Access

```kotlin
import com.superpets.mobile.ui.theme.LocalSpacing

@Composable
fun DirectSpacingAccess() {
    val spacing = LocalSpacing.current

    // Use spacing values directly
    Box(modifier = Modifier.padding(spacing.medium))
}
```

## Using Shapes

### Material Theme Shapes

```kotlin
@Composable
fun ShapeExamples() {
    // Using Material theme shapes
    Card(
        shape = MaterialTheme.shapes.large
    ) {
        // Card content with 12dp rounded corners
    }

    OutlinedTextField(
        value = "",
        onValueChange = { },
        shape = MaterialTheme.shapes.medium
    )
}
```

### Custom Shapes

```kotlin
import com.superpets.mobile.ui.theme.CustomShapes

@Composable
fun CustomShapeExamples() {
    // Fully rounded button
    Button(
        onClick = { },
        shape = CustomShapes.button
    ) {
        Text("Get Started")
    }

    // Card with large radius
    Card(shape = CustomShapes.cardLarge) { }

    // Bottom sheet with only top corners rounded
    ModalBottomSheet(
        onDismissRequest = { },
        shape = CustomShapes.bottomSheet
    ) {
        // Bottom sheet content
    }

    // Badge with full rounding
    Surface(
        shape = CustomShapes.badge,
        color = MaterialTheme.colorScheme.primary
    ) {
        Text(
            text = "5 Free Credits",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
```

## Complete Component Examples

### Primary Button

```kotlin
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = CustomShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
```

### Hero Card

```kotlin
@Composable
fun HeroCard(
    heroName: String,
    imageUrl: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Card(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CustomShapes.large
                    )
                } else Modifier
            ),
        shape = CustomShapes.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = heroName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            // Hero name
            Text(
                text = heroName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(spacing.small)
            )

            // Selected indicator
            if (isSelected) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(spacing.space2)
                        .size(24.dp),
                    shape = CustomShapes.full,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}
```

### Screen Header

```kotlin
@Composable
fun ScreenHeader(
    title: String,
    credits: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = MaterialTheme.spacing

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(spacing.topBarHeight)
            .padding(horizontal = spacing.screenPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // Credit badge
        Surface(
            shape = CustomShapes.full,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = spacing.small,
                    vertical = spacing.space2
                ),
                horizontalArrangement = Arrangement.spacedBy(spacing.space1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("💎")
                Text(
                    text = "$credits credits",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
```

## Dark Mode Support

The theme automatically handles dark mode. All colors, surfaces, and text adapt to the selected theme:

```kotlin
@Composable
fun App() {
    // isDarkTheme comes from settings or system
    val isDarkTheme by settingsRepository.getThemeIsDark()
        .collectAsState(isSystemInDarkTheme())

    SuperpetsTheme(darkTheme = isDarkTheme) {
        // Your app automatically adapts to light/dark mode
    }
}
```

## Best Practices

1. **Always use theme values** instead of hardcoded colors, sizes, or spacing
2. **Use semantic spacing names** (e.g., `spacing.screenPadding` instead of `spacing.space4`)
3. **Prefer Material theme typography** styles over custom text styles
4. **Use custom shapes from `CustomShapes`** for component-specific needs
5. **Test both light and dark modes** to ensure proper contrast and readability

## Next Steps

Now that the design system is set up, you can:

1. Build reusable UI components (buttons, cards, inputs)
2. Convert Stitch designs to Compose code using these tokens
3. Ensure consistency across all screens
4. Add custom font files (Be Vietnam Pro) to resources

---

**See also:**
- `DESIGN_TOKENS.md` - Complete design token reference
- `stitch_superpets/` - Original Stitch designs
- `MOBILE_TODO.md` - Development roadmap
