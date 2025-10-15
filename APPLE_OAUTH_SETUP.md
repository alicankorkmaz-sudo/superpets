# Apple OAuth Setup Guide

This guide explains how to configure Sign in with Apple for both the web and mobile apps.

## Overview

Apple Sign-In has been implemented in:
- ✅ **Web App** (React): [LoginForm.tsx](superpets-web/src/components/Auth/LoginForm.tsx) and [SignupForm.tsx](superpets-web/src/components/Auth/SignupForm.tsx)
- ✅ **Mobile App** (Compose Multiplatform): [LoginScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/LoginScreen.kt) and [SignupScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/SignupScreen.kt)

## Prerequisites

1. **Apple Developer Account** (Required - $99/year membership)
2. Supabase project (already set up)
3. Access to your Supabase dashboard
4. For iOS app: Valid Bundle ID registered in Apple Developer portal

**Important:** Unlike Google OAuth, Apple Sign-In **requires** an active Apple Developer Program membership. You cannot test Apple OAuth without this.

## Step 1: Create App ID in Apple Developer Portal

1. Go to [Apple Developer Portal](https://developer.apple.com/account/)
2. Navigate to **Certificates, Identifiers & Profiles**
3. Click **Identifiers** in the sidebar
4. Click the **+** button to create a new identifier
5. Select **App IDs** and click **Continue**
6. Select **App** and click **Continue**
7. Fill in the details:
   - **Description**: Superpets
   - **Bundle ID**: `fun.superpets.mobile` (Explicit)
   - **Capabilities**: Enable **Sign in with Apple**
8. Click **Continue** and then **Register**

## Step 2: Create Services ID (for Web OAuth)

Apple OAuth on the web requires a separate Services ID:

1. In **Certificates, Identifiers & Profiles**, click **Identifiers**
2. Click the **+** button
3. Select **Services IDs** and click **Continue**
4. Fill in the details:
   - **Description**: Superpets Web
   - **Identifier**: `fun.superpets.web` (must be different from App ID)
5. Check **Sign in with Apple**
6. Click **Configure** next to Sign in with Apple
7. Configure the service:
   - **Primary App ID**: Select your Superpets App ID created in Step 1
   - **Web Domain**:
     - Add `superpets.fun`
     - Add `zrivjktyzllaevduydai.supabase.co` (your Supabase project domain)
   - **Return URLs**:
     - Add `https://zrivjktyzllaevduydai.supabase.co/auth/v1/callback`
     - Add `https://superpets.fun/auth/callback` (optional, for direct callbacks)
8. Click **Save**
9. Click **Continue** and then **Register**

**To find your exact Supabase callback URL:**
1. Go to Supabase Dashboard > Authentication > Providers
2. Scroll to Apple provider
3. Copy the "Callback URL (for OAuth)" shown

## Step 3: Create Private Key for Sign in with Apple

1. In **Certificates, Identifiers & Profiles**, click **Keys**
2. Click the **+** button
3. Fill in:
   - **Key Name**: Superpets Sign in with Apple Key
   - Check **Sign in with Apple**
4. Click **Configure** next to Sign in with Apple
5. Select your **Superpets App ID** as the Primary App ID
6. Click **Save**
7. Click **Continue** and then **Register**
8. **Download the key file** (.p8 file) - **Save it securely! You can only download it once!**
9. Note the **Key ID** (10 characters) - you'll need this for Supabase

## Step 4: Get Your Team ID

1. In the Apple Developer Portal, click your name in the top right
2. Go to **Membership**
3. Copy your **Team ID** (10 characters) - you'll need this for Supabase

## Step 5: Configure Supabase

1. Go to your [Supabase Dashboard](https://supabase.com/dashboard)
2. Navigate to **Authentication** > **Providers**
3. Find **Apple** in the list
4. Toggle **Enable Sign in with Apple** to ON
5. Fill in the required fields:
   - **Services ID**: `fun.superpets.web` (from Step 2)
   - **Team ID**: Your 10-character Team ID (from Step 4)
   - **Key ID**: Your 10-character Key ID (from Step 3)
   - **Secret Key**: Paste the entire contents of your downloaded .p8 file
     - Open the .p8 file in a text editor
     - Copy everything including the `-----BEGIN PRIVATE KEY-----` and `-----END PRIVATE KEY-----` lines
6. Click **Save**

## Step 6: Test Web App

### Development Testing

1. Start the web development server:
```bash
cd superpets-web
npm run dev
```

2. **Important:** Apple OAuth will **NOT work on localhost**
   - Apple requires HTTPS for OAuth
   - You must test on your production domain or use a tunneling service

### Production Testing

1. Deploy to production: `https://superpets.fun`
2. Navigate to Login or Signup
3. Click "Sign in with Apple" button
4. You should see Apple's OAuth consent screen
5. Sign in with your Apple ID
6. Choose whether to share or hide your email
7. After approving, you should be redirected back to Superpets and logged in

**Note:** On first sign-in, Apple gives you the option to hide your email. If you choose to hide it, Apple generates a relay email address that forwards to your actual email.

## Step 7: Configure Mobile App OAuth

### iOS Configuration

1. **Update Xcode Project**:
   - Open `iosApp/iosApp.xcodeproj` in Xcode
   - Select your target
   - Go to **Signing & Capabilities**
   - Click **+ Capability**
   - Add **Sign in with Apple**
   - Ensure your Bundle ID matches: `fun.superpets.mobile`

2. **Verify Team and Bundle ID**:
   - Make sure your Development Team is selected
   - Bundle Identifier should be `fun.superpets.mobile`
   - Sign in with Apple capability should show a checkmark

3. **Test on iOS**:
```bash
cd superpets-mobile
# Open in Xcode
open iosApp/iosApp.xcodeproj
# Run on simulator or device
```

### Android Configuration

Android apps can also use Sign in with Apple via web-based OAuth:

1. **No additional Android-specific configuration needed**
   - Supabase handles the OAuth flow via web browser
   - Uses the same Services ID as the web app

2. **Deep linking already configured** in `AuthManager.kt`:
```kotlin
scheme = "superpets"
host = "auth"
```

3. **Test on Android**:
```bash
cd superpets-mobile
./gradlew :composeApp:installDebug
```

## How It Works

### Web App Flow

1. User clicks "Sign in with Apple" button
2. `signInWithApple()` is called in [useAuth.ts](superpets-web/src/hooks/useAuth.ts)
3. Supabase SDK redirects user to Apple's OAuth consent screen
4. User approves with Face ID/Touch ID/password
5. Apple redirects back to `https://zrivjktyzllaevduydai.supabase.co/auth/v1/callback`
6. Supabase processes the OAuth token and creates session
7. User is redirected to `https://superpets.fun/auth/callback`
8. User is authenticated and logged in

### Mobile App Flow

**iOS:**
1. User taps "Sign in with Apple" button
2. `signInWithApple()` is called in [AuthViewModel.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/AuthViewModel.kt)
3. Native iOS Sign in with Apple sheet appears
4. User approves with Face ID/Touch ID
5. iOS returns credentials to the app
6. Supabase SDK exchanges credentials for session
7. User is authenticated and logged in

**Android:**
1. User taps "Sign in with Apple" button
2. System browser opens with Apple OAuth page
3. User approves with password/2FA
4. Browser redirects to `superpets://auth` deep link
5. App catches deep link and processes OAuth token
6. User is authenticated and logged in

## Files Modified

### Web App
- [src/hooks/useAuth.ts](superpets-web/src/hooks/useAuth.ts#L67-L77) - Added `signInWithApple()` method
- [src/components/Auth/LoginForm.tsx](superpets-web/src/components/Auth/LoginForm.tsx#L135-L145) - Added Apple Sign-In button
- [src/components/Auth/SignupForm.tsx](superpets-web/src/components/Auth/SignupForm.tsx#L170-L180) - Added Apple Sign-In button

### Mobile App
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/data/auth/AuthManager.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/data/auth/AuthManager.kt#L205-L222) - Added `signInWithApple()` method
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/AuthViewModel.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/AuthViewModel.kt#L111-L140) - Added Apple OAuth handler
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/LoginScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/LoginScreen.kt#L237-L240) - Connected Apple button
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/SignupScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/SignupScreen.kt#L240-L243) - Connected Apple button

## Troubleshooting

### "invalid_client" Error
- Verify your Services ID matches exactly what you entered in Supabase
- Make sure the Services ID is configured with Sign in with Apple enabled
- Check that your Return URLs include the exact Supabase callback URL

### "invalid_request" Error
- Verify your private key (.p8 file) was copied correctly to Supabase
- Make sure you included the BEGIN and END lines
- Check that your Key ID and Team ID are correct (10 characters each)

### Web Domain Verification Failed
- Make sure you added both `superpets.fun` and your Supabase domain
- Domains must not include `https://` or paths, just the domain name
- You may need to wait a few minutes for Apple to verify the domains

### Apple Sign-In Not Working on localhost
- Apple **requires HTTPS** for Sign in with Apple
- Use ngrok or similar tunneling service for local testing
- Or test directly on your production domain

### iOS: "Sign in with Apple capability not found"
- Open your Xcode project
- Check **Signing & Capabilities** tab
- Ensure **Sign in with Apple** capability is added
- Verify your Bundle ID matches the App ID in Apple Developer Portal

### Email Not Returned
- Apple only provides the user's email on first sign-in
- If user chose to hide email, Apple provides a relay email
- Subsequent sign-ins only provide a user identifier
- Handle cases where email might be null or a relay address

### Private Key Issues
- Make sure you downloaded the .p8 key file (you can only download it once!)
- If you lost the key, you need to revoke it and create a new one
- The key file should be plain text starting with `-----BEGIN PRIVATE KEY-----`

## Apple Sign-In Best Practices

1. **Email Privacy**: Always handle the case where users hide their email
   - Store the user's `sub` (subject) identifier as primary key
   - Email might be a relay address: `privaterelay@icloud.com`

2. **Name Handling**: Apple only provides the user's name on first sign-in
   - Cache the name when you first receive it
   - Don't rely on it being available in subsequent sign-ins

3. **Token Verification**: Supabase handles token verification automatically
   - No need to manually verify Apple's JWT tokens
   - Supabase validates tokens before creating sessions

4. **Revoking Access**: Users can revoke Apple Sign-In access from their Apple ID settings
   - Handle sign-in failures gracefully
   - Provide alternative sign-in methods

## Security Considerations

- ✅ Private key (.p8) stored securely in Supabase, not exposed to clients
- ✅ HTTPS required for production (already configured via Firebase Hosting)
- ✅ Apple validates redirect URLs to prevent phishing
- ✅ Tokens are short-lived and automatically refreshed by Supabase
- ⚠️ Store the private key file (.p8) in a secure location - you can only download it once!
- ⚠️ Never commit the .p8 file to version control
- ⚠️ Rate limit authentication endpoints to prevent abuse (already implemented in backend)

## Testing Checklist

### Web App
- [ ] Sign in with Apple on production (`https://superpets.fun`)
- [ ] Verify email is received (or relay email if hidden)
- [ ] Test both "Share My Email" and "Hide My Email" options
- [ ] Verify user is created in Supabase with correct data
- [ ] Test session persistence (refresh page, should stay logged in)
- [ ] Test sign out and sign in again

### iOS App
- [ ] Sign in with Apple on iPhone/iPad
- [ ] Test Face ID/Touch ID authentication
- [ ] Verify email handling (including relay emails)
- [ ] Test deep link redirect after authentication
- [ ] Verify session persistence across app restarts
- [ ] Test sign out and sign in again

### Android App
- [ ] Sign in with Apple on Android device
- [ ] Test browser-based OAuth flow
- [ ] Verify deep link redirect works correctly
- [ ] Test session persistence
- [ ] Test sign out and sign in again

## Comparison: Apple vs Google OAuth

| Feature | Apple | Google |
|---------|-------|--------|
| **Cost** | $99/year (Apple Developer) | Free |
| **Email Privacy** | Optional (can hide) | Always provided |
| **Development** | Requires membership | Free to test |
| **iOS Integration** | Native (seamless) | Web-based |
| **Android Integration** | Web-based | Native (Google Play) |
| **User Trust** | High (privacy-focused) | High (ubiquitous) |
| **Setup Complexity** | Higher | Lower |

## Next Steps

After setting up Apple OAuth:

1. **Test thoroughly** on all platforms (web, iOS, Android)
2. **Publish OAuth consent screen** in Apple Developer Portal before public launch
3. **Monitor authentication errors** via Sentry
4. **Add analytics** to track OAuth conversion rates
5. **Consider adding** other social providers (GitHub, Facebook, etc.)

## References

- [Apple Sign in with Apple Docs](https://developer.apple.com/sign-in-with-apple/)
- [Supabase Apple OAuth Guide](https://supabase.com/docs/guides/auth/social-login/auth-apple)
- [Apple Developer Portal](https://developer.apple.com/account/)
- [Sign in with Apple JS](https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_js)
