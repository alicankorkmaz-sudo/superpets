# Assets & Resources - Quick Reference

## 📁 Directory Structure Created

```
composeApp/src/
├── commonMain/composeResources/
│   ├── drawable/              ✅ Created - For vector drawables
│   ├── drawable-hdpi/         ✅ Created - For @1.5x bitmaps
│   ├── drawable-xhdpi/        ✅ Created - For @2x bitmaps
│   ├── drawable-xxhdpi/       ✅ Created - For @3x bitmaps
│   └── drawable-xxxhdpi/      ✅ Created - For @4x bitmaps
│
├── androidMain/res/
│   ├── mipmap-*/              ✅ Exists - For app icons
│   └── drawable/              ✅ Exists - For Android-specific assets
│
└── iosMain/
    └── (Resources)            📋 Managed via Xcode
```

## 📝 Documentation Files

1. **`ASSETS_GUIDE.md`** ✅
   - Complete asset inventory
   - Extraction workflow from Stitch
   - Directory structure
   - Usage examples
   - Material Icons reference

2. **`APP_ICON_TODO.md`** ✅
   - App icon creation guide
   - Platform-specific requirements (Android & iOS)
   - Export sizes and formats
   - Tool recommendations
   - Step-by-step checklist

## ✅ What's Ready

### Icons (via Material Icons)
All UI icons are available through Compose Material Icons:
- ✅ Navigation: Home, Add, History, Person
- ✅ Actions: Camera, Search, Download, Share, Refresh
- ✅ Controls: Check, Close, ArrowBack, Visibility
- ✅ Status: Error, Warning, Info

**Usage:**
```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
```

### Resource Directories
- ✅ Created drawable directories for all densities
- ✅ Android mipmap directories exist for app icons
- ✅ Structure ready for asset placement

### Documentation
- ✅ Asset inventory documented
- ✅ Extraction workflow defined
- ✅ Icon usage guidelines provided
- ✅ App icon creation guide ready

## ⚠️ What's Needed

### Manual Asset Extraction
Since Stitch outputs are HTML/screenshots, these need manual extraction:

1. **Superpets Logo/Mascot**
   - Location: `stitch_superpets/landing/onboarding_screen/screen.png`
   - Blue cartoon character in top left
   - Export at multiple densities
   - **Action:** Extract and save to `/drawable/` or `/drawable-*/`

2. **App Icon (1024x1024)**
   - Based on the mascot
   - Create square composition
   - Generate Android adaptive icon
   - Generate iOS icon set
   - **Action:** Follow `APP_ICON_TODO.md` guide

3. **Social Login Icons** (Optional)
   - Google: Download from Google branding guidelines
   - Apple: Extract from design or use Material Icons
   - **Action:** Download and add to `/drawable/`

## 🎨 Using Emojis as Placeholders

Currently using emojis for some icons:
- 💎 Diamond (credits)
- 📅 Calendar (free credits badge)

**Options:**
1. Keep emojis (works cross-platform, no assets needed)
2. Create custom vector drawables
3. Use Material Icons alternatives

## 📋 Quick Action Checklist

### Immediate (Before Screen Development)
- [x] Create resource directories
- [x] Document asset inventory
- [x] Identify required assets
- [ ] Extract Superpets logo from Stitch
- [ ] Create app icon (1024x1024)
- [ ] (Optional) Extract demo pet images

### Phase 5 (Polish)
- [ ] Generate Android adaptive icons
- [ ] Generate iOS icon set
- [ ] Create splash screen assets
- [ ] Export promotional graphics

## 🔗 Key Files

- **Asset Guide:** [`ASSETS_GUIDE.md`](./ASSETS_GUIDE.md)
- **App Icon Guide:** [`APP_ICON_TODO.md`](./APP_ICON_TODO.md)
- **Component Library:** [`COMPONENT_LIBRARY.md`](./COMPONENT_LIBRARY.md)
- **Design Tokens:** [`DESIGN_TOKENS.md`](./DESIGN_TOKENS.md)

## 💡 Tips

### Material Icons are Preferred
- No asset management needed
- Consistent with Material Design
- Available in multiple styles (Filled, Outlined, Rounded, Sharp, TwoTone)
- Just import and use

### Vector Drawables are Better
- Scalable without quality loss
- Smaller file size
- Theme-aware (tintable)
- XML format (text-based, version control friendly)

### Only Use Bitmaps When Necessary
- Complex illustrations
- Photographs
- Raster graphics that can't be vectorized
- Remember to export at all densities

---

**Status:** 📦 Asset structure ready, awaiting manual extraction from Stitch designs
