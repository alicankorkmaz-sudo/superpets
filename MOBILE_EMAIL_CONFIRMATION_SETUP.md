# Mobile Email Confirmation Setup Guide

This guide explains how email confirmation works in the Superpets mobile app (Android & iOS) and how it's configured.

## Overview

As of October 14, 2025, the Superpets mobile app requires email confirmation for new user signups, matching the web frontend behavior. This adds security and ensures users have valid email addresses.

## How It Works

### 1. User Signup Flow
1. User fills out the signup screen with email and password
2. App calls `authManager.signUp()`
3. If email confirmation is enabled in Supabase:
   - User account is created but not yet confirmed
   - Supabase sends a confirmation email with a deep link
   - App shows "Check Your Email" screen with instructions
4. User clicks the confirmation link in their email
5. Link opens the mobile app via deep linking (`superpets://auth?...`)
6. App validates the tokens and logs the user in automatically
7. User is redirected to the main app screen

### 2. Login Attempts Before Confirmation
- If a user tries to login before confirming their email:
  - Supabase returns an error: "Email not confirmed"
  - App shows an error message asking them to check their email

## Implementation Details

### AuthManager Changes

**File:** `composeApp/src/commonMain/kotlin/com/superpets/mobile/data/auth/AuthManager.kt`

1. **Supabase Client Configuration**
   - Added `scheme = "superpets"` and `host = "auth"` to Auth plugin
   - This tells Supabase to generate deep links with `superpets://auth` scheme

2. **SignUpResult Sealed Class**
   ```kotlin
   sealed class SignUpResult {
       data object Authenticated : SignUpResult()
       data class ConfirmationRequired(val email: String) : SignUpResult()
   }
   ```
   - Differentiates between immediate authentication and pending confirmation

3. **Updated signUp() Method**
   - Returns `Result<SignUpResult>` instead of `Result<Unit>`
   - Detects if email confirmation is required by checking if session exists
   - Updates auth state accordingly

4. **New handleDeepLink() Method**
   ```kotlin
   suspend fun handleDeepLink(url: String): Result<Unit>
   ```
   - Parses deep link URL and extracts authentication tokens
   - Calls `supabaseClient.auth.handleDeeplinks(url)`
   - Updates auth state to authenticated if successful

### AuthViewModel Changes

**File:** `composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/AuthViewModel.kt`

1. **Updated signUp() Function**
   - Handles both `SignUpResult.Authenticated` and `SignUpResult.ConfirmationRequired`
   - Sets `confirmationPending` and `confirmationEmail` in UI state

2. **Updated SignupUiState**
   ```kotlin
   data class SignupUiState(
       val isLoading: Boolean = false,
       val error: String? = null,
       val confirmationPending: Boolean = false,
       val confirmationEmail: String? = null
   )
   ```

3. **Error Handling**
   - Already handles "Email not confirmed" errors via `getErrorMessage()`
   - Shows user-friendly message: "Please verify your email address"

### SignupScreen Changes

**File:** `composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/SignupScreen.kt`

1. **Conditional UI Rendering**
   - Shows `EmailConfirmationPendingScreen` when `uiState.confirmationPending == true`
   - Otherwise shows normal signup form

2. **New EmailConfirmationPendingScreen Composable**
   - Beautiful Material 3 UI with email icon
   - Displays user's email address
   - Shows clear instructions
   - Highlights that link will open the app automatically
   - "Back to Login" button for navigation

### Android Deep Link Configuration

**File:** `composeApp/src/androidMain/AndroidManifest.xml`

```xml
<intent-filter android:autoVerify="true">
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data
        android:scheme="superpets"
        android:host="auth" />
</intent-filter>
```

- `android:autoVerify="true"`: Enables App Links verification
- `singleTask` launch mode: Ensures only one instance of the activity
- Scheme: `superpets://`
- Host: `auth`
- Full deep link example: `superpets://auth?token=...&type=signup`

**File:** `composeApp/src/androidMain/kotlin/com/superpets/mobile/MainActivity.kt`

- Handles deep links in `onCreate()` and `onNewIntent()`
- Extracts URL from intent data
- Passes to AuthManager via `LaunchedEffect`
- Uses Koin to inject AuthManager

### iOS Deep Link Configuration

**File:** `iosApp/iosApp/Info.plist`

```xml
<key>CFBundleURLTypes</key>
<array>
    <dict>
        <key>CFBundleTypeRole</key>
        <string>Editor</string>
        <key>CFBundleURLName</key>
        <string>fun.superpets.mobile</string>
        <key>CFBundleURLSchemes</key>
        <array>
            <string>superpets</string>
        </array>
    </dict>
</array>
```

- Registers `superpets://` URL scheme
- Allows iOS to recognize and open the app for these links

**File:** `iosApp/iosApp/iOSApp.swift`

- Uses `.onOpenURL {}` modifier to handle deep links
- Checks for `superpets://auth` scheme and host
- Calls `DIHelperKt.getAuthManager()` to access Kotlin AuthManager
- Handles deep link via `authManager.handleDeepLink(url:)`

**File:** `composeApp/src/iosMain/kotlin/com/superpets/mobile/DIHelper.kt` (NEW)

- Kotlin object implementing `KoinComponent`
- Exposes `getAuthManager()` function for Swift code
- Bridges Swift and Kotlin for dependency injection

## Supabase Configuration

### Required Settings in Supabase Dashboard

1. **Enable Email Confirmation**
   - Go to: Authentication → Settings
   - Enable: "Enable email confirmations"

2. **Configure Redirect URLs**
   - Go to: Authentication → URL Configuration
   - Add these URLs to "Redirect URLs":

**Mobile Deep Links:**
```
superpets://auth
```

**For Testing (Optional):**
- You can also add web URLs for testing in browser/emulator:
```
http://localhost:3000/auth/callback
```

3. **Site URL**
   - Set to your production web URL: `https://superpets.fun`
   - This is used as fallback if deep link fails

### Email Template

The confirmation email from Supabase will contain a link like:
```
superpets://auth?token=...&type=signup
```

When clicked on mobile, this will:
1. Open the Superpets mobile app
2. Trigger the deep link handler
3. Authenticate the user automatically

## Testing

### Android Testing

1. **Build and Install:**
   ```bash
   cd superpets-mobile
   ./gradlew :composeApp:installDebug
   ```

2. **Test Signup:**
   - Open app
   - Go to signup screen
   - Enter email and password
   - Click "Create Account"
   - Verify "Check Your Email" screen appears

3. **Test Deep Link:**
   - Check your email on the same device
   - Click the confirmation link
   - App should open automatically
   - User should be logged in

4. **Manual Deep Link Testing (via ADB):**
   ```bash
   adb shell am start -W -a android.intent.action.VIEW \
   -d "superpets://auth?token=test&type=signup" \
   fun.superpets.mobile
   ```

### iOS Testing

1. **Build and Run:**
   - Open `superpets-mobile/iosApp/iosApp.xcodeproj` in Xcode
   - Select target device/simulator
   - Build and run (Cmd+R)

2. **Test Signup:**
   - Same flow as Android above

3. **Test Deep Link:**
   - Check email on same device
   - Click confirmation link
   - App should open automatically

4. **Manual Deep Link Testing (via Terminal):**
   ```bash
   xcrun simctl openurl booted "superpets://auth?token=test&type=signup"
   ```

## Troubleshooting

### Android Issues

**Deep link doesn't open the app:**
- Verify AndroidManifest.xml has correct intent filter
- Check that scheme is exactly "superpets" (lowercase)
- Ensure `android:launchMode="singleTask"` is set
- Check logs: `adb logcat | grep "Deep link"`

**App opens but nothing happens:**
- Check logs for errors in `AuthManager.handleDeepLink()`
- Verify Supabase configuration (scheme and host)
- Ensure AuthManager is properly injected in MainActivity

### iOS Issues

**Deep link doesn't open the app:**
- Verify Info.plist has CFBundleURLSchemes configured
- Check that scheme is exactly "superpets" (lowercase)
- Rebuild app after Info.plist changes

**App opens but crashes:**
- Check that DIHelper.kt is properly compiled for iOS
- Verify Koin is initialized in IOSAppInitializer
- Check Xcode console for error messages

### Email Not Received

- Check spam/junk folder
- Verify SMTP settings in Supabase dashboard
- Check Supabase logs for email sending errors
- Ensure email address is valid

### Token Validation Fails

- Check that redirect URL `superpets://auth` is configured in Supabase
- Verify tokens haven't expired (default: 1 hour)
- Check Supabase logs for token validation errors
- Ensure scheme and host match exactly in all configs

## Security Considerations

1. **Deep Link Validation**
   - Always validate deep link URL scheme and host
   - Never trust URL parameters without validation
   - Supabase handles token validation server-side

2. **Token Security**
   - Tokens are single-use and expire quickly
   - Supabase validates tokens on server before creating session
   - Sessions are stored securely (Keychain on iOS, EncryptedSharedPreferences on Android)

3. **App Link Verification (Android)**
   - `android:autoVerify="true"` enables App Links
   - Requires assetlinks.json file on your domain
   - Prevents other apps from intercepting deep links

## Files Changed

### Common (Shared Kotlin)
- `data/auth/AuthManager.kt` - Added SignUpResult, handleDeepLink()
- `screens/auth/AuthViewModel.kt` - Updated to handle confirmation state
- `screens/auth/SignupScreen.kt` - Added EmailConfirmationPendingScreen

### Android
- `androidMain/AndroidManifest.xml` - Added deep link intent filter
- `androidMain/kotlin/com/superpets/mobile/MainActivity.kt` - Added deep link handling

### iOS
- `iosApp/iosApp/Info.plist` - Added CFBundleURLTypes
- `iosApp/iosApp/iOSApp.swift` - Added .onOpenURL handler
- `iosMain/kotlin/com/superpets/mobile/DIHelper.kt` (NEW) - Koin bridge for Swift

## Next Steps

After implementing email confirmation, consider:

1. **Password Reset Flow** - Add "Forgot Password" functionality
2. **Resend Confirmation** - Add button to resend confirmation email
3. **Email Change** - Allow users to change their email (requires re-confirmation)
4. **Analytics** - Track signup funnel and confirmation rates
5. **A/B Testing** - Test different email templates for better conversion

## References

- [Supabase Auth Documentation](https://supabase.com/docs/guides/auth)
- [Supabase Kotlin Client](https://github.com/supabase-community/supabase-kt)
- [Android Deep Linking](https://developer.android.com/training/app-links)
- [iOS Universal Links](https://developer.apple.com/ios/universal-links/)
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
