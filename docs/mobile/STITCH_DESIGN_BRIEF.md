# Superpets Mobile App - UI Design Brief for Google Stitch

## App Overview

**App Name:** Superpets
**Tagline:** Transform Your Pets into Superheroes
**Domain:** superpets.fun
**Platforms:** iOS & Android (Material 3 Design System)

Superpets is an AI-powered mobile application that transforms pet photos into superhero versions using advanced image generation technology. Users can choose from 29+ hero characters, upload pet photos, and generate multiple superhero transformations. The app uses a credit-based system where users get 5 free credits to start.

## Brand Identity

- **Color Palette:** Vibrant, playful, and energetic (suggest: primary purple/blue, accent orange/yellow for hero theme)
- **Style:** Modern, fun, approachable - appealing to pet owners
- **Tone:** Friendly, exciting, magical transformation theme
- **Target Audience:** Pet owners aged 25-45, social media enthusiasts

## Required Screens (10 screens total)

### 1. Landing/Onboarding Screen
- Hero section with app logo and mascot
- Compelling headline: "Turn Your Pet into a Superhero"
- Value proposition: "AI-Powered • 29+ Heroes • Lightning Fast"
- CTA buttons: "Get Started" and "Sign In"
- Sample before/after image showcase
- "5 Free Credits" badge prominently displayed

### 2. Sign Up Screen
- Email input field
- Password input field
- "Create Account" button
- Social login options (Google, Apple)
- "Already have an account? Sign In" link
- Back button to return to landing

### 3. Login Screen
- Email input field
- Password input field
- "Sign In" button
- "Forgot password?" link
- Social login options (Google, Apple)
- "Don't have an account? Sign Up" link
- Back button to return to landing

### 4. Home/Dashboard Screen
- Top bar with credit balance display (e.g., "💎 12 credits")
- Large, prominent "Create New" button with camera icon
- Quick stats: Total creations, recent activity
- Recent/Featured generations gallery (2x2 grid)
- Bottom navigation: Home, Create, History, Profile
- Welcome message for first-time users

### 5. Hero Selection Screen
- **Two tabs:** "Classic Heroes" (10) and "Unique Heroes" (19+)
- Grid layout with hero cards (2 columns)
- Each card shows:
  - Hero avatar/icon
  - Hero name (e.g., "Superman", "Galactic Guardian")
  - Brief description overlay on tap
- Selected hero highlighted with border/checkmark
- "Continue" button at bottom
- Search bar to filter heroes

### 6. Image Upload/Editor Screen
- Large preview area showing selected image(s)
- "Add Photo" button with camera and gallery options
- Image counter: "1-10 images allowed"
- Selected hero display at top
- "Change Hero" button
- Number of outputs slider (1-10 images)
- Credit cost calculator: "Cost: 5 credits" (dynamic)
- Large "Generate" button (disabled if insufficient credits)
- Image compression indicator: "Optimizing..." during upload

### 7. Generation Progress Screen
- Animated loading indicator
- Progress text: "Transforming your pet into [Hero Name]..."
- Estimated time remaining
- Fun facts or tips while waiting
- Cancel option

### 8. Result Gallery Screen
- Full-screen image carousel for generated results
- Swipe between multiple outputs
- Action buttons: Download, Share, Regenerate
- "Save to History" option
- Credit cost display: "-5 credits used"
- "Generate More" button

### 9. Edit History Screen
- Timeline/grid view toggle
- Each history item shows:
  - Thumbnail of generated image
  - Hero used
  - Date/time
  - Credits spent
- Filter options: Date, Hero type
- Pull to refresh
- Tap to view full result

### 10. Profile Screen
- User avatar and name
- Credit balance with large display
- "Buy More Credits" prominent button
- Account info: Email, member since
- Settings: Notifications, Privacy, Terms
- Edit history summary
- Logout button

### 11. Pricing/Credits Screen
- Credit packages displayed as cards:
  - Starter: 10 credits - $4.99
  - Popular: 50 credits - $19.99 (Best Value badge)
  - Pro: 100 credits - $34.99
- "What you get" explanation (1 credit = 1 image)
- Stripe payment integration UI
- "Restore Purchases" option for iOS

## Design Requirements

### Navigation
- Bottom tab bar: Home, Create (center, elevated), History, Profile
- Top app bar with back button, title, and actions (credits, settings)

### Interactive Elements
- Clear CTAs with loading states
- Haptic feedback for important actions
- Smooth transitions between screens
- Pull-to-refresh on list views

### Error States
- Friendly error messages
- Illustration for empty states
- Retry buttons
- Low credit warnings

### Accessibility
- High contrast text
- Touch targets minimum 48x48dp
- Screen reader friendly labels
- Support for large text sizes

### Platform Considerations
- iOS: Follow Human Interface Guidelines (rounded corners, SF Symbols)
- Android: Material 3 Design (Material You, dynamic colors)
- Support both light and dark themes

## Key User Flows

1. **First-Time User:** Landing → Sign Up → Home (with 5 free credits) → Hero Selection → Upload Photo → Generate → View Results
2. **Returning User:** Login → Home → View History → Create New → Hero Selection → Upload → Generate → Share Result
3. **Purchase Credits:** Profile → Buy Credits → Select Package → Checkout → Confirmation → Updated Balance

## Technical Notes

- Supports both light and dark themes
- Handles portrait and landscape orientations (primarily portrait)
- Image compression to 2048x2048px before upload
- Offline mode: View cached history, cannot generate new images

## Inspiration & References

- Similar to photo editing apps like Lensa, Remini
- Pet-friendly, Instagram-worthy aesthetic
- Superhero/comic book visual elements (subtle, not overwhelming)

## Deliverables Requested

- High-fidelity mockups for all 11 screens
- Light and dark mode variants
- iOS and Android platform-specific versions
- Interactive prototype showing key user flows
- Component library with reusable UI elements
- Design tokens export (colors, typography, spacing)
- All assets (icons, images) at multiple resolutions

---

**Submit this brief to Google Stitch to generate UI designs for the Superpets mobile app.**
