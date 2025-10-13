# Superpets Mobile - API Client Configuration

## ✅ Configuration Complete

The Ktor HTTP client is fully configured and connected to the Superpets backend API at `https://api.superpets.fun`.

## Architecture Overview

```
┌──────────────────┐
│   ViewModel      │ ← Business logic, UI state management
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│   Repository     │ ← Data layer, caching, transformations
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│   ApiService     │ ← REST API calls, error handling
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  HttpClient      │ ← Ktor client with auth, logging, serialization
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│  Backend API     │ ← https://api.superpets.fun
└──────────────────┘
```

## What's Configured

### 1. **HttpClientFactory** (`HttpClientFactory.kt`)

✅ **Base URL**: `https://api.superpets.fun`
✅ **Authentication**: Bearer token from Supabase Auth
✅ **Serialization**: JSON with kotlinx.serialization
✅ **Logging**: Napier integration for debugging
✅ **Timeouts**:
- Request timeout: 60 seconds (for image generation)
- Connect timeout: 30 seconds
- Socket timeout: 60 seconds

✅ **Features**:
- Automatic auth token injection via `AuthTokenProvider`
- Content negotiation (JSON)
- Pretty print JSON in debug mode
- Lenient parsing, ignores unknown keys

### 2. **SuperpetsApiService** (`SuperpetsApiService.kt`)

Provides all REST API endpoints:

#### Public Endpoints (No Auth Required)
- `getHeroes()` - Get all available heroes

#### Authenticated Endpoints (Require Bearer Token)
- `getUserProfile()` - Get user profile (auto-creates with 5 credits)
- `getUserCredits()` - Get credit balance
- `getTransactions()` - Get transaction history
- `getEditHistory()` - Get edit history
- `editImage()` - Edit images from URLs
- `uploadAndEditImages()` - Upload and edit (multipart/form-data)
- `createCheckoutSession()` - Create Stripe checkout for credits

✅ **Error Handling**:
- Automatic retry logic
- 401 Unauthorized → "Please log in again"
- 402 Payment Required → "Insufficient credits"
- 429 Too Many Requests → "Rate limit exceeded"
- 5xx Server Errors → "Server error. Please try again"
- Network errors → Custom error messages

### 3. **AuthManager** (`AuthManager.kt`)

Integrates Supabase Auth for authentication:

✅ **Features**:
- Email/password signup and login
- JWT token management
- Session persistence
- Auth state flow (Loading, Authenticated, Unauthenticated)
- Password reset
- Automatic token refresh

✅ **Token Provider**:
- Implements `AuthTokenProvider` interface
- Automatically injects tokens into HTTP requests
- Used by `HttpClientFactory` for bearer auth

### 4. **SuperpetsRepository** (`SuperpetsRepository.kt`)

Clean architecture repository layer:

✅ **Benefits**:
- Simplified ViewModel code
- Business logic centralization
- Logging and error handling
- Data transformation
- Easy to test and mock

### 5. **Data Models** (`models/`)

All API models are defined with kotlinx.serialization:

✅ **Models**:
- `Hero` - Hero definition with id, name, category, identity, scenes
- `User` - User profile with uid, email, credits, isAdmin
- `CreditTransaction` - Transaction history
- `EditHistory` - Edit history with prompts and images
- `EditImageRequest/Response` - Image editing requests/responses
- `CheckoutSessionRequest/Response` - Stripe payment sessions

### 6. **Dependency Injection** (`di/Koin.kt`)

Fully configured with Koin:

```kotlin
val superpetsModule = module {
    // Supabase Auth Manager
    single<AuthManager> { AuthManager(httpClientEngine = get()) }
    single<AuthTokenProvider> { get<AuthManager>() }

    // HTTP Client with auth
    single<HttpClient>(named("superpets")) {
        HttpClientFactory.create(
            authTokenProvider = get(),
            enableLogging = true
        )
    }

    // API Service
    single<SuperpetsApiService> {
        SuperpetsApiService(httpClient = get(named("superpets")))
    }

    // Repository
    single<SuperpetsRepository> {
        SuperpetsRepository(apiService = get())
    }
}
```

### 7. **Platform-Specific Engines**

✅ **Android**: OkHttp engine (`androidMain/di/PlatformModule.kt`)
✅ **iOS**: Darwin engine (`iosMain/di/PlatformModule.kt`)

Both configured and injected via Koin.

## Usage Examples

### Example 1: Fetch Heroes (Public Endpoint)

```kotlin
class HeroSelectionViewModel(
    private val repository: SuperpetsRepository
) : ViewModel() {

    private val _heroes = MutableStateFlow<List<Hero>>(emptyList())
    val heroes: StateFlow<List<Hero>> = _heroes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadHeroes() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getHeroes()
                .onSuccess { heroes ->
                    _heroes.value = heroes
                }
                .onFailure { error ->
                    // Handle error
                    Napier.e("Failed to load heroes", error)
                }
            _isLoading.value = false
        }
    }
}
```

### Example 2: Upload and Edit Images (Authenticated)

```kotlin
class EditorViewModel(
    private val repository: SuperpetsRepository
) : ViewModel() {

    private val _editResult = MutableStateFlow<EditImageResponse?>(null)
    val editResult: StateFlow<EditImageResponse?> = _editResult.asStateFlow()

    fun uploadAndEdit(
        imageBytes: List<ByteArray>,
        heroId: String,
        numImages: Int = 4
    ) {
        viewModelScope.launch {
            repository.uploadAndEditImages(imageBytes, heroId, numImages)
                .onSuccess { response ->
                    _editResult.value = response
                    // response.outputs contains list of generated image URLs
                }
                .onFailure { error ->
                    when {
                        error.message?.contains("Unauthorized") == true -> {
                            // Redirect to login
                        }
                        error.message?.contains("Insufficient credits") == true -> {
                            // Show purchase credits dialog
                        }
                        else -> {
                            // Show generic error
                        }
                    }
                }
        }
    }
}
```

### Example 3: Get User Profile and Credits

```kotlin
class ProfileViewModel(
    private val repository: SuperpetsRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            repository.getUserProfile()
                .onSuccess { user ->
                    _user.value = user
                    // user.credits, user.email, etc.
                }
                .onFailure { error ->
                    // Handle error
                }
        }
    }

    fun refreshCredits() {
        viewModelScope.launch {
            repository.getUserCredits()
                .onSuccess { credits ->
                    _user.value = _user.value?.copy(credits = credits)
                }
        }
    }
}
```

### Example 4: Authentication Flow

```kotlin
class AuthViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    val authState = authManager.authState

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            authManager.signUp(email, password)
                .onSuccess {
                    // Navigate to home screen
                }
                .onFailure { error ->
                    // Show error message
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authManager.signIn(email, password)
                .onSuccess {
                    // Navigate to home screen
                }
                .onFailure { error ->
                    // Show error message
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
                .onSuccess {
                    // Navigate to login screen
                }
        }
    }
}
```

## API Endpoints Reference

### Base URL
```
https://api.superpets.fun
```

### Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/heroes` | ❌ | Get all heroes |
| GET | `/user/profile` | ✅ | Get user profile |
| GET | `/user/credits` | ✅ | Get credit balance |
| GET | `/user/transactions` | ✅ | Get transaction history |
| GET | `/user/edits` | ✅ | Get edit history |
| POST | `/nano-banana/edit` | ✅ | Edit images from URLs |
| POST | `/nano-banana/upload-and-edit` | ✅ | Upload and edit |
| POST | `/stripe/create-checkout-session` | ✅ | Create payment checkout |

### Authentication

All authenticated endpoints require a Bearer token in the Authorization header:

```
Authorization: Bearer <supabase-jwt-token>
```

This is handled automatically by `HttpClientFactory` using `AuthTokenProvider`.

## Important Notes

### Image Upload Requirements

⚠️ **File Size**: Maximum 10MB per image
⚠️ **Compression Required**: Resize to 2048x2048 before upload
⚠️ **Allowed Types**: JPEG, PNG, WEBP, GIF

### Credit System

- New users get **5 free credits** automatically
- **1 credit per output image** (not per request)
  - Example: 4 images = 4 credits, 1 image = 1 credit
- Client-side validation required before API calls
- No refunds for failed requests

### Rate Limiting

- **Image generation**: 5 requests/minute per user
- **Profile endpoints**: 30 requests/minute per user
- **Public endpoints**: 60 requests/minute per IP

Returns HTTP 429 with `X-RateLimit-*` headers when exceeded.

## Testing

### Manual Testing

Run the app and test:
1. ✅ Login/Signup flow
2. ✅ Fetch heroes (no auth required)
3. ✅ Get user profile (should auto-create with 5 credits)
4. ✅ Upload and edit images
5. ✅ Check credit balance updates
6. ✅ View transaction and edit history

### Unit Testing

Mock the `SuperpetsRepository` in ViewModels:

```kotlin
@Test
fun `test load heroes success`() = runTest {
    val mockRepository = mockk<SuperpetsRepository>()
    coEvery { mockRepository.getHeroes() } returns Result.success(testHeroes)

    val viewModel = HeroSelectionViewModel(mockRepository)
    viewModel.loadHeroes()

    assertEquals(testHeroes, viewModel.heroes.value)
}
```

## Next Steps

Now that the API client is configured, you can:

1. ✅ **Build Authentication UI** - Login, signup, password reset screens
2. ✅ **Build Hero Selection UI** - Grid of heroes with categories
3. ✅ **Build Image Editor UI** - Upload, compress, preview, generate
4. ✅ **Build Profile UI** - User info, credits, transaction history
5. ✅ **Build Results UI** - Display generated images, save, share
6. ✅ **Add Error Handling UI** - Toast messages, dialogs
7. ✅ **Add Loading States** - Progress indicators during API calls

## Troubleshooting

### Issue: "Unauthorized" errors
**Solution**: Check if user is logged in and token is valid. Call `authManager.signIn()` again.

### Issue: "Insufficient credits" errors
**Solution**: Check credit balance before API calls. Show purchase credits UI.

### Issue: Image upload fails (file too large)
**Solution**: Compress images to 2048x2048 before upload. See image compression section in TODO list.

### Issue: Rate limit exceeded
**Solution**: Implement client-side rate limiting or show appropriate error message to user.

## Resources

- **Backend API**: https://api.superpets.fun
- **Supabase URL**: https://zrivjktyzllaevduydai.supabase.co
- **Ktor Documentation**: https://ktor.io/docs/client.html
- **Supabase Kotlin SDK**: https://github.com/supabase-community/supabase-kt

---

**Status**: ✅ **COMPLETE** - API client fully configured and ready to use!

Last updated: October 13, 2025
