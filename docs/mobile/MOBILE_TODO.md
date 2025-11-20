# Superpets Mobile Development Todo List

## Phase 0: UI Design & Design System (Stitch Integration) ✅ COMPLETE

> **Note:** This phase can run in parallel with Phase 1. Design system setup should be completed before Phase 2.

- [x] **Generate UI designs with Google Stitch**
  - ✅ Submit design brief to Stitch (see design prompt)
  - ✅ Review generated designs for all 10 screens
  - ✅ Request iterations/refinements as needed
  - ✅ Approve final designs

- [x] **Extract design tokens from Stitch output**
  - ✅ Document color palette (primary, secondary, background, etc.)
  - ✅ Extract typography scale (sizes, weights, line heights)
  - ✅ Document spacing system (4dp, 8dp, 16dp, etc.)
  - ✅ Note border radius, elevation, and other design tokens

- [x] **Export assets from Stitch** (Documented & Structured)
  - ✅ Identified all assets in Stitch designs
  - ✅ Created resource directories for Android/iOS
  - ✅ Using Material Icons for UI (no custom export needed)
  - ⚠️ Manual extraction needed: Logo/mascot, app icon
  - ⏭️ Splash screen assets (deferred to Phase 5)
  - **See:** `ASSETS_GUIDE.md`, `APP_ICON_TODO.md`, `ASSETS_README.md`

- [x] **Create design system in code**
  - ✅ Create `ui/theme/Color.kt` with color palette
  - ✅ Create `ui/theme/Typography.kt` with text styles
  - ✅ Create `ui/theme/Spacing.kt` with spacing constants
  - ✅ Create `ui/theme/Shape.kt` with border radius definitions
  - ✅ Create `ui/theme/Theme.kt` with Material 3 theme
  - ✅ Support light and dark themes

- [x] **Build reusable component library**
  - ✅ Primary/Secondary/Tertiary buttons
  - ✅ Input fields (text, email, password, search)
  - ✅ Cards (hero card, history card, credit package card, stats card)
  - ✅ Top app bar with credit display
  - ✅ Bottom navigation bar (standard and floating variants)
  - ✅ Loading indicators (simple, screen, generation, skeleton)
  - ✅ Error state components (generic, network, insufficient credits)
  - ✅ Empty state components (generic, no results)
  - ✅ Badge components (credit badge, status badge)

**See `DESIGN_TOKENS.md`, `DESIGN_SYSTEM_USAGE.md`, and `COMPONENT_LIBRARY.md` for documentation.**

- [ ] **Convert Stitch designs to Compose code (per screen)**
  - [ ] Landing/Onboarding screen
  - [ ] Login screen
  - [ ] Signup screen
  - [ ] Home/Dashboard screen
  - [ ] Hero selection screen
  - [ ] Image upload/editor screen
  - [ ] Generation progress screen
  - [ ] Result gallery screen
  - [ ] Edit history screen
  - [ ] Profile screen
  - [ ] Pricing/Credits screen

## Phase 1: Core Infrastructure (Foundation) ✅ COMPLETE

- [x] **Set up API client with Ktor for backend communication**
  - ✅ Configure HTTP client to communicate with `https://api.superpets.fun`
  - ✅ Add authentication interceptor for Bearer tokens
  - ✅ Implement request/response serialization

- [x] **Integrate Supabase Auth SDK for Kotlin Multiplatform**
  - ✅ Add Supabase KMP dependencies
  - ✅ Configure Supabase client with project credentials
  - ✅ Implement auth state management

- [x] **Create shared data models (User, Hero, EditHistory, etc.)**
  - ✅ User model
  - ✅ Hero model
  - ✅ EditHistory model
  - ✅ CreditTransaction model
  - ✅ API request/response models

**See `INFRASTRUCTURE_SETUP.md` for detailed documentation.**

## Phase 2: Authentication & Navigation (Essential) ✅ COMPLETE

- [x] **Implement authentication screens (Login, Signup)**
  - ✅ Login screen UI (`screens/auth/LoginScreen.kt`)
  - ✅ Signup screen UI (`screens/auth/SignupScreen.kt`)
  - ✅ Form validation (email, password, password confirmation)
  - ✅ Error handling with snackbars
  - ✅ Token storage via SupabaseSessionManager

- [x] **Create landing/onboarding screen with app overview**
  - ✅ Onboarding flow (`screens/landing/LandingScreen.kt`)
  - ✅ Navigation hub (`navigation/RootNavigationGraph.kt`)
  - ✅ Feature showcase (29+ heroes, AI-powered, lightning-fast)
  - ✅ "Get Started" CTA button

- [x] **Session persistence implementation**
  - ✅ Custom `SupabaseSessionManager` implementing `SessionManager` interface
  - ✅ Multiplatform Settings library for secure storage
  - ✅ Platform-specific storage: Keychain (iOS), EncryptedSharedPreferences (Android)
  - ✅ Auto-load session on app restart (`autoLoadFromStorage = true`)
  - ✅ Auto token refresh (`alwaysAutoRefresh = true`)
  - ✅ 500ms delay in AuthManager init to allow session loading

- [x] **Navigation logic**
  - ✅ Splash screen with navigation decision logic
  - ✅ Flow: Splash → Landing (if not onboarded) → Auth (if not authenticated) → Main
  - ✅ Auth navigation graph for Login ↔ Signup
  - ✅ Updated root navigation graph with all routes

**Completion Date:** October 14, 2025
**Key Files:**
- `screens/auth/LoginScreen.kt`, `SignupScreen.kt`, `AuthViewModel.kt`
- `screens/landing/LandingScreen.kt`, `LandingViewModel.kt`
- `screens/splash/SplashScreen.kt`, `SplashViewModel.kt`
- `data/auth/SupabaseSessionManager.kt`
- `navigation/AuthNavigationGraph.kt`, `RootNavigationGraph.kt`

## Phase 3: Core Features (MVP) - ✅ 95% COMPLETE (Nov 6, 2025)

- [x] **Implement hero selection screen (29+ heroes grid)** ✅
  - ✅ Fetch heroes from `/heroes` endpoint
  - ✅ Grid layout with tabs (Classic vs Unique)
  - ✅ Hero card components
  - ✅ Search/filter functionality

- [x] **Build image picker with platform-specific implementations** ✅ (Partial)
  - ✅ Gallery picker implemented (multi-select 1-10 images)
  - ✅ Multiple image selection working
  - ❌ Camera capture option (placeholder exists, not implemented)

- [x] **Implement image compression to 2048x2048 before upload** ✅
  - ✅ Platform-specific compression utilities (expect/actual for Android/iOS)
  - ✅ Quality: JPEG 85%
  - ✅ Target: 1-3MB per image achieved
  - ✅ Integrated into generation flow

- [x] **Create image editor screen with hero selection and generate button** ✅
  - ✅ Image preview (shows selected images count)
  - ✅ Hero selection display (shows selected hero)
  - ✅ Output count slider (1-10)
  - ✅ Credit cost calculator (dynamic display)
  - ✅ Generate button with validation

- [x] **Implement credit display and management UI** ✅
  - ✅ Credit balance in top bar (HomeScreen)
  - ✅ Real-time credit updates after generation
  - ✅ Credit validation before generation
  - ✅ Fetch from `/user/profile` and `/user/credits` endpoints

- [x] **Build result gallery screen to display generated images** ✅
  - ✅ Image carousel (HorizontalPager)
  - ✅ Swipe navigation with page indicators
  - ✅ Download functionality (Android: MediaStore API, iOS: PHPhotoLibrary)
  - ✅ Share functionality (Android: FileProvider + Intent, iOS: UIActivityViewController)
  - ✅ Navigation to history

## Phase 4: User Experience (Important) - 🚧 IN PROGRESS

- [x] **Implement edit history screen with past generations** ✅ (Nov 6, 2025)
  - ✅ Fetch from `/user/edits` endpoint
  - ✅ 2-column grid layout (follows Stitch design)
  - ✅ Pull to refresh
  - ✅ Date filter (Newest/Oldest first)
  - ✅ Hero filter (filter by specific hero)
  - ✅ Clear filters button
  - ✅ Loading, error, and empty states

- [ ] **Profile Screen** ⏳ (HIGH PRIORITY)
  - Route exists, screen not implemented
  - Display user email, credit balance, transaction history
  - Logout button
  - Account settings

- [ ] **Add pricing/credit purchase screen with Stripe integration** ⏳
  - Credit packages display
  - Stripe checkout session
  - Payment confirmation
  - Credit balance update

- [x] **Add image download/share functionality** ✅ (Nov 6, 2025)
  - ✅ Save to device storage (Android: MediaStore API, iOS: PHPhotoLibrary)
  - ✅ Share to social media (Android: FileProvider + Intent, iOS: UIActivityViewController)
  - ✅ Platform-specific permissions (Android: MediaStore, iOS: Photos)
  - ✅ Integrated into ResultGalleryScreen with EditorViewModel

- [ ] **Implement error handling and loading states** ⏳
  - ✅ Network error handling (401, 402, 429 implemented)
  - ✅ Insufficient credits error
  - ✅ Loading indicators
  - ⏳ Enhanced retry mechanisms
  - ⏳ Offline mode graceful degradation

## Phase 5: Platform Testing & Polish

- [ ] **Test Android build and fix platform-specific issues**
  - Test on various Android devices
  - Fix layout issues
  - Test permissions
  - Performance optimization

- [ ] **Test iOS build and fix platform-specific issues**
  - Test on various iOS devices
  - Fix layout issues
  - Test permissions
  - Performance optimization

- [ ] **Add app icons and splash screens for both platforms**
  - Design app icon (1024x1024)
  - Generate adaptive icons (Android)
  - iOS icon set
  - Splash screens

## Phase 6: Optimization (Optional)

- [ ] **Implement local caching with Room Database**
  - Cache heroes locally
  - Cache edit history
  - Offline viewing
  - Sync strategy

- [ ] **Add analytics and error monitoring (optional)**
  - Sentry integration
  - Analytics events
  - Crash reporting
  - Performance monitoring

---

## API Endpoints Reference

- `GET /heroes` - Get all heroes (public, no auth)
- `POST /nano-banana/upload-and-edit` - Upload and generate
- `GET /user/profile` - Get user profile
- `GET /user/credits` - Get credit balance
- `GET /user/transactions` - Get transaction history
- `GET /user/edits` - Get edit history
- `POST /stripe/create-checkout-session` - Create payment session

## Technical Requirements

- **Backend URL:** `https://api.superpets.fun`
- **Authentication:** Bearer token (Supabase JWT)
- **Image Upload:** Max 10MB, types: jpeg/png/webp/gif
- **Compression:** 2048x2048px, JPEG 80-90%
- **Rate Limits:** 5 requests/minute for image generation
- **Credits:** 1 credit = 1 image generated

## Stitch-to-Code Workflow

**Step-by-step process for converting Stitch designs:**

1. **Get Stitch Designs** → Submit design brief, review outputs
2. **Extract Design Tokens** → Document colors, typography, spacing
3. **Export Assets** → Icons, images, app icon at all resolutions
4. **Create Theme System** → Build Color.kt, Typography.kt, Spacing.kt
5. **Build Components** → Reusable UI components with @Preview
6. **Convert Screens** → Share screenshots with Claude Code for conversion
7. **Connect Data** → Wire up ViewModels and API calls
8. **Test & Refine** → Compare with Stitch, adjust spacing/colors

**Using Claude Code for conversion:**
- Share Stitch screenshot: "Convert this screen to Compose Multiplatform"
- Claude generates production-ready Compose code
- Review, test, and iterate

## Notes

- All authenticated requests need `Authorization: Bearer <token>` header
- Credit validation happens both client-side and server-side
- Images processed in parallel on backend (fast performance)
- Hero selection is required (no custom prompts)
- **Start with Phase 0 (Stitch) in parallel with Phase 1 (Infrastructure)**
- Design system must be complete before building screens (Phase 2+)
