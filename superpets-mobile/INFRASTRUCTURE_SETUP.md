# Superpets Mobile - Core Infrastructure Setup Complete ✅

## Phase 1: Core Infrastructure - COMPLETED

This document describes the core infrastructure that has been implemented for the Superpets mobile app.

---

## What Was Built

### 1. Dependencies Added

Updated `gradle/libs.versions.toml` and `composeApp/build.gradle.kts` with:

- **Ktor Client Extensions:**
  - `ktor-client-auth` - Bearer token authentication
  - `ktor-client-logging` - HTTP request/response logging

- **Supabase KMP SDK:**
  - `supabase-gotrue` (v3.0.6) - Authentication for Kotlin Multiplatform

### 2. Data Models (`composeApp/src/commonMain/kotlin/fun/superpets/mobile/data/models/`)

Created all shared data models matching the backend API:

- **User.kt** - User profile model
  - `uid`, `email`, `credits`, `isAdmin`, `createdAt`

- **Hero.kt** - Hero and HeroesResponse models
  - `id`, `name`, `category`, `identity`, `scenes`

- **EditHistory.kt** - Edit history and response models
  - `id`, `userId`, `prompt`, `inputImages`, `outputImages`, `creditsCost`, `timestamp`

- **CreditTransaction.kt** - Credit transaction models
  - `id`, `userId`, `amount`, `type`, `description`, `timestamp`
  - `CreditsResponse`, `TransactionsResponse`

- **ApiModels.kt** - Request/response models for API calls
  - `EditImageRequest`, `EditImageResponse`
  - `ApiError`, `CheckoutSessionRequest`, `CheckoutSessionResponse`

### 3. Authentication Layer (`composeApp/src/commonMain/kotlin/fun/superpets/mobile/data/auth/`)

- **AuthTokenProvider.kt** - Interface for providing auth tokens
  - `getToken()` - Get current JWT token
  - `isAuthenticated()` - Check auth status

- **AuthManager.kt** - Supabase Auth implementation
  - Implements `AuthTokenProvider`
  - Methods: `signUp()`, `signIn()`, `signOut()`, `resetPassword()`
  - Exposes `authState: StateFlow<AuthState>` for observing auth changes
  - Uses Supabase GoTrue SDK for authentication

- **SupabaseConfig.kt** - Configuration constants
  - `SUPABASE_URL` - Project URL (needs to be configured)
  - `SUPABASE_ANON_KEY` - Public/anon key (needs to be configured)

### 4. Network Layer (`composeApp/src/commonMain/kotlin/fun/superpets/mobile/data/network/`)

- **HttpClientFactory.kt** - Creates configured Ktor HttpClient
  - Base URL: `https://api.superpets.fun`
  - Features:
    - JSON serialization (kotlinx.serialization)
    - Bearer token authentication (automatic token injection)
    - Request/response logging (via Napier)
    - Timeout configuration (60s for image generation)
    - Content negotiation

- **SuperpetsApiService.kt** - Complete API service implementation
  - All backend endpoints implemented:
    - `getHeroes()` - Get all heroes (public, no auth)
    - `getUserProfile()` - Get/create user profile
    - `getUserCredits()` - Get credit balance
    - `getTransactions()` - Get transaction history
    - `getEditHistory()` - Get edit history
    - `editImage()` - Edit images from URLs
    - `uploadAndEditImages()` - Upload and edit (multipart)
    - `createCheckoutSession()` - Stripe checkout
  - Error handling with `ApiException`
  - Upload progress tracking

### 5. Dependency Injection (`composeApp/src/commonMain/kotlin/fun/superpets/mobile/di/Koin.kt`)

Added **superpetsModule** to Koin configuration:

```kotlin
val superpetsModule = module {
    // Authentication
    single<AuthManager> { AuthManager() }
    single<AuthTokenProvider> { get<AuthManager>() }

    // HTTP Client configured with auth
    single<HttpClient>(named("superpets")) {
        HttpClientFactory.create(
            authTokenProvider = get<AuthTokenProvider>(),
            enableLogging = true
        )
    }

    // API Service
    single<SuperpetsApiService> {
        SuperpetsApiService(httpClient = get(named("superpets")))
    }
}
```

---

## Configuration Required

### 1. Supabase Credentials ✅ Already Configured

Supabase credentials are already configured in:
`composeApp/src/commonMain/kotlin/fun/superpets/mobile/data/auth/SupabaseConfig.kt`

```kotlin
object SupabaseConfig {
    const val SUPABASE_URL = "https://zrivjktyzllaevduydai.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

These credentials are shared with the web frontend and backend. Users authenticated on mobile will have the same accounts as on the web app.

### 2. Gradle Wrapper

The Gradle wrapper JAR is missing. Regenerate it:

```bash
cd superpets-mobile
gradle wrapper
```

Or open the project in Android Studio and it will regenerate automatically.

---

## How to Use

### Inject Dependencies in ViewModels

```kotlin
class MyViewModel(
    private val apiService: SuperpetsApiService,
    private val authManager: AuthManager
) : ViewModel() {

    init {
        // Observe auth state
        viewModelScope.launch {
            authManager.authState.collect { state ->
                when (state) {
                    is AuthState.Authenticated -> {
                        // User is logged in
                        loadUserData()
                    }
                    is AuthState.Unauthenticated -> {
                        // User is logged out
                    }
                    is AuthState.Loading -> {
                        // Auth status checking
                    }
                }
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            apiService.getUserProfile().fold(
                onSuccess = { user ->
                    // Handle success
                },
                onFailure = { error ->
                    // Handle error
                }
            )
        }
    }
}
```

### Authentication Examples

```kotlin
// Sign up
authManager.signUp(email, password).fold(
    onSuccess = { /* Navigate to home */ },
    onFailure = { error -> /* Show error */ }
)

// Sign in
authManager.signIn(email, password).fold(
    onSuccess = { /* Navigate to home */ },
    onFailure = { error -> /* Show error */ }
)

// Sign out
authManager.signOut()

// Reset password
authManager.resetPassword(email)
```

### API Service Examples

```kotlin
// Get heroes (no auth required)
apiService.getHeroes().fold(
    onSuccess = { response ->
        val heroes = response.heroes
    },
    onFailure = { error ->
        // Handle error
    }
)

// Get user profile (requires auth)
apiService.getUserProfile().fold(
    onSuccess = { user ->
        println("Credits: ${user.credits}")
    },
    onFailure = { error ->
        // Handle unauthorized, etc.
    }
)

// Upload and edit images
apiService.uploadAndEditImages(
    imageData = listOf(imageBytes),
    heroId = "superman",
    numImages = 3
).fold(
    onSuccess = { response ->
        val outputImages = response.outputs
    },
    onFailure = { error ->
        // Handle insufficient credits, etc.
    }
)
```

---

## Architecture Overview

```
┌─────────────────────────────────────────┐
│            UI Layer (Compose)           │
│  Screens, ViewModels, Navigation        │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         Business Logic Layer            │
│  AuthManager, SuperpetsApiService       │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│          Network Layer                  │
│  HttpClient, Authentication             │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│     Superpets Backend (Railway)          │
│  https://api.superpets.fun        │
└─────────────────────────────────────────┘
```

---

## Error Handling

The `SuperpetsApiService` includes comprehensive error handling:

- **401 Unauthorized** → "Unauthorized. Please log in again."
- **402 Payment Required** → "Insufficient credits. Please purchase more credits."
- **429 Too Many Requests** → "Rate limit exceeded. Please try again later."
- **5xx Server Errors** → "Server error. Please try again later."
- **Network Errors** → "Network error: <details>"

All errors are wrapped in `ApiException` with status code and message.

---

## Testing

Once the Gradle wrapper is regenerated, you can build and test:

```bash
# Build Android
./gradlew :composeApp:assembleDebug

# Build iOS (requires Xcode)
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

# Run tests
./gradlew :composeApp:test
```

---

## Next Steps (Phase 2)

Now that core infrastructure is complete, you can proceed with:

1. **Authentication Screens** (Login, Signup)
   - Use `AuthManager` for sign in/up/out
   - Observe `authState` for navigation
   - Handle form validation and errors

2. **Landing/Home Screen**
   - Welcome screen with app overview
   - Quick stats display
   - Navigate to hero selection

3. **Navigation Setup**
   - Set up NavHost with authentication routing
   - Protected routes that require auth
   - Deep linking support

See `MOBILE_TODO.md` for complete roadmap.

---

## Summary

✅ Phase 1 is **100% complete**:
- ✅ Ktor HTTP client configured
- ✅ Bearer token authentication interceptor
- ✅ Supabase Auth SDK integrated
- ✅ All data models created
- ✅ Complete API service layer
- ✅ Dependency injection configured

**Status:** ✅ Fully configured and ready for Phase 2 (Authentication & Navigation)!
