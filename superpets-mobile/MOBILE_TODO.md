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

## Phase 2: Authentication & Navigation (Essential)

- [ ] **Implement authentication screens (Login, Signup)**
  - Login screen UI
  - Signup screen UI
  - Form validation
  - Error handling
  - Token storage

- [ ] **Create landing/home screen with app overview**
  - Onboarding flow
  - Navigation hub
  - Quick stats display
  - Recent generations preview

## Phase 3: Core Features (MVP)

- [ ] **Implement hero selection screen (29+ heroes grid)**
  - Fetch heroes from `/heroes` endpoint
  - Grid layout with tabs (Classic vs Unique)
  - Hero card components
  - Search/filter functionality

- [ ] **Build image picker with platform-specific implementations**
  - Android: Use ActivityResultContracts
  - iOS: Use PHPickerViewController
  - Camera capture option
  - Multiple image selection (1-10)

- [ ] **Implement image compression to 2048x2048 before upload**
  - Platform-specific compression utilities
  - Quality: JPEG 80-90%
  - Target: 1-3MB per image
  - Progress indicator

- [ ] **Create image editor screen with hero selection and generate button**
  - Image preview
  - Hero selection display
  - Output count slider (1-10)
  - Credit cost calculator
  - Generate button with validation

- [ ] **Implement credit display and management UI**
  - Credit balance in top bar
  - Real-time credit updates
  - Low credit warnings
  - Fetch from `/user/credits` endpoint

- [ ] **Build result gallery screen to display generated images**
  - Image carousel
  - Swipe navigation
  - Download/Share actions
  - Regenerate option

## Phase 4: User Experience (Important)

- [ ] **Implement edit history screen with past generations**
  - Fetch from `/user/edits` endpoint
  - Grid/Timeline view toggle
  - Pull to refresh
  - Filter options

- [ ] **Add pricing/credit purchase screen with Stripe integration**
  - Credit packages display
  - Stripe checkout session
  - Payment confirmation
  - Credit balance update

- [ ] **Implement error handling and loading states**
  - Network error handling
  - Insufficient credits error
  - Rate limiting (429) handling
  - Loading indicators
  - Retry mechanisms

- [ ] **Add image download/share functionality**
  - Save to device storage
  - Share to social media
  - Platform-specific permissions

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
