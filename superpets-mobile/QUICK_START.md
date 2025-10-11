# Superpets Mobile - Quick Start Guide

## Prerequisites

- Android Studio Hedgehog or newer
- JDK 11+
- Xcode 15+ (for iOS development)
- Kotlin 1.9.0+

---

## Setup Steps

### 1. Supabase Credentials ✅ Already Configured

Supabase credentials are already configured and shared with the web frontend:

- **Project:** `zrivjktyzllaevduydai`
- **URL:** `https://zrivjktyzllaevduydai.supabase.co`
- **Config file:** `composeApp/src/commonMain/kotlin/fun/superpets/mobile/data/auth/SupabaseConfig.kt`

No action needed! The app will authenticate users using the same Supabase project as the web app.

### 2. Regenerate Gradle Wrapper (if needed)

```bash
cd superpets-mobile
gradle wrapper
```

Or simply open the project in Android Studio - it will auto-regenerate.

### 3. Sync Gradle Dependencies

In Android Studio:
- File → Sync Project with Gradle Files
- Wait for dependencies to download

### 4. Run the App

**Android:**
- Select Android target in toolbar
- Click Run (▶️)

**iOS:**
- Select iOS target in toolbar
- Click Run (▶️)
- Or open `iosApp/iosApp.xcodeproj` in Xcode

---

## Project Structure

```
superpets-mobile/
├── composeApp/
│   └── src/
│       ├── commonMain/kotlin/fun/superpets/mobile/
│       │   ├── data/
│       │   │   ├── models/          # Data models (User, Hero, etc.)
│       │   │   ├── network/         # API service, HTTP client
│       │   │   └── auth/            # Auth manager, Supabase config
│       │   ├── di/                  # Koin dependency injection
│       │   ├── screens/             # UI screens (to be implemented)
│       │   └── navigation/          # Navigation setup
│       ├── androidMain/             # Android-specific code
│       └── iosMain/                 # iOS-specific code
├── MOBILE_TODO.md                   # Development roadmap
├── INFRASTRUCTURE_SETUP.md          # Infrastructure documentation
└── QUICK_START.md                   # This file
```

---

## Using the Infrastructure

### Example: Authentication Flow

```kotlin
import org.koin.compose.koinInject

@Composable
fun LoginScreen() {
    val authManager: AuthManager = koinInject()
    val authState by authManager.authState.collectAsState()

    when (authState) {
        is AuthState.Authenticated -> {
            // Navigate to home
        }
        is AuthState.Unauthenticated -> {
            // Show login form
            LoginForm(onLogin = { email, password ->
                scope.launch {
                    authManager.signIn(email, password)
                }
            })
        }
        is AuthState.Loading -> {
            // Show loading indicator
        }
    }
}
```

### Example: API Calls

```kotlin
import org.koin.compose.koinInject

@Composable
fun HeroListScreen() {
    val apiService: SuperpetsApiService = koinInject()
    var heroes by remember { mutableStateOf<List<Hero>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        apiService.getHeroes().fold(
            onSuccess = { response ->
                heroes = response.heroes
                isLoading = false
            },
            onFailure = { error ->
                // Handle error
                isLoading = false
            }
        )
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        HeroGrid(heroes = heroes)
    }
}
```

### Example: Image Upload

```kotlin
suspend fun uploadAndGenerate(
    imageBytes: ByteArray,
    heroId: String,
    numImages: Int
): Result<EditImageResponse> {
    val apiService: SuperpetsApiService = koinInject()

    return apiService.uploadAndEditImages(
        imageData = listOf(imageBytes),
        heroId = heroId,
        numImages = numImages
    )
}
```

---

## Development Tips

### 1. Enable HTTP Logging

Logging is enabled by default in `HttpClientFactory`. Check logs with tag `HttpClient`:

```kotlin
// In Android Studio: Logcat filter
HttpClient

// You'll see request/response details
```

### 2. Handle Authentication Errors

```kotlin
apiService.getUserProfile().fold(
    onSuccess = { user -> /* ... */ },
    onFailure = { error ->
        if (error is ApiException) {
            when (error.statusCode) {
                401 -> {
                    // Unauthorized - sign out and show login
                    authManager.signOut()
                }
                402 -> {
                    // Insufficient credits - show pricing
                    navController.navigate("pricing")
                }
                429 -> {
                    // Rate limited - show retry later message
                }
            }
        }
    }
)
```

### 3. Observe Auth State

```kotlin
class MyViewModel(
    private val authManager: AuthManager
) : ViewModel() {

    val isAuthenticated = authManager.authState
        .map { it is AuthState.Authenticated }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)
}
```

### 4. Credit Balance Updates

```kotlin
// Get credits
apiService.getUserCredits().fold(
    onSuccess = { response ->
        val credits = response.credits
        // Update UI
    },
    onFailure = { /* handle error */ }
)

// Or get full profile (includes credits)
apiService.getUserProfile().fold(
    onSuccess = { user ->
        val credits = user.credits
    },
    onFailure = { /* handle error */ }
)
```

---

## Testing

### Unit Tests

```bash
./gradlew :composeApp:test
```

### Android Build

```bash
./gradlew :composeApp:assembleDebug
```

### iOS Build

```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

---

## Troubleshooting

### "Could not find or load main class GradleWrapperMain"

Regenerate the Gradle wrapper:

```bash
gradle wrapper
```

### "SupabaseClient initialization failed"

Check your credentials in `SupabaseConfig.kt`:
- URL should start with `https://` and end with `.supabase.co`
- Anon key should be a long JWT-like string

### "Network error: Connection refused"

Check that:
1. Backend is running: https://superpets-backend-pipp.onrender.com
2. Device/emulator has internet connection
3. If using local backend, update `BASE_URL` in `HttpClientFactory.kt`

### "Unauthorized" even after login

The JWT token might be expired. Check:
1. Token is being sent in requests (check logs)
2. Supabase session is valid
3. Backend JWT_SECRET matches Supabase project

---

## Next Steps

1. **Configure Supabase credentials** (see step 1 above)
2. **Start Phase 2:** Implement authentication screens
3. **Review:** `MOBILE_TODO.md` for full roadmap
4. **Read:** `INFRASTRUCTURE_SETUP.md` for architecture details

---

## Resources

- [Compose Multiplatform Docs](https://www.jetbrains.com/compose-multiplatform/)
- [Ktor Client Docs](https://ktor.io/docs/client.html)
- [Supabase Kotlin Docs](https://supabase.com/docs/reference/kotlin/introduction)
- [Koin Docs](https://insert-koin.io/docs/reference/koin-mp/kmp)

---

## Support

For issues or questions:
1. Check `INFRASTRUCTURE_SETUP.md` for detailed docs
2. Review backend API docs in `../CLAUDE.md`
3. Test backend endpoints at: https://superpets-backend-pipp.onrender.com

**Backend API Health Check:**
```bash
curl https://superpets-backend-pipp.onrender.com/
```

Should return: `{"message":"Superpets API is running"}`
