# Superpets Mobile Assets Guide

This document outlines all visual assets used in the Superpets mobile app, their locations, and how to manage them.

## Asset Inventory

### 1. App Icon & Branding

#### **Superpets Logo/Mascot**
- **Location in design:** Top left of landing screen
- **Description:** Blue cartoon character/mascot
- **Status:** ⚠️ **Needs extraction from Stitch**
- **Usage:** App logo, splash screen, branding
- **Export requirements:**
  - **Android:** Multiple densities (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)
  - **iOS:** @1x, @2x, @3x
  - **Format:** PNG with transparency
  - **Sizes:**
    - Icon: 40dp/40pt base size
    - App Icon: 1024x1024px

#### **App Icon**
- **Status:** ⚠️ **Needs creation**
- **Description:** Square icon based on the mascot
- **Export requirements:**
  - **Android Adaptive Icon:**
    - Foreground: 108x108dp
    - Background: 108x108dp
    - Safe zone: 66x66dp (center)
  - **iOS Icon Set:**
    - 1024x1024px (App Store)
    - Multiple sizes for different devices (handled by Xcode)
  - **Location:**
    - Android: `/composeApp/src/androidMain/res/mipmap-*/ic_launcher.png`
    - iOS: `iosApp/Assets.xcassets/AppIcon.appiconset/`

---

### 2. Icons (Using Material Icons)

Most UI icons are available through Material Icons (already included in Compose). Use these instead of custom assets:

#### **Navigation Icons**
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

Icons.Default.Home          // Home navigation
Icons.Default.Add           // Create/Add
Icons.Default.History       // History/Clock
Icons.Default.Person        // Profile
Icons.Default.ArrowBack     // Back navigation
Icons.Default.Close         // Close/X
```

#### **Action Icons**
```kotlin
Icons.Default.CameraAlt     // Camera/Photo
Icons.Default.Search        // Search
Icons.Default.Check         // Checkmark
Icons.Default.Clear         // Clear/Remove
Icons.Default.Visibility    // Show password
Icons.Default.VisibilityOff // Hide password
Icons.Default.Download      // Download
Icons.Default.Share         // Share
Icons.Default.Refresh       // Regenerate/Refresh
```

#### **Status Icons**
```kotlin
Icons.Default.Error         // Error state
Icons.Default.Warning       // Warning
Icons.Default.Info          // Information
```

---

### 3. Custom Icons & Assets

#### **Diamond/Gem Icon** (Credits)
- **Location in design:** Credit badge, top right
- **Current implementation:** Using emoji "💎" as placeholder
- **Status:** ⚠️ **Needs custom vector drawable**
- **Recommended:** Create vector drawable or use emoji
- **Usage:** Credit badge, pricing screen

**Option 1: Continue using emoji**
```kotlin
Text(text = "💎")
```

**Option 2: Create custom vector drawable**
```xml
<!-- drawable/ic_diamond.xml -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#FAC638"
        android:pathData="M12,2 L16,8 L20,8 L12,22 L8,8 Z"/>
</vector>
```

#### **Calendar Icon** (Free Credits Badge)
- **Location in design:** "5 Free Credits" badge
- **Current implementation:** Using emoji "📅" as placeholder
- **Alternative:** `Icons.Default.Event` or `Icons.Default.CalendarMonth`

#### **Social Login Icons**
- **Google Icon:**
  - Use Google branding guidelines
  - Download from: https://developers.google.com/identity/branding-guidelines
  - **Location:** `/composeApp/src/commonMain/composeResources/drawable/ic_google.png`

- **Apple Icon:**
  - Use Apple branding guidelines
  - Built-in in design: Black Apple logo
  - **Location:** `/composeApp/src/commonMain/composeResources/drawable/ic_apple.png`

---

### 4. Sample Images (Hero Pets)

#### **Example Pet Images** (from design screens)
- **Location in design:** Landing screen, dashboard, result gallery
- **Status:** ✅ These are example/demo images from Stitch
- **Usage:** Can be used as placeholders during development
- **Production:** Will be replaced with user-generated content

**Demo Images to Extract (optional):**
1. Dog with cape (landing screen)
2. Superhero pets (dashboard grid)
3. Hero selection examples

---

## Directory Structure

```
superpets-mobile/
├── composeApp/src/
│   ├── commonMain/
│   │   └── composeResources/
│   │       ├── drawable/              # Vector drawables (SVG/XML)
│   │       │   ├── ic_diamond.xml
│   │       │   ├── ic_logo.xml
│   │       │   └── ...
│   │       ├── drawable-mdpi/         # Bitmap assets @1x
│   │       ├── drawable-hdpi/         # Bitmap assets @1.5x
│   │       ├── drawable-xhdpi/        # Bitmap assets @2x
│   │       ├── drawable-xxhdpi/       # Bitmap assets @3x
│   │       └── drawable-xxxhdpi/      # Bitmap assets @4x
│   │
│   ├── androidMain/
│   │   └── res/
│   │       ├── mipmap-anydpi-v26/     # Adaptive icons
│   │       │   ├── ic_launcher.xml
│   │       │   └── ic_launcher_round.xml
│   │       ├── mipmap-mdpi/           # App icons @1x
│   │       ├── mipmap-hdpi/           # App icons @1.5x
│   │       ├── mipmap-xhdpi/          # App icons @2x
│   │       ├── mipmap-xxhdpi/         # App icons @3x
│   │       └── mipmap-xxxhdpi/        # App icons @4x
│   │
│   └── iosMain/
│       └── (iOS-specific resources)
│
└── iosApp/
    └── Assets.xcassets/
        └── AppIcon.appiconset/        # iOS app icons
```

---

## Asset Extraction Workflow

### From Stitch Designs

Since Stitch outputs are HTML/CSS with embedded images, assets need to be manually extracted:

#### **Method 1: Browser DevTools**
1. Open `stitch_superpets/*/screen.png` files
2. Identify assets (logo, icons, images)
3. Use browser DevTools to inspect and download images
4. Or screenshot and crop individual assets

#### **Method 2: Direct File Extraction**
1. Open HTML files in browser
2. Right-click images → Save Image As
3. Or inspect HTML `<img>` tags for data URLs
4. Decode base64 images if needed

#### **Method 3: Design Tool**
1. Open screen.png files in design tool (Figma, Sketch, etc.)
2. Select and export individual assets
3. Export at multiple resolutions (@1x, @2x, @3x)

---

## Required Actions

### Immediate (Phase 0)
- [ ] Extract Superpets logo/mascot from landing screen
  - Save as `ic_logo.png` at multiple densities
  - Or create vector drawable `ic_logo.xml`
- [ ] Create app icon (1024x1024) based on mascot
  - Design square version
  - Export for Android adaptive icon
  - Export for iOS icon set
- [ ] Download Google/Apple social login icons
  - Follow branding guidelines
  - Save to drawable resources

### Optional (Can use Material Icons)
- [ ] Create custom diamond/gem icon for credits
- [ ] Extract example pet images for development placeholders

### Future (Phase 5)
- [ ] Create splash screen assets
- [ ] Design promotional images (1024x500 for Play Store)
- [ ] Create feature graphics for app stores

---

## Asset Usage in Code

### Logo
```kotlin
// In SuperpetsHeader or landing screen
Image(
    painter = painterResource("drawable/ic_logo.png"),
    contentDescription = "Superpets Logo",
    modifier = Modifier.size(40.dp)
)
```

### Material Icons
```kotlin
Icon(
    imageVector = Icons.Default.Home,
    contentDescription = "Home",
    tint = MaterialTheme.colorScheme.primary
)
```

### Custom Icons
```kotlin
Icon(
    painter = painterResource("drawable/ic_diamond.xml"),
    contentDescription = "Credits",
    tint = MaterialTheme.colorScheme.primary
)
```

---

## Icon Design Guidelines

### Size & Format
- **Vector (XML):** Preferred for icons, logos
  - 24dp standard size
  - Viewbox: 24x24
  - Color: Use theme colors, not hardcoded

- **Raster (PNG):** For photos, complex graphics
  - Base size: @1x density
  - Export: mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi
  - Format: PNG-24 with alpha transparency

### Colors
- **Primary:** #FAC638 (golden yellow)
- **Surface:** Adapt to light/dark theme
- **Use tint parameter** instead of hardcoding colors in assets

### Naming Convention
- `ic_` prefix for icons
- `img_` prefix for images
- `bg_` prefix for backgrounds
- Lowercase, underscore-separated
- Example: `ic_diamond.xml`, `ic_logo.png`

---

## Material Icons Reference

Complete list: https://fonts.google.com/icons

Commonly used in Superpets:
- Navigation: `Home`, `Add`, `History`, `Person`
- Actions: `CameraAlt`, `Search`, `Download`, `Share`
- Controls: `Check`, `Close`, `ArrowBack`, `Visibility`
- Status: `Error`, `Warning`, `Info`

All available via:
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
```

---

## Next Steps

1. ✅ Create resource directories (done)
2. ⚠️ Extract logo/mascot from Stitch designs
3. ⚠️ Create app icon (1024x1024)
4. ⚠️ Download social login icons
5. ✅ Use Material Icons for UI (already available)
6. ⏭️ Export splash screen assets (Phase 5)

---

**Last Updated:** October 11, 2025
**Status:** Asset structure created, awaiting asset extraction from Stitch
