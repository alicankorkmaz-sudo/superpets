# Google OAuth Setup Guide

This guide explains how to configure Google Sign-In for both the web and mobile apps.

## Overview

Google OAuth has been implemented in:
- ✅ **Web App** (React): [LoginForm.tsx](superpets-web/src/components/Auth/LoginForm.tsx) and [SignupForm.tsx](superpets-web/src/components/Auth/SignupForm.tsx)
- ✅ **Mobile App** (Compose Multiplatform): [LoginScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/LoginScreen.kt) and [SignupScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/SignupScreen.kt)

## Prerequisites

1. Google Cloud Console account
2. Supabase project (already set up)
3. Access to your Supabase dashboard

## Step 1: Create Google Cloud Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one:
   - Click "Select a project" at the top
   - Click "New Project"
   - Name it "Superpets" or similar
   - Click "Create"

## Step 2: Enable Google+ API

1. In your Google Cloud project, go to **APIs & Services** > **Library**
2. Search for "Google+ API"
3. Click on it and click **Enable**

## Step 3: Configure OAuth Consent Screen

1. Go to **APIs & Services** > **OAuth consent screen**
2. Select **External** user type
3. Click **Create**
4. Fill in the required information:
   - **App name**: Superpets
   - **User support email**: Your email
   - **Developer contact information**: Your email
5. Click **Save and Continue**
6. On the Scopes page, click **Save and Continue** (default scopes are fine)
7. On the Test users page, add your test email addresses
8. Click **Save and Continue**

## Step 4: Create OAuth 2.0 Credentials

1. Go to **APIs & Services** > **Credentials**
2. Click **Create Credentials** > **OAuth 2.0 Client ID**
3. Select **Web application** as Application type
4. Configure authorized origins and redirect URIs:

### Authorized JavaScript Origins

Add these URLs (one per line):
```
http://localhost:5173
https://superpets.fun
```

### Authorized Redirect URIs

From your Supabase dashboard screenshot, you need to add:
```
https://zrivjktyzllaevduydai.supabase.co/auth/v1/callback
```

**To find your exact callback URL:**
1. Go to Supabase Dashboard > Authentication > Providers
2. Find the Google provider section
3. Copy the "Callback URL (for OAuth)" shown

5. Click **Create**
6. Copy the **Client ID** and **Client Secret** that are displayed

## Step 5: Configure Supabase

1. Go to your [Supabase Dashboard](https://supabase.com/dashboard)
2. Navigate to **Authentication** > **Providers**
3. Find **Google** in the list
4. Toggle **Enable Sign in with Google** to ON
5. Paste your **Client ID** from Google Cloud Console
6. Paste your **Client Secret** from Google Cloud Console
7. Click **Save**

## Step 6: Test Web App

### Development Testing

1. Start the web development server:
```bash
cd superpets-web
npm run dev
```

2. Open `http://localhost:5173` in your browser
3. Navigate to Login or Signup
4. Click "Sign in with Google" button
5. You should be redirected to Google OAuth consent screen
6. After approving, you should be redirected back to Superpets and logged in

### Production Testing

1. Deploy to production or visit `https://superpets.fun`
2. Try the Google Sign-In flow
3. Verify successful authentication

## Step 7: Configure Mobile App OAuth

For mobile apps, you need additional OAuth 2.0 credentials:

### Android Configuration

1. In Google Cloud Console > **Credentials**, create another OAuth 2.0 Client ID
2. Select **Android** as Application type
3. Fill in:
   - **Package name**: `fun.superpets.mobile`
   - **SHA-1 certificate fingerprint**: Get this from your keystore

To get SHA-1 fingerprint:
```bash
cd superpets-mobile/android
./gradlew signingReport
```

4. Copy the **Client ID** (you'll need this for Android)

### iOS Configuration

1. In Google Cloud Console > **Credentials**, create another OAuth 2.0 Client ID
2. Select **iOS** as Application type
3. Fill in:
   - **Bundle ID**: `fun.superpets.mobile`
4. Copy the **Client ID** (you'll need this for iOS)

### Update Mobile App Configuration

The Supabase Kotlin SDK handles OAuth flow for mobile. You need to:

1. **Add Google Sign-In dependencies** (if not already added):

For Android in `composeApp/build.gradle.kts`:
```kotlin
dependencies {
    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.0.0")
}
```

For iOS, no additional dependencies needed (handled by Supabase SDK).

2. **Configure deep linking** for OAuth callback:

Android - Update `AndroidManifest.xml`:
```xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data
            android:scheme="superpets"
            android:host="auth" />
    </intent-filter>
</activity>
```

iOS - Already configured in `AuthManager.kt`:
```kotlin
scheme = "superpets"
host = "auth"
```

3. **Test mobile OAuth flow**:
```bash
cd superpets-mobile
# For Android
./gradlew :composeApp:installDebug

# For iOS (requires Xcode)
open iosApp/iosApp.xcodeproj
```

## How It Works

### Web App Flow

1. User clicks "Sign in with Google" button
2. `signInWithGoogle()` is called in [useAuth.ts](superpets-web/src/hooks/useAuth.ts)
3. Supabase SDK redirects user to Google OAuth consent screen
4. User approves permissions
5. Google redirects back to `https://superpets.fun/auth/callback`
6. Supabase processes the OAuth token
7. User is authenticated and session is created
8. User is redirected to the dashboard

### Mobile App Flow

1. User taps "Sign in with Google" button
2. `signInWithGoogle()` is called in [AuthViewModel.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/AuthViewModel.kt)
3. Supabase SDK opens Google OAuth in system browser
4. User approves permissions
5. System browser redirects to `superpets://auth` deep link
6. App catches deep link and processes OAuth token
7. User is authenticated and session is created
8. User is navigated to main app

## Files Modified

### Web App
- [src/hooks/useAuth.ts](superpets-web/src/hooks/useAuth.ts) - Added `signInWithGoogle()` method
- [src/components/Auth/LoginForm.tsx](superpets-web/src/components/Auth/LoginForm.tsx) - Added Google Sign-In button
- [src/components/Auth/SignupForm.tsx](superpets-web/src/components/Auth/SignupForm.tsx) - Added Google Sign-In button

### Mobile App
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/data/auth/AuthManager.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/data/auth/AuthManager.kt) - Added `signInWithGoogle()` method
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/AuthViewModel.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/AuthViewModel.kt) - Added Google OAuth handler
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/LoginScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/LoginScreen.kt) - Connected Google button
- [composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/SignupScreen.kt](superpets-mobile/composeApp/src/commonMain/kotlin/com/superpets/mobile/screens/auth/SignupScreen.kt) - Connected Google button

## Troubleshooting

### "redirect_uri_mismatch" Error
- Make sure the redirect URI in Google Cloud Console exactly matches your Supabase callback URL
- Double-check there are no trailing slashes or typos

### OAuth Consent Screen Issues
- Make sure your email is added as a test user if the app is in testing mode
- If you see "This app is blocked", publish your OAuth consent screen or add your email to test users

### Mobile Deep Link Not Working
- Verify the deep link scheme (`superpets://auth`) is configured correctly in both Android and iOS
- Check that the Supabase Auth configuration in `AuthManager.kt` matches the scheme

### Session Not Persisting
- Check that `persistSession: true` is set in Supabase client configuration
- For web: Check browser localStorage is enabled
- For mobile: Check that `SupabaseSessionManager` is properly saving to device storage

## Next Steps

After setting up Google OAuth, you may want to:

1. **Add Apple Sign-In** - Follow a similar process for Apple OAuth (requires Apple Developer account)
2. **Test user flow** - Create a test account and verify the entire authentication flow
3. **Monitor errors** - Use Sentry to track any OAuth-related errors
4. **Add analytics** - Track OAuth sign-in conversions

## Security Considerations

- ✅ OAuth tokens are handled by Supabase, not stored in your app
- ✅ HTTPS is required for production OAuth (already configured via Firebase Hosting)
- ✅ Client secrets are stored in Supabase backend, not exposed to clients
- ⚠️ Remember to remove test users and publish OAuth consent screen before public launch
- ⚠️ Rate limit authentication endpoints to prevent abuse (already implemented in backend)

## References

- [Supabase Google OAuth Docs](https://supabase.com/docs/guides/auth/social-login/auth-google)
- [Google OAuth 2.0 Setup](https://developers.google.com/identity/protocols/oauth2)
- [Supabase Kotlin SDK Docs](https://supabase.com/docs/reference/kotlin/introduction)
