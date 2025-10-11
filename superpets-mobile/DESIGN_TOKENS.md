# Superpets Mobile Design Tokens

This document contains all design tokens extracted from the Stitch design output. These tokens will form the foundation of our design system in Compose Multiplatform.

## Color Palette

### Brand Colors

| Token Name | Hex Value | Usage |
|------------|-----------|-------|
| Primary | `#FAC638` | Primary brand color, buttons, highlights, active states |
| Primary Gradient Start | `#FFD700` | Gold gradient for special badges |
| Primary Gradient End | `#F7971E` | Orange gradient for special badges |

### Background Colors

| Token Name | Hex Value | Usage |
|------------|-----------|-------|
| Background Light | `#f8f8f5` | Light mode background (off-white/cream) |
| Background Dark | `#231e0f` | Dark mode background (dark brown/black) |

### Surface Colors

| Light Mode | Dark Mode | Usage |
|------------|-----------|-------|
| `#FFFFFF` (white) | `#1e293b` (slate-800) | Cards, elevated surfaces |
| `#f1f5f9` (gray-100) | `#0f172a` (slate-900) | Secondary surfaces |

### Text Colors

| Token Name | Light Mode | Dark Mode | Usage |
|------------|------------|-----------|-------|
| Text Primary | `#1f2937` (gray-800) | `#f3f4f6` (gray-100) | Primary text, headings |
| Text Secondary | `#6b7280` (gray-500) | `#9ca3af` (gray-400) | Secondary text, labels |
| Text Tertiary | `#9ca3af` (gray-400) | `#6b7280` (gray-500) | Tertiary text, placeholders |
| Text On Primary | `#FFFFFF` | `#FFFFFF` | Text on primary color buttons |

### Border Colors

| Light Mode | Dark Mode | Usage |
|------------|-----------|-------|
| `#e5e7eb` (gray-200) | `#374151` (gray-700) | Default borders |
| `#FAC638` with 20% opacity | `#FAC638` with 30% opacity | Primary borders |
| `#d1d5db` (gray-300) | `#4b5563` (gray-600) | Dividers |

### Primary Color Variations (Opacity-based)

| Opacity | Usage |
|---------|-------|
| primary/10 | Very light backgrounds |
| primary/20 | Light backgrounds, secondary buttons |
| primary/30 | Shadows, subtle highlights |
| primary/40 | Disabled states |
| primary/50 | Hover states |
| primary/60 | Inactive tabs |

## Typography

### Font Family

**Primary Font:** Be Vietnam Pro

**Font Weights Available:**
- Regular: 400
- Medium: 500
- Bold: 700
- Black/Extra Bold: 900

### Type Scale

| Token Name | Size (px) | Size (sp/dp) | Line Height | Font Weight | Usage |
|------------|-----------|--------------|-------------|-------------|-------|
| Display Large | 36px | 36sp | 1.2 (43.2px) | 900 (Black) | Hero headings |
| Display Medium | 30px | 30sp | 1.2 (36px) | 900 (Black) | Large headings |
| Heading 1 | 24px | 24sp | 1.3 (31.2px) | 700 (Bold) | Section headers |
| Heading 2 | 20px | 20sp | 1.3 (26px) | 700 (Bold) | Subsection headers |
| Heading 3 | 18px | 18sp | 1.4 (25.2px) | 700 (Bold) | Card titles |
| Body Large | 18px | 18sp | 1.5 (27px) | 400 (Regular) | Large body text |
| Body Medium | 16px | 16sp | 1.5 (24px) | 400 (Regular) | Default body text |
| Body Small | 14px | 14sp | 1.4 (19.6px) | 400 (Regular) | Secondary text |
| Label | 14px | 14sp | 1.4 (19.6px) | 500 (Medium) | Form labels |
| Caption | 12px | 12sp | 1.3 (15.6px) | 400 (Regular) | Captions, metadata |
| Button | 16px | 16sp | 1.0 (16px) | 700 (Bold) | Button text |
| Button Large | 18px | 18sp | 1.0 (18px) | 700 (Bold) | Large button text |

## Spacing System

Based on 4dp/4px grid system (Tailwind's default spacing scale).

| Token Name | Value (dp) | Value (px) | Usage |
|------------|------------|------------|-------|
| Space 0.5 | 2dp | 2px | Minimal spacing |
| Space 1 | 4dp | 4px | Tight spacing, icon gaps |
| Space 2 | 8dp | 8px | Small spacing, compact layouts |
| Space 3 | 12dp | 12px | Medium-small spacing |
| Space 4 | 16dp | 16px | Default spacing, card padding |
| Space 5 | 20dp | 20px | Medium spacing |
| Space 6 | 24dp | 24px | Large spacing, section gaps |
| Space 8 | 32dp | 32px | Extra large spacing |
| Space 10 | 40dp | 40px | Hero spacing |
| Space 12 | 48dp | 48px | Major section spacing |
| Space 16 | 64dp | 64px | Screen padding |

### Common Spacing Patterns

- **Screen padding:** 16dp (Space 4)
- **Card padding:** 16dp (Space 4)
- **Section gaps:** 24dp (Space 6) or 32dp (Space 8)
- **List item spacing:** 12dp (Space 3) or 16dp (Space 4)
- **Button padding:** Vertical 12dp (Space 3), Horizontal 16-20dp (Space 4-5)
- **Input padding:** 12-16dp (Space 3-4)

## Border Radius

| Token Name | Value (dp) | Value (px) | Usage |
|------------|------------|------------|-------|
| Radius XS | 2dp | 2px | Minimal rounding |
| Radius SM | 4dp | 4px | Small elements |
| Radius MD | 8dp | 8px | Default rounding, inputs |
| Radius LG | 12dp | 12px | Cards, images |
| Radius XL | 16dp | 16px | Large cards, hero cards |
| Radius 2XL | 20dp | 20px | Extra large cards |
| Radius Full | 9999dp | 9999px | Fully rounded (pills, circles) |

### Common Component Radii

- **Buttons (primary):** Radius Full or Radius LG
- **Buttons (secondary):** Radius LG
- **Input fields:** Radius MD
- **Cards:** Radius LG or Radius XL
- **Images/Thumbnails:** Radius LG
- **Bottom sheets:** Radius XL (top corners only)
- **Badges:** Radius Full
- **Avatar:** Radius Full

## Elevation & Shadows

### Elevation Levels

| Level | Usage | Shadow Definition |
|-------|-------|-------------------|
| 0 | Flat elements | No shadow |
| 1 | Slightly raised elements | `0 1px 3px rgba(0,0,0,0.12), 0 1px 2px rgba(0,0,0,0.24)` |
| 2 | Default cards | `0 3px 6px rgba(0,0,0,0.15), 0 2px 4px rgba(0,0,0,0.12)` |
| 3 | Raised cards, buttons | `0 10px 20px rgba(0,0,0,0.15), 0 3px 6px rgba(0,0,0,0.10)` |
| 4 | Floating elements | `0 15px 25px rgba(0,0,0,0.15), 0 5px 10px rgba(0,0,0,0.05)` |
| 5 | Modals, dialogs | `0 20px 40px rgba(0,0,0,0.30)` |

### Special Shadows

- **Primary Shadow:** `0 4px 15px rgba(250, 198, 56, 0.3)` (used with primary buttons)
- **Badge Shadow:** `0 4px 15px rgba(247, 151, 30, 0.4)` (for super badge gradient)
- **Image Overlay Shadow:** `0 10px 30px rgba(0,0,0,0.2)` (for hero images)

## Gradients

### Brand Gradients

**Super Badge Gradient:**
```
linearGradient(135deg, #FFD700 0%, #F7971E 100%)
```

**Image Overlay Gradient (Dark):**
```
linearGradient(0deg, rgba(0,0,0,0.6) 0%, transparent 100%)
```

**Image Overlay Gradient (Subtle):**
```
linearGradient(0deg, rgba(0,0,0,0.4) 0%, transparent 25%)
```

## Effects

### Backdrop Blur

| Level | Blur Amount | Usage |
|-------|-------------|-------|
| Small | 8px | Sticky headers, navigation bars |
| Medium | 12px | Overlay elements |
| Large | 20px | Modals, bottom sheets |

### Opacity Levels

| Token Name | Value | Usage |
|------------|-------|-------|
| Opacity Full | 1.0 (100%) | Default |
| Opacity High | 0.9 (90%) | Slightly transparent |
| Opacity Medium | 0.6-0.8 (60-80%) | Backgrounds, overlays |
| Opacity Low | 0.3-0.5 (30-50%) | Subtle overlays |
| Opacity Minimal | 0.1-0.2 (10-20%) | Very subtle tints |

## Component Patterns

### Button Styles

**Primary Button:**
- Background: Primary color (`#FAC638`)
- Text: White, Bold (700)
- Padding: 12-16dp vertical, 16-24dp horizontal
- Border Radius: Radius Full or Radius LG
- Shadow: Primary Shadow
- Height: 48dp minimum

**Secondary Button:**
- Background: primary/10 (light mode) or primary/20 (dark mode)
- Text: Primary color, Bold (700)
- Padding: 12-16dp vertical, 16-24dp horizontal
- Border Radius: Radius Full or Radius LG
- Shadow: None
- Height: 48dp minimum

**Icon Button:**
- Size: 40-48dp
- Border Radius: Radius Full
- Background: primary/10 or transparent

### Input Fields

- Height: 48dp minimum
- Padding: 12-16dp
- Border: 1px solid (gray-200 light / gray-700 dark)
- Border Radius: Radius MD
- Focus: 2px border with Primary color
- Placeholder: Text Tertiary color

### Cards

- Background: White (light) / slate-800 (dark)
- Border Radius: Radius LG or Radius XL
- Padding: 16dp
- Shadow: Elevation 2
- Border: Optional 1px (gray-200 / gray-700)

### Bottom Navigation

- Height: 64dp
- Background: Background color with 80% opacity
- Backdrop Blur: Small (8px)
- Border Top: 1px solid (white/10 opacity)
- Icon Size: 24dp
- Active Color: Primary
- Inactive Color: gray-500 (light) / gray-400 (dark)

### Top Navigation/Header

- Height: 56dp minimum
- Background: Background color with 80% opacity
- Backdrop Blur: Small (8px)
- Padding: 16dp horizontal
- Border Bottom: Optional 1px solid (white/10 opacity)

## Icon Sizes

| Size Name | Value (dp) | Usage |
|-----------|------------|-------|
| Icon XS | 16dp | Small inline icons |
| Icon SM | 20dp | Button icons |
| Icon MD | 24dp | Default icons, navigation |
| Icon LG | 28dp | Prominent actions |
| Icon XL | 32dp | Feature icons |
| Icon 2XL | 48dp | Hero icons |

## Grid & Layout

### Screen Breakpoints

For Compose Multiplatform, we'll primarily target mobile screens:

| Device | Width | Columns | Gutter |
|--------|-------|---------|--------|
| Phone (Portrait) | 360-414dp | 4 columns | 16dp |
| Phone (Landscape) | 640-896dp | 8 columns | 16dp |
| Tablet (Portrait) | 768-834dp | 8 columns | 24dp |
| Tablet (Landscape) | 1024-1366dp | 12 columns | 24dp |

### Grid System

- **Columns:** 4 (portrait) / 8 (landscape)
- **Gutter:** 16dp (phone) / 24dp (tablet)
- **Margin:** 16dp (phone) / 24dp (tablet)

## Motion & Animation

### Duration

| Speed | Duration (ms) | Usage |
|-------|---------------|-------|
| Fast | 150ms | Micro-interactions, ripples |
| Normal | 300ms | Standard transitions |
| Slow | 500ms | Complex animations, page transitions |

### Easing

- **Standard:** Cubic bezier (0.4, 0.0, 0.2, 1) - Default easing
- **Decelerate:** Cubic bezier (0.0, 0.0, 0.2, 1) - Enter animations
- **Accelerate:** Cubic bezier (0.4, 0.0, 1, 1) - Exit animations

## Implementation Notes

### Kotlin/Compose Translation

For Compose Multiplatform:

1. **Colors:** Create a `Color.kt` file with color definitions
2. **Typography:** Create a `Typography.kt` file with Material 3 typography
3. **Spacing:** Create a `Spacing.kt` file with spacing constants
4. **Theme:** Create a `Theme.kt` file combining all tokens
5. **Dark Mode:** Use Material 3's `ColorScheme` for light/dark themes

### Example Color Definition

```kotlin
// ui/theme/Color.kt
val Primary = Color(0xFFFAC638)
val BackgroundLight = Color(0xFFF8F8F5)
val BackgroundDark = Color(0xFF231E0F)
```

### Example Typography Definition

```kotlin
// ui/theme/Typography.kt
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = BeVietnamPro,
        fontWeight = FontWeight.Black,
        fontSize = 36.sp,
        lineHeight = 43.2.sp
    ),
    // ... more styles
)
```

## Next Steps

1. ✅ Design tokens extracted and documented
2. [ ] Create `ui/theme/Color.kt` with color definitions
3. [ ] Create `ui/theme/Typography.kt` with typography scale
4. [ ] Create `ui/theme/Spacing.kt` with spacing constants
5. [ ] Create `ui/theme/Theme.kt` with Material 3 theme
6. [ ] Create reusable component library
7. [ ] Implement dark mode support

---

**Last Updated:** October 11, 2025
**Source:** Stitch design output (`stitch_superpets/`)
