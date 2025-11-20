# App Icon Creation Guide

## Current Status
⚠️ **App icon needs to be created from the Superpets mascot**

## Source Asset
- **Location:** `stitch_superpets/landing/onboarding_screen/screen.png`
- **Element:** Blue cartoon character/mascot in top left corner
- **Style:** Friendly, cartoon superhero character

## Design Requirements

### Base Design (1024x1024px)
1. Extract the blue mascot from the landing screen
2. Create a square composition (1024x1024px)
3. Options:
   - **Option A:** Mascot centered on solid background (primary yellow #FAC638)
   - **Option B:** Mascot centered on gradient background (yellow to orange)
   - **Option C:** Mascot with "S" letterform background

### Recommended Approach
```
┌─────────────────┐
│                 │
│    ╔═══╗       │
│    ║ S ║       │  ← "S" for Superpets
│    ╚═══╝       │     (optional background)
│   [Mascot]     │  ← Blue character
│                 │
│   SUPERPETS    │  ← Text (optional)
│                 │
└─────────────────┘
```

## Platform-Specific Exports

### Android (Adaptive Icon)
Android uses adaptive icons with separate foreground and background layers.

#### Foreground Layer
- **Size:** 108x108dp
- **Safe zone:** 66x66dp (center circle)
- **Content:** Mascot character only
- **Format:** PNG with transparency
- **Export sizes:**
  - `mipmap-mdpi/ic_launcher_foreground.png` - 108x108px
  - `mipmap-hdpi/ic_launcher_foreground.png` - 162x162px
  - `mipmap-xhdpi/ic_launcher_foreground.png` - 216x216px
  - `mipmap-xxhdpi/ic_launcher_foreground.png` - 324x324px
  - `mipmap-xxxhdpi/ic_launcher_foreground.png` - 432x432px

#### Background Layer
- **Size:** 108x108dp
- **Content:** Solid color (#FAC638) or gradient
- **Format:** PNG or XML color
- **Export sizes:** Same as foreground

**Simple XML Background:**
```xml
<!-- mipmap-anydpi-v26/ic_launcher_background.xml -->
<?xml version="1.0" encoding="utf-8"?>
<color xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#FAC638"/>
```

#### Adaptive Icon Definitions
```xml
<!-- mipmap-anydpi-v26/ic_launcher.xml -->
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@mipmap/ic_launcher_foreground"/>
</adaptive-icon>

<!-- mipmap-anydpi-v26/ic_launcher_round.xml -->
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background"/>
    <foreground android:drawable="@mipmap/ic_launcher_foreground"/>
</adaptive-icon>
```

### iOS (Icon Set)
iOS requires a complete icon set with multiple sizes.

#### Required Sizes
Export from the 1024x1024px master:
- **1024x1024** - App Store
- **180x180** - iPhone (60pt @3x)
- **120x120** - iPhone (60pt @2x)
- **167x167** - iPad Pro (83.5pt @2x)
- **152x152** - iPad (76pt @2x)
- **76x76** - iPad (76pt @1x)
- **40x40** - iPad Notifications (20pt @2x)
- **29x29** - iPad Settings (29pt @1x)
- **58x58** - iPad Settings (29pt @2x)
- **87x87** - iPhone Settings (29pt @3x)

#### Location
Place in `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/`

#### Contents.json
Xcode generates this automatically when you drag icons into the asset catalog.

## Quick Start Options

### Option 1: Design Tool (Recommended)
1. Open `stitch_superpets/landing/onboarding_screen/screen.png` in Figma/Sketch/Photoshop
2. Select and export the blue mascot
3. Create 1024x1024 composition with mascot centered
4. Use Android Asset Studio to generate adaptive icon
   - https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html
5. Use Xcode to generate iOS icons
   - Drag 1024x1024 into AppIcon asset catalog

### Option 2: Online Tools
1. Extract mascot from screenshot
2. Use **App Icon Generator:**
   - https://www.appicon.co/
   - https://makeappicon.com/
   - Upload 1024x1024 source
   - Download all platform sizes

### Option 3: AI Generation
If mascot extraction is difficult:
1. Describe icon to AI image generator:
   - "Friendly blue cartoon dog superhero character with cape, square app icon, centered, yellow background, modern flat design"
2. Generate 1024x1024 image
3. Refine and use generation tools

## Temporary Placeholder
Until the real icon is ready, the template icon can remain in place.

To update placeholder icons:
1. Design simple 1024x1024 with "S" letter or text "Superpets"
2. Background: #FAC638 (primary yellow)
3. Text: #231E0F (dark) or #FFFFFF (white)
4. Generate all sizes using tools above

## Files to Create

### Android
```
composeApp/src/androidMain/res/
├── mipmap-anydpi-v26/
│   ├── ic_launcher.xml
│   ├── ic_launcher_round.xml
│   └── ic_launcher_background.xml (or color in values/colors.xml)
├── mipmap-mdpi/
│   ├── ic_launcher.png (48x48)
│   └── ic_launcher_foreground.png (108x108)
├── mipmap-hdpi/
│   ├── ic_launcher.png (72x72)
│   └── ic_launcher_foreground.png (162x162)
├── mipmap-xhdpi/
│   ├── ic_launcher.png (96x96)
│   └── ic_launcher_foreground.png (216x216)
├── mipmap-xxhdpi/
│   ├── ic_launcher.png (144x144)
│   └── ic_launcher_foreground.png (324x324)
└── mipmap-xxxhdpi/
    ├── ic_launcher.png (192x192)
    └── ic_launcher_foreground.png (432x432)
```

### iOS
```
iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/
├── icon-1024.png (1024x1024)
├── icon-20@2x.png (40x40)
├── icon-20@3x.png (60x60)
├── icon-29.png (29x29)
├── icon-29@2x.png (58x58)
├── icon-29@3x.png (87x87)
├── icon-40@2x.png (80x80)
├── icon-40@3x.png (120x120)
├── icon-60@2x.png (120x120)
├── icon-60@3x.png (180x180)
├── icon-76.png (76x76)
├── icon-76@2x.png (152x152)
├── icon-83.5@2x.png (167x167)
└── Contents.json
```

## Checklist

- [ ] Extract blue mascot from `stitch_superpets/landing/onboarding_screen/screen.png`
- [ ] Create 1024x1024 master app icon
- [ ] Generate Android adaptive icon (foreground + background)
- [ ] Export all Android mipmap densities
- [ ] Generate iOS icon set (all required sizes)
- [ ] Add icons to `androidMain/res/mipmap-*/`
- [ ] Add icons to `iosApp/Assets.xcassets/AppIcon.appiconset/`
- [ ] Test icon on Android device/emulator
- [ ] Test icon on iOS device/simulator
- [ ] Verify icon appears correctly in app launchers

## Resources

- **Android Icon Guidelines:** https://developer.android.com/google-play/resources/icon-design-specifications
- **iOS Icon Guidelines:** https://developer.apple.com/design/human-interface-guidelines/app-icons
- **Android Asset Studio:** https://romannurik.github.io/AndroidAssetStudio/
- **AppIcon.co:** https://www.appicon.co/
- **MakeAppIcon:** https://makeappicon.com/

---

**Status:** 📋 Documented - Awaiting asset extraction and icon creation
**Priority:** High - Required for Phase 5 (Platform Testing & Polish)
