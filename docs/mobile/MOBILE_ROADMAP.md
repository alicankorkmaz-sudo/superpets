# Superpets Mobile Development Roadmap

**Last Updated:** October 14, 2025
**Project:** Superpets Mobile (Compose Multiplatform - Android & iOS)
**Current Phase:** Phase 3 (Core Features)

---

## 📊 Progress Overview

| Phase | Status | Completion | Priority |
|-------|--------|-----------|----------|
| **Phase 0:** Design System | ✅ Complete | 100% | HIGH |
| **Phase 1:** Core Infrastructure | ✅ Complete | 100% | HIGH |
| **Phase 2:** Auth & Onboarding | ✅ Complete | 100% | HIGH |
| **Phase 3:** Core Features (MVP) | 🚧 In Progress | 0% | **CURRENT** |
| **Phase 4:** User Experience | ⏳ Pending | 0% | HIGH |
| **Phase 5:** Platform Testing & Polish | ⏳ Pending | 0% | MEDIUM |
| **Phase 6:** Optimization | ⏳ Pending | 0% | LOW |

**Overall Progress:** 45% (3/6 phases complete)

---

## 🎯 Project Goals

**Primary Objective:** Build a native mobile app (Android & iOS) that provides feature parity with the web app at https://superpets.fun

**Core Value Proposition:**
- Transform pet photos into AI-generated superhero images
- 29+ hero characters (10 classics + 19 uniques)
- Fast parallel processing (10 images in ~15 seconds)
- Credit-based system (5 free credits on signup)
- Stripe integration for credit purchases

**Target Launch Date:** TBD (MVP ready after Phase 4)

---

## ✅ Phase 0: Design System & UI Components (COMPLETE)

**Status:** ✅ 100% Complete
**Completion Date:** October 2025

### Completed Items

- [x] **Design Token System**
  - `ui/theme/Color.kt` - Material 3 color palette (light & dark themes)
  - `ui/theme/Typography.kt` - Text styles hierarchy
  - `ui/theme/Spacing.kt` - Consistent spacing system
  - `ui/theme/Shape.kt` - Border radius definitions
  - `ui/theme/Theme.kt` - Unified Material 3 theme

- [x] **Reusable Component Library**
  - **Buttons:** Primary, Secondary, Tertiary variants (`ui/components/buttons/`)
  - **Input Fields:** Text, Email, Password, Search (`ui/components/input/`)
  - **Cards:** Hero cards, History cards, Credit packages, Stats cards (`ui/components/cards/`)
  - **Navigation:** Top app bar with credits, Bottom nav bar (`ui/components/navigation/`)
  - **Loading States:** Simple, Screen, Generation, Skeleton (`ui/components/loading/`)
  - **Error States:** Generic, Network, Insufficient credits (`ui/components/states/`)
  - **Empty States:** Generic, No results (`ui/components/states/`)
  - **Badges:** Credit badge, Status badge (`ui/components/badges/`)

### Documentation
- `DESIGN_TOKENS.md` - Design system specification
- `DESIGN_SYSTEM_USAGE.md` - Usage guidelines
- `COMPONENT_LIBRARY.md` - Component catalog with examples

---

## ✅ Phase 1: Core Infrastructure (COMPLETE)

**Status:** ✅ 100% Complete
**Completion Date:** October 2025

### Completed Items

- [x] **Ktor HTTP Client Configuration**
  - Base URL: `https://api.superpets.fun`
  - Platform-specific engines (OkHttp for Android, Darwin for iOS)
  - Content negotiation with kotlinx.serialization
  - Request/response logging
  - Authentication interceptor for Bearer tokens
  - File: `data/network/HttpClientFactory.kt`

- [x] **Supabase Auth SDK Integration**
  - Supabase Kotlin Multiplatform SDK installed
  - Auth module configured (`data/auth/AuthManager.kt`)
  - Custom SessionManager with Settings storage (`data/auth/SupabaseSessionManager.kt`)
  - Session persistence across app restarts
  - Auto token refresh (`alwaysAutoRefresh = true`)
  - Auto load from storage (`autoLoadFromStorage = true`)

- [x] **Data Models**
  - `User.kt` - User profile (email, credits, uid)
  - `Hero.kt` - Hero character (id, name, category, identity, scenes)
  - `EditHistory.kt` - Edit record (timestamp, prompt, images, credits)
  - `CreditTransaction.kt` - Transaction record (amount, type, description)
  - `ApiModels.kt` - Request/response DTOs

- [x] **API Service Layer**
  - `SuperpetsApiService.kt` - REST API client
  - All endpoints implemented:
    - `GET /heroes` - Get all heroes
    - `GET /user/profile` - Get user profile
    - `GET /user/credits` - Get credit balance
    - `GET /user/transactions` - Get transaction history
    - `GET /user/edits` - Get edit history
    - `POST /nano-banana/upload-and-edit` - Upload and edit images
    - `POST /stripe/create-checkout-session` - Create payment session
  - Error handling wrapper (`safeApiCall`)
  - Custom `ApiException` with status codes

- [x] **Repository Layer**
  - `SuperpetsRepository.kt` - Business logic layer
  - Clean interface for ViewModels
  - Result-based error handling
  - Logging with Napier

- [x] **Dependency Injection (Koin)**
  - Configured modules: `databaseModule`, `dataModule`, `superpetsModule`, `viewModelModule`, `platformModule`
  - Singleton services (AuthManager, ApiService, Repository)
  - Factory ViewModels
  - File: `di/Koin.kt`

### Documentation
- `INFRASTRUCTURE_SETUP.md` - Infrastructure setup guide

---

## ✅ Phase 2: Authentication & Onboarding (COMPLETE)

**Status:** ✅ 100% Complete
**Completion Date:** October 14, 2025

### Completed Items

- [x] **Authentication Screens**
  - ✅ `LoginScreen.kt` - Email/password login with Material 3 design
  - ✅ `SignupScreen.kt` - Registration with password confirmation
  - ✅ `AuthViewModel.kt` - Auth business logic with form validation
  - ✅ Forgot password dialog
  - ✅ Error handling with snackbars
  - ✅ "Get 5 Free Credits" badge prominently displayed
  - ✅ Navigation between Login ↔ Signup

- [x] **Onboarding Flow**
  - ✅ `LandingScreen.kt` - Welcoming first-time users
  - ✅ `LandingViewModel.kt` - Onboarding state management
  - ✅ Feature showcase (29+ heroes, AI-powered, lightning-fast)
  - ✅ "Get Started" CTA button
  - ✅ Saves onboarding completion to Settings

- [x] **Navigation Logic**
  - ✅ `SplashScreen.kt` - App initialization screen
  - ✅ `SplashViewModel.kt` - Navigation decision logic
  - ✅ Navigation flow: Splash → Landing (if not onboarded) → Auth (if not authenticated) → Main
  - ✅ `AuthNavigationGraph.kt` - Auth sub-navigation
  - ✅ Updated `RootNavigationGraph.kt` with all routes

- [x] **Session Persistence**
  - ✅ `SupabaseSessionManager.kt` - Custom SessionManager implementation
  - ✅ Uses multiplatform Settings library for secure storage
  - ✅ Platform-specific storage: Keychain (iOS), EncryptedSharedPreferences (Android)
  - ✅ JSON serialization/deserialization of UserSession
  - ✅ 500ms delay in AuthManager to allow session loading
  - ✅ Session restoration on app restart verified ✅

### Technical Highlights

**Navigation Flow:**
```
Splash Screen
  ↓
  Check onboarding status
  ├─ Not onboarded → Landing → Complete onboarding → Auth
  └─ Onboarded
      ├─ Authenticated → Main (Home)
      └─ Not Authenticated → Auth (Login/Signup)
```

**Session Persistence Architecture:**
- Web app: Uses `@supabase/supabase-js` with automatic localStorage
- Mobile app: Custom `SessionManager` required for Supabase KMP
- Storage: `multiplatform-settings` library with platform-specific secure storage
- JSON format: Serialized `UserSession` object stored with key `"supabase_user_session"`

### Verification
- ✅ User can sign up and login
- ✅ Session persists after app restart
- ✅ Onboarding shows once per device
- ✅ Proper navigation between all auth states
- ✅ Error handling for invalid credentials
- ✅ Password reset email functionality

---

## 🚧 Phase 3: Core Features (MVP) - IN PROGRESS

**Status:** 🚧 In Progress (0% complete)
**Priority:** HIGH (Critical for MVP)
**Estimated Duration:** 3-4 weeks

### Overview

This phase implements the core image editing functionality that makes Superpets valuable to users. After completion, users will be able to select heroes, upload pet photos, and generate superhero transformations.

### 3.1 Home/Dashboard Screen

**Priority:** HIGH
**Estimated Time:** 1 week

**Tasks:**
- [ ] Create `screens/home/HomeScreen.kt`
- [ ] Create `screens/home/HomeViewModel.kt`
- [ ] Implement UI layout:
  - [ ] Top app bar with credit balance (live)
  - [ ] Welcome message with user email
  - [ ] Quick stats cards (total edits, credits used)
  - [ ] Recent generations preview (3-5 most recent)
  - [ ] Primary CTA: "Create New Superhero" button
  - [ ] Bottom navigation: Home, History, Profile
- [ ] Fetch data on screen load:
  - [ ] User profile (`getUserProfile()`)
  - [ ] Credit balance (`getUserCredits()`)
  - [ ] Recent edit history (`getEditHistory()` with limit)
- [ ] Navigation:
  - [ ] Navigate to Editor screen on CTA click
  - [ ] Navigate to full history from recent preview
  - [ ] Navigate to profile screen
- [ ] Pull-to-refresh functionality
- [ ] Loading states and error handling

**API Endpoints:**
- `GET /user/profile` - Auto-creates user with 5 credits if first API call
- `GET /user/credits` - Get current balance
- `GET /user/edits` - Get recent edits

**UI Components (already built):**
- `SuperpetsTopAppBar` with credit display
- `SuperpetsBottomNavigationBar`
- `StatsCard` for quick stats
- `HistoryCard` for recent previews
- `PrimaryButton` for CTA

**Success Criteria:**
- ✅ User sees their credit balance immediately
- ✅ Recent generations load and display correctly
- ✅ Pull-to-refresh updates all data
- ✅ Smooth navigation to editor and other screens
- ✅ Loading and error states are user-friendly

---

### 3.2 Hero Selection Screen

**Priority:** HIGH
**Estimated Time:** 1 week

**Tasks:**
- [ ] Create `screens/heroes/HeroSelectionScreen.kt`
- [ ] Create `screens/heroes/HeroSelectionViewModel.kt`
- [ ] Fetch heroes from `/heroes` endpoint on screen load
- [ ] Implement UI layout:
  - [ ] Top app bar with credit display
  - [ ] Search bar for filtering heroes
  - [ ] Tabs: "Classic Heroes" (10) and "Unique Heroes" (19)
  - [ ] Lazy vertical grid (2 columns)
  - [ ] Hero cards with image placeholder, name, category badge
  - [ ] Selected hero highlighted with border/checkmark
  - [ ] Bottom sheet showing selected hero details
  - [ ] "Continue" button (fixed at bottom)
- [ ] Hero card component (reuse `HeroCard` from components)
- [ ] Search/filter functionality
- [ ] Tab switching between classics and uniques
- [ ] Hero selection state management
- [ ] Navigate to Editor with selected hero

**API Endpoints:**
- `GET /heroes` - Public endpoint, no auth required
- Returns: `{ heroes: [{ id, name, category, identity, scenes }] }`

**Data Model (already exists):**
```kotlin
data class Hero(
    val id: String,
    val name: String,
    val category: String,
    val identity: String,
    val scenes: List<String>
)
```

**UI Components (already built):**
- `HeroCard` - Displays hero with image, name, category
- `SearchTextField` - Search input field
- `SuperpetsTopAppBar` - Top bar with credits
- `PrimaryButton` - Continue button

**Success Criteria:**
- ✅ All 29 heroes load and display correctly
- ✅ Tabs switch smoothly between classics and uniques
- ✅ Search filters heroes by name
- ✅ Selected hero is visually indicated
- ✅ Bottom sheet shows hero details
- ✅ Continue button is disabled until hero selected
- ✅ Selected hero passed to Editor screen

---

### 3.3 Image Picker (Platform-Specific)

**Priority:** HIGH
**Estimated Time:** 1 week

**Tasks:**

**Common Interface:**
- [ ] Create `expect class ImagePicker` in `commonMain`
- [ ] Define interface: `suspend fun pickImage(): ByteArray?`
- [ ] Define interface: `suspend fun pickMultipleImages(max: Int): List<ByteArray>`
- [ ] Define interface: `suspend fun captureImage(): ByteArray?`

**Android Implementation (`androidMain`):**
- [ ] Create `actual class ImagePicker` using `ActivityResultContracts`
- [ ] Implement single image picker:
  - [ ] Use `PickVisualMedia` contract
  - [ ] Request permission if needed (`READ_MEDIA_IMAGES`)
  - [ ] Return image as `ByteArray`
- [ ] Implement multiple image picker:
  - [ ] Use `PickMultipleVisualMedia` contract
  - [ ] Limit to specified max (1-10)
  - [ ] Return list of `ByteArray`
- [ ] Implement camera capture:
  - [ ] Use `TakePicture` contract
  - [ ] Request camera permission
  - [ ] Return captured image as `ByteArray`
- [ ] Compress images immediately after selection (see 3.4)

**iOS Implementation (`iosMain`):**
- [ ] Create `actual class ImagePicker` using `PHPickerViewController`
- [ ] Implement single image picker:
  - [ ] Configure `PHPickerConfiguration` with image filter
  - [ ] Request photo library permission
  - [ ] Convert `UIImage` to `ByteArray`
- [ ] Implement multiple image picker:
  - [ ] Set `selectionLimit` on configuration
  - [ ] Handle multiple selections
  - [ ] Convert all images to `ByteArray`
- [ ] Implement camera capture:
  - [ ] Use `UIImagePickerController` with camera source
  - [ ] Request camera permission
  - [ ] Return captured image as `ByteArray`
- [ ] Compress images immediately after selection (see 3.4)

**Permissions:**
- [ ] Android: `READ_MEDIA_IMAGES`, `CAMERA` in `AndroidManifest.xml`
- [ ] iOS: `NSPhotoLibraryUsageDescription`, `NSCameraUsageDescription` in `Info.plist`

**Success Criteria:**
- ✅ Users can pick single/multiple images from gallery
- ✅ Users can capture images with camera
- ✅ Permissions are requested properly
- ✅ Images are returned as `ByteArray` ready for compression
- ✅ Error handling for permission denial

---

### 3.4 Image Compression

**Priority:** HIGH (Backend validation: 10MB max)
**Estimated Time:** 3 days

**Requirements:**
- **Backend limit:** 10MB per image
- **Target:** Compress to 2048x2048px max, JPEG quality 80-90%, ~1-3MB
- **Rationale:** Modern phone cameras capture 5-25MB photos, compression prevents upload failures

**Tasks:**

**Common Interface:**
- [ ] Create `expect class ImageCompressor` in `commonMain`
- [ ] Define interface: `suspend fun compress(imageData: ByteArray, maxSizePx: Int, quality: Int): ByteArray`
- [ ] Add logging: original size, compressed size, dimensions

**Android Implementation:**
- [ ] Create `actual class ImageCompressor` using `Bitmap` and `BitmapFactory`
- [ ] Compression algorithm:
  - [ ] Decode `ByteArray` to `Bitmap`
  - [ ] Calculate scaling factor (if width or height > 2048)
  - [ ] Scale down using `createScaledBitmap()`
  - [ ] Compress to JPEG with quality 85% (configurable)
  - [ ] Write to `ByteArrayOutputStream`
  - [ ] Return compressed `ByteArray`
- [ ] Add EXIF orientation handling (rotate if needed)

**iOS Implementation:**
- [ ] Create `actual class ImageCompressor` using `UIImage`
- [ ] Compression algorithm:
  - [ ] Convert `ByteArray` to `UIImage`
  - [ ] Calculate scaling factor
  - [ ] Resize using `UIGraphicsImageRenderer`
  - [ ] Compress to JPEG using `jpegData(compressionQuality:)`
  - [ ] Convert back to `ByteArray`
- [ ] Preserve image orientation during compression

**Integration:**
- [ ] Call compression immediately after image selection
- [ ] Show progress indicator during compression
- [ ] Validate compressed size is under 10MB
- [ ] Show error if compression fails or still too large

**Testing:**
- [ ] Test with various image sizes (small, large, portrait, landscape)
- [ ] Verify compressed images are under 10MB
- [ ] Verify quality is acceptable (no visible artifacts)

**Success Criteria:**
- ✅ All images compressed to ≤2048x2048px
- ✅ Compressed size is 1-3MB on average
- ✅ Quality is acceptable (JPEG 85%)
- ✅ Compression happens quickly (<1 second per image)
- ✅ Images pass backend validation (10MB limit)

---

### 3.5 Editor Screen

**Priority:** HIGH
**Estimated Time:** 1.5 weeks

**Tasks:**
- [ ] Create `screens/editor/EditorScreen.kt`
- [ ] Create `screens/editor/EditorViewModel.kt`
- [ ] Implement UI layout:
  - [ ] Top app bar with credit balance and back button
  - [ ] Selected hero display (card with name, identity)
  - [ ] Image picker section:
    - [ ] "Choose from Gallery" button
    - [ ] "Take Photo" button
    - [ ] Selected images preview (horizontal scroll)
    - [ ] Remove image button on each preview
  - [ ] Output settings:
    - [ ] "Number of Images" slider (1-10)
    - [ ] Credit cost display: "This will cost X credits"
  - [ ] Validation messages:
    - [ ] "Please select at least 1 image"
    - [ ] "Insufficient credits. You have X but need Y."
  - [ ] "Generate Superhero Images" button (primary, full width)
  - [ ] Loading overlay during generation
- [ ] State management:
  - [ ] Selected hero (passed from Hero Selection)
  - [ ] Selected images (`List<ByteArray>`)
  - [ ] Number of images to generate (1-10)
  - [ ] Credit balance (live from API)
  - [ ] Loading state
  - [ ] Error state
- [ ] Business logic:
  - [ ] Validate images selected
  - [ ] Validate sufficient credits: `credits >= numImages`
  - [ ] Show error if validation fails (client-side)
  - [ ] Compress images before upload (call ImageCompressor)
  - [ ] Call API: `uploadAndEditImages(imageData, heroId, numImages)`
  - [ ] Navigate to Result Gallery on success
  - [ ] Handle API errors (show snackbar)
- [ ] Credit cost calculator:
  - [ ] 1 credit per generated image
  - [ ] Update in real-time as slider moves
  - [ ] Disable button if `credits < numImages`

**API Endpoints:**
- `POST /nano-banana/upload-and-edit` - Upload and generate
  - Request: Multipart form data with `hero_id`, `num_images`, `images[]`
  - Response: `{ outputs: [{ url }], prompt: "..." }`
  - Credit cost: `numImages` credits (1 per image)

**State Flow:**
1. User arrives from Hero Selection with selected hero
2. User selects images from gallery or camera
3. Images are compressed immediately
4. User adjusts output count slider
5. Credit cost updates in real-time
6. User taps "Generate"
7. Client-side validation (images, credits)
8. Upload images with loading indicator
9. Backend processes (parallel, ~15 seconds for 10 images)
10. Navigate to Result Gallery with generated images

**Error Handling:**
- **Insufficient credits:** Show snackbar with "Insufficient credits. You have X but need Y. Purchase more credits?"
- **Upload failure:** Show snackbar with "Upload failed. Please try again."
- **API error 429:** Show snackbar with "Rate limit exceeded. Please wait a moment."
- **Network error:** Show snackbar with "Network error. Check your connection."

**Success Criteria:**
- ✅ Users can select 1+ images from gallery or camera
- ✅ Images are compressed and validated (<10MB)
- ✅ Credit cost is clearly displayed and accurate
- ✅ Generate button is disabled if insufficient credits
- ✅ Loading indicator shows during upload/generation
- ✅ Success navigates to result gallery
- ✅ Errors are shown with user-friendly messages

---

### 3.6 Result Gallery Screen

**Priority:** HIGH
**Estimated Time:** 1 week

**Tasks:**
- [ ] Create `screens/results/ResultGalleryScreen.kt`
- [ ] Create `screens/results/ResultGalleryViewModel.kt`
- [ ] Implement UI layout:
  - [ ] Top app bar with updated credit balance and close button
  - [ ] Fullscreen image carousel (swipe left/right)
  - [ ] Page indicator (e.g., "3 / 10")
  - [ ] Image zoom/pan gestures (optional but recommended)
  - [ ] Bottom action buttons:
    - [ ] "Download" - Save to device storage
    - [ ] "Share" - Share to social media
    - [ ] "Regenerate" - Go back to editor
  - [ ] Success message: "Your superhero images are ready! 🎉"
  - [ ] Prompt display (optional, collapsible)
- [ ] State management:
  - [ ] List of generated image URLs
  - [ ] Current page index
  - [ ] Download/share progress
- [ ] Image loading:
  - [ ] Use Coil for image loading from URLs
  - [ ] Show loading indicator while images load
  - [ ] Cache images locally
- [ ] Actions:
  - [ ] Download: Save image to gallery (platform-specific)
  - [ ] Share: Open share sheet (platform-specific)
  - [ ] Regenerate: Navigate back to Editor
  - [ ] Close: Navigate back to Home

**Data Received from Editor:**
```kotlin
data class EditImageResponse(
    val outputs: List<OutputImage>,
    val prompt: String
)
data class OutputImage(val url: String)
```

**Platform-Specific Implementations:**

**Download (Android):**
- [ ] Request `WRITE_EXTERNAL_STORAGE` permission (API < 29)
- [ ] Use `MediaStore` API to save to Pictures folder
- [ ] Show success toast: "Image saved to gallery"

**Download (iOS):**
- [ ] Request photo library permission
- [ ] Use `PHPhotoLibrary` to save to camera roll
- [ ] Show success toast: "Image saved to photos"

**Share (Android & iOS):**
- [ ] Create temporary file from downloaded image
- [ ] Use platform-specific share intent/sheet
- [ ] Share with text: "Check out my superhero pet from Superpets! https://superpets.fun"

**Image Carousel:**
- [ ] Use `HorizontalPager` from Accompanist (or Compose Foundation)
- [ ] Swipe left/right to navigate between images
- [ ] Page indicator dots or "X / Y" text
- [ ] Smooth animations

**Success Criteria:**
- ✅ All generated images load and display
- ✅ Users can swipe between images smoothly
- ✅ Download saves images to device gallery
- ✅ Share opens native share sheet
- ✅ Images are cached for fast re-viewing
- ✅ Regenerate navigates back to editor with same hero
- ✅ Close returns to home with updated credit balance

---

### Phase 3 Summary

**Total Estimated Time:** 6-7 weeks
**Deliverables:**
- ✅ Home/Dashboard screen with live data
- ✅ Hero selection with search and filtering
- ✅ Platform-specific image picker (gallery + camera)
- ✅ Image compression (2048x2048, <10MB)
- ✅ Editor screen with validation and credit calculation
- ✅ API integration for image upload and generation
- ✅ Result gallery with download/share functionality

**After Phase 3:**
- Users can complete the full flow: signup → hero selection → upload → generate → view/share results
- **MVP is ready for internal testing**

---

## ⏳ Phase 4: User Experience Enhancements

**Status:** ⏳ Pending
**Priority:** HIGH (Required for production)
**Estimated Duration:** 3-4 weeks

### 4.1 Edit History Screen

**Priority:** HIGH
**Estimated Time:** 1 week

**Tasks:**
- [ ] Create `screens/history/EditHistoryScreen.kt`
- [ ] Create `screens/history/EditHistoryViewModel.kt`
- [ ] Fetch edit history from `/user/edits` endpoint
- [ ] Implement UI layout:
  - [ ] Top app bar with credit balance
  - [ ] View toggle: Grid view / Timeline view
  - [ ] Grid view: 2-column grid of thumbnails
  - [ ] Timeline view: Chronological list with date headers
  - [ ] Each item shows: thumbnail, hero used, date, credits used
  - [ ] Tap to view full result gallery
  - [ ] Pull-to-refresh
  - [ ] Empty state: "No edits yet. Create your first superhero!"
- [ ] Pagination (load more as user scrolls)
- [ ] Filter options (optional):
  - [ ] By hero
  - [ ] By date range
- [ ] Navigation to Result Gallery with historical data

**API Endpoints:**
- `GET /user/edits` - Returns all edits with input/output URLs, prompt, credits, timestamp

**Data Model (already exists):**
```kotlin
data class EditHistory(
    val id: String,
    val userId: String,
    val prompt: String,
    val inputImages: List<String>,
    val outputImages: List<String>,
    val creditsCost: Int,
    val timestamp: Long
)
```

**Success Criteria:**
- ✅ All past edits load and display
- ✅ Grid and timeline views work smoothly
- ✅ Tapping an edit shows full result gallery
- ✅ Pull-to-refresh updates history
- ✅ Empty state is user-friendly

---

### 4.2 Profile Screen

**Priority:** MEDIUM
**Estimated Time:** 1 week

**Tasks:**
- [ ] Create `screens/profile/ProfileScreen.kt`
- [ ] Create `screens/profile/ProfileViewModel.kt`
- [ ] Fetch user profile from `/user/profile` endpoint
- [ ] Implement UI layout:
  - [ ] Top app bar
  - [ ] Profile section:
    - [ ] Avatar placeholder (initials)
    - [ ] Email address
    - [ ] Member since date
  - [ ] Stats section:
    - [ ] Total credits used
    - [ ] Total images generated
    - [ ] Total edits created
  - [ ] Actions:
    - [ ] "Purchase Credits" button → Navigate to Pricing
    - [ ] "View Edit History" button → Navigate to History
    - [ ] "Terms of Service" link
    - [ ] "Privacy Policy" link
    - [ ] "Sign Out" button
- [ ] Sign out flow:
  - [ ] Confirmation dialog: "Are you sure you want to sign out?"
  - [ ] Call `authManager.signOut()`
  - [ ] Clear session storage
  - [ ] Navigate to Auth screen

**Success Criteria:**
- ✅ User profile data loads and displays
- ✅ Stats are accurate
- ✅ Purchase credits navigates to pricing
- ✅ Sign out clears session and returns to auth
- ✅ Terms and Privacy links open web pages

---

### 4.3 Pricing/Credits Screen

**Priority:** HIGH (Monetization)
**Estimated Time:** 1.5 weeks

**Tasks:**
- [ ] Create `screens/pricing/PricingScreen.kt`
- [ ] Create `screens/pricing/PricingViewModel.kt`
- [ ] Define credit packages (coordinate with backend/Stripe):
  - **Starter:** 10 credits - $4.99
  - **Plus:** 25 credits - $9.99
  - **Pro:** 50 credits - $14.99
  - **Ultimate:** 100 credits - $24.99
- [ ] Implement UI layout:
  - [ ] Top app bar with current credit balance
  - [ ] "Get More Credits" headline
  - [ ] Grid of credit package cards (2x2 or vertical list)
  - [ ] Each card shows: credit amount, price, best value badge
  - [ ] "Purchase" button on each card
  - [ ] "Powered by Stripe" badge
- [ ] Stripe integration:
  - [ ] Call `/stripe/create-checkout-session` with `priceId`
  - [ ] Open checkout URL in in-app browser (WebView/SFSafariViewController)
  - [ ] Handle success/cancel URLs
  - [ ] Refresh credit balance after successful payment
- [ ] Loading and error states

**API Endpoints:**
- `POST /stripe/create-checkout-session`
  - Request: `{ priceId, successUrl, cancelUrl }`
  - Response: `{ sessionId, url }`
- Payment confirmation handled by backend webhook

**Platform-Specific:**
- [ ] Android: Open checkout URL in Custom Tabs
- [ ] iOS: Open checkout URL in SFSafariViewController
- [ ] Deep link handling for success/cancel URLs

**Success Criteria:**
- ✅ Credit packages display with prices
- ✅ Tapping "Purchase" opens Stripe checkout
- ✅ Successful payment updates credit balance
- ✅ Cancel returns to pricing screen
- ✅ Error handling for failed payments

---

### 4.4 Error Handling & Retry Logic

**Priority:** HIGH
**Estimated Time:** 3 days

**Tasks:**
- [ ] Implement global error handling strategy
- [ ] Network error detection:
  - [ ] No internet connection
  - [ ] Timeout errors
  - [ ] Server unavailable (5xx)
- [ ] API error handling:
  - [ ] 401 Unauthorized → Sign out and show message
  - [ ] 402 Insufficient Credits → Show pricing screen with message
  - [ ] 429 Rate Limit → Show retry countdown timer
  - [ ] 500 Server Error → Show retry option
- [ ] Retry mechanism:
  - [ ] Exponential backoff (1s, 2s, 4s)
  - [ ] Max 3 retries
  - [ ] Show retry button on failure
- [ ] User-friendly error messages:
  - [ ] Replace technical errors with friendly text
  - [ ] Provide actionable solutions
  - [ ] Use Material 3 Snackbar for transient errors
  - [ ] Use full-screen error states for critical failures

**Error Message Examples:**
- ❌ "ClientRequestException: 402"
- ✅ "Insufficient credits. You need 5 more credits to generate these images. Purchase credits?"

**Success Criteria:**
- ✅ All API errors have user-friendly messages
- ✅ Network errors show retry options
- ✅ 401 errors automatically sign user out
- ✅ 402 errors navigate to pricing
- ✅ Retry mechanism works with exponential backoff

---

### 4.5 Download & Share Functionality

**Priority:** MEDIUM
**Estimated Time:** 3 days

**Tasks:**
- [ ] Implement image download to device (started in 3.6)
- [ ] Implement share functionality (started in 3.6)
- [ ] Add "Download All" option in Result Gallery
- [ ] Add share text customization
- [ ] Handle permissions gracefully:
  - [ ] Show rationale dialog before requesting
  - [ ] Handle permission denial with fallback
  - [ ] Show "Go to Settings" option if permanently denied

**Platform-Specific:**
- [ ] Android: `WRITE_EXTERNAL_STORAGE` permission handling
- [ ] iOS: Photo library permission handling
- [ ] Android: MediaStore API for saving to gallery
- [ ] iOS: PHPhotoLibrary API for saving to camera roll

**Success Criteria:**
- ✅ Single image download works
- ✅ "Download All" downloads all images sequentially
- ✅ Share opens native share sheet with image + text
- ✅ Permissions are requested properly
- ✅ Permission denial is handled gracefully

---

### Phase 4 Summary

**Total Estimated Time:** 4-5 weeks
**Deliverables:**
- ✅ Edit history with grid/timeline views
- ✅ Profile screen with stats and sign out
- ✅ Pricing/Credits screen with Stripe integration
- ✅ Comprehensive error handling and retry logic
- ✅ Download and share functionality

**After Phase 4:**
- App has all core features of the web version
- **Ready for internal beta testing**

---

## ⏳ Phase 5: Platform Testing & Polish

**Status:** ⏳ Pending
**Priority:** MEDIUM
**Estimated Duration:** 2-3 weeks

### 5.1 Android Testing

**Priority:** MEDIUM
**Estimated Time:** 1 week

**Tasks:**
- [ ] Test on various Android devices:
  - [ ] Pixel phones (latest Android)
  - [ ] Samsung Galaxy (OneUI)
  - [ ] Older devices (Android 7-9)
- [ ] Test on various screen sizes:
  - [ ] Small phones (<5.5")
  - [ ] Large phones (>6.5")
  - [ ] Tablets
- [ ] Test on different Android versions:
  - [ ] Android 7 (API 24) - Minimum SDK
  - [ ] Android 10 (API 29) - Scoped storage changes
  - [ ] Android 14 (API 34) - Latest stable
  - [ ] Android 15 (API 35) - Target SDK
- [ ] Fix platform-specific issues:
  - [ ] Layout issues on different screen sizes
  - [ ] Permission handling for different API levels
  - [ ] Back button behavior
  - [ ] Navigation gestures
- [ ] Performance optimization:
  - [ ] Reduce APK size
  - [ ] Optimize image loading
  - [ ] Reduce memory usage
- [ ] Accessibility:
  - [ ] Content descriptions
  - [ ] TalkBack support
  - [ ] Font scaling

**Success Criteria:**
- ✅ App works on Android 7-15
- ✅ No layout issues on various screen sizes
- ✅ Permissions work correctly on all API levels
- ✅ Performance is smooth on mid-range devices
- ✅ Accessibility score passes

---

### 5.2 iOS Testing

**Priority:** MEDIUM
**Estimated Time:** 1 week

**Tasks:**
- [ ] Test on various iOS devices:
  - [ ] iPhone SE (small screen)
  - [ ] iPhone 15 Pro (latest)
  - [ ] iPad (tablet layout)
- [ ] Test on different iOS versions:
  - [ ] iOS 15 (minimum deployment target)
  - [ ] iOS 16
  - [ ] iOS 17
  - [ ] iOS 18 (latest)
- [ ] Fix platform-specific issues:
  - [ ] Safe area insets (notch, dynamic island)
  - [ ] Layout issues on different screen sizes
  - [ ] Permission handling
  - [ ] Navigation gestures
- [ ] Performance optimization:
  - [ ] Reduce IPA size
  - [ ] Optimize image loading
  - [ ] Reduce memory usage
- [ ] Accessibility:
  - [ ] VoiceOver support
  - [ ] Dynamic Type support

**Success Criteria:**
- ✅ App works on iOS 15-18
- ✅ No layout issues on various screen sizes
- ✅ Safe area insets are handled correctly
- ✅ Permissions work correctly
- ✅ Performance is smooth on older devices
- ✅ Accessibility score passes

---

### 5.3 App Icons & Splash Screens

**Priority:** MEDIUM
**Estimated Time:** 3 days

**Tasks:**
- [ ] Design app icon (1024x1024)
- [ ] Generate Android adaptive icons:
  - [ ] Foreground layer
  - [ ] Background layer
  - [ ] All density buckets (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)
- [ ] Generate iOS icon set:
  - [ ] All required sizes (20pt to 1024pt)
  - [ ] App Store icon (1024x1024)
- [ ] Design splash screen:
  - [ ] Android: Use `core-splashscreen` library
  - [ ] iOS: Use LaunchScreen.storyboard
- [ ] Test splash screen on both platforms

**Asset Requirements:**
- **App Icon:** 1024x1024px, no transparency, no rounded corners
- **Android Adaptive Icon:** Foreground + Background layers
- **iOS Icon:** Multiple sizes (see `APP_ICON_TODO.md`)

**Success Criteria:**
- ✅ App icon looks professional and recognizable
- ✅ Adaptive icons work on Android
- ✅ iOS icons pass App Store validation
- ✅ Splash screen displays correctly on both platforms

---

### 5.4 Performance Optimization

**Priority:** MEDIUM
**Estimated Time:** 3 days

**Tasks:**
- [ ] Profile app performance:
  - [ ] Identify slow screens
  - [ ] Identify memory leaks
  - [ ] Identify excessive API calls
- [ ] Optimize image loading:
  - [ ] Use Coil's memory/disk cache
  - [ ] Downscale images to fit screen
  - [ ] Lazy load images in lists
- [ ] Optimize API calls:
  - [ ] Cache hero list locally
  - [ ] Avoid redundant API calls
  - [ ] Implement request deduplication
- [ ] Reduce bundle size:
  - [ ] Remove unused dependencies
  - [ ] Enable R8/ProGuard (Android)
  - [ ] Enable bitcode (iOS)
- [ ] Improve startup time:
  - [ ] Lazy initialize dependencies
  - [ ] Defer non-critical work

**Success Criteria:**
- ✅ App startup time <2 seconds
- ✅ Screens load in <1 second
- ✅ No memory leaks
- ✅ Bundle size <50MB

---

### Phase 5 Summary

**Total Estimated Time:** 2-3 weeks
**Deliverables:**
- ✅ Android testing across devices and versions
- ✅ iOS testing across devices and versions
- ✅ App icons and splash screens
- ✅ Performance optimizations

**After Phase 5:**
- App is polished and ready for beta release
- **Ready for external beta testing (TestFlight/Play Store Beta)**

---

## ⏳ Phase 6: Optimization & Advanced Features

**Status:** ⏳ Pending
**Priority:** LOW (Optional)
**Estimated Duration:** 2-3 weeks

### 6.1 Local Caching with Room Database

**Priority:** LOW
**Estimated Time:** 1 week

**Tasks:**
- [ ] Create Room entities:
  - [ ] `HeroEntity` - Cache heroes locally
  - [ ] `EditHistoryEntity` - Cache edit history
  - [ ] `UserEntity` - Cache user profile
- [ ] Create DAOs for each entity
- [ ] Implement repository caching strategy:
  - [ ] Fetch from local DB first
  - [ ] Fetch from API if cache is stale or empty
  - [ ] Update cache with API response
- [ ] Implement cache invalidation:
  - [ ] Expire heroes cache after 7 days
  - [ ] Expire edit history cache after 1 hour
  - [ ] Expire user profile cache after 5 minutes
- [ ] Offline viewing:
  - [ ] Show cached data when offline
  - [ ] Show "Offline" indicator
  - [ ] Prevent actions that require network

**Benefits:**
- ✅ Faster loading (no API call needed)
- ✅ Offline viewing of past data
- ✅ Reduced API calls (saves backend costs)

---

### 6.2 Analytics & Error Monitoring

**Priority:** LOW
**Estimated Time:** 3 days

**Tasks:**
- [ ] Integrate Sentry for error tracking (like web app)
- [ ] Track events:
  - [ ] User signup/login
  - [ ] Hero selection
  - [ ] Image upload
  - [ ] Image generation success/failure
  - [ ] Credit purchase
- [ ] Track screens:
  - [ ] Screen views with navigation
  - [ ] Time spent on each screen
- [ ] Track errors:
  - [ ] API errors
  - [ ] Crash reports
  - [ ] ANRs (Android)
- [ ] Configure Sentry:
  - [ ] Separate projects for Android and iOS
  - [ ] Filter out PII (emails, user IDs)
  - [ ] Set up alerts for critical errors

**Benefits:**
- ✅ Understand user behavior
- ✅ Track conversion funnel
- ✅ Monitor errors and crashes
- ✅ Improve app based on data

---

### 6.3 Advanced Features (Nice-to-Have)

**Priority:** LOW
**Estimated Time:** 1 week

**Tasks:**
- [ ] Push notifications:
  - [ ] Notify when credits are low
  - [ ] Notify when generation is complete (if backgrounded)
  - [ ] Promotional notifications
- [ ] In-app image editing:
  - [ ] Crop tool
  - [ ] Rotate tool
  - [ ] Brightness/contrast adjustment
- [ ] Favorites/Collections:
  - [ ] Mark favorite heroes
  - [ ] Create collections of edits
  - [ ] Share collections
- [ ] Social features:
  - [ ] Leaderboard (most edits, most creative)
  - [ ] Community gallery
  - [ ] Comment on edits

**Benefits:**
- ✅ Increased user engagement
- ✅ Better user experience
- ✅ Differentiation from web app

---

### Phase 6 Summary

**Total Estimated Time:** 2-3 weeks
**Deliverables:**
- ✅ Local caching with Room
- ✅ Analytics and error monitoring
- ✅ Advanced features (optional)

**After Phase 6:**
- App has advanced features beyond the web version
- **Ready for production release**

---

## 📝 API Integration Reference

**Base URL:** `https://api.superpets.fun`
**Authentication:** Bearer token (Supabase JWT)

### Endpoints

| Endpoint | Method | Auth | Description |
|----------|--------|------|-------------|
| `/heroes` | GET | No | Get all 29 heroes |
| `/user/profile` | GET | Yes | Get user profile (auto-creates) |
| `/user/credits` | GET | Yes | Get credit balance |
| `/user/transactions` | GET | Yes | Get transaction history |
| `/user/edits` | GET | Yes | Get edit history |
| `/nano-banana/upload-and-edit` | POST | Yes | Upload and edit images |
| `/stripe/create-checkout-session` | POST | Yes | Create payment session |

### Rate Limits

- **Image generation:** 5 requests/minute per user
- **User profile:** 30 requests/minute per user
- **Public endpoints:** 60 requests/minute per IP

### Error Codes

| Code | Meaning | Client Action |
|------|---------|---------------|
| 401 | Unauthorized | Sign out and show login |
| 402 | Insufficient credits | Show pricing screen |
| 429 | Rate limit exceeded | Show retry countdown |
| 500 | Server error | Show retry option |

---

## 🎨 Design System Reference

**Theme:** Material 3
**Location:** `ui/theme/`

### Colors
- **Primary:** Purple gradient (#8B5CF6 to #6366F1)
- **Secondary:** Pink accent (#EC4899)
- **Background:** White (#FFFFFF) / Dark (#1F2937)
- **Surface:** Gray-50 (#F9FAFB) / Dark-800 (#1F2937)

### Typography
- **Display:** 36sp, Bold
- **Headline:** 28sp, Bold
- **Title:** 20sp, SemiBold
- **Body:** 16sp, Regular
- **Caption:** 12sp, Regular

### Spacing
- **xs:** 4dp
- **sm:** 8dp
- **md:** 16dp
- **lg:** 24dp
- **xl:** 32dp

### Components
All reusable components documented in `COMPONENT_LIBRARY.md`

---

## 🚀 Deployment Checklist

### Before Production Release

**App Store / Play Store Setup:**
- [ ] Create developer accounts (Apple, Google)
- [ ] Prepare app store listings:
  - [ ] App name, description, keywords
  - [ ] Screenshots (all required sizes)
  - [ ] Privacy policy URL
  - [ ] Support URL
  - [ ] Marketing assets
- [ ] Configure app signing:
  - [ ] Android: Upload key to Play Console
  - [ ] iOS: Create certificates and provisioning profiles
- [ ] Set up TestFlight (iOS) and Play Store Beta (Android)

**Backend Configuration:**
- [ ] Ensure CORS allows mobile app requests
- [ ] Verify rate limits are appropriate
- [ ] Test Stripe webhook integration
- [ ] Monitor backend performance

**Testing:**
- [ ] Internal testing (all features)
- [ ] Beta testing (external users)
- [ ] Load testing (simulate 100+ concurrent users)
- [ ] Security testing (API endpoints, auth flow)

**Legal:**
- [ ] Terms of Service
- [ ] Privacy Policy
- [ ] GDPR compliance (if applicable)
- [ ] App Store compliance (age ratings, content warnings)

**Monitoring:**
- [ ] Set up Sentry for error tracking
- [ ] Set up analytics
- [ ] Set up crash reporting
- [ ] Configure alerts for critical errors

---

## 📊 Success Metrics

**Technical Metrics:**
- App startup time: <2 seconds
- Screen load time: <1 second
- Crash rate: <0.1%
- API error rate: <1%

**Business Metrics:**
- User signups
- Daily/Monthly active users
- Images generated per user
- Credit purchases
- Retention rate (Day 1, Day 7, Day 30)

**User Experience Metrics:**
- Average time to first generation
- Completion rate (signup → first generation)
- Feature adoption (hero selection, share, download)
- User ratings (App Store, Play Store)

---

## 🔗 Related Documentation

- `MOBILE_TODO.md` - Detailed task checklist
- `PROJECT_STATE.md` - Overall project status
- `CLAUDE.md` - Project overview and backend details
- `DESIGN_TOKENS.md` - Design system specification
- `COMPONENT_LIBRARY.md` - UI component catalog
- `INFRASTRUCTURE_SETUP.md` - Infrastructure documentation
- `APP_ICON_TODO.md` - App icon requirements
- `ASSETS_README.md` - Asset management guide

---

## 🤝 Contributing

**For AI Assistants:**
When working on the mobile app:
1. Read this roadmap first to understand current phase
2. Focus on tasks in the current phase
3. Use existing components from the component library
4. Follow Material 3 design guidelines
5. Update this roadmap after completing major milestones

**For Developers:**
1. Pick tasks from the current phase
2. Create feature branches (e.g., `feature/hero-selection`)
3. Test on both Android and iOS before merging
4. Update this roadmap with progress
5. Mark tasks complete with ✅

---

**Last Updated:** October 14, 2025
**Next Milestone:** Complete Phase 3 (Core Features)
**Target:** MVP ready for internal testing by end of Phase 4
