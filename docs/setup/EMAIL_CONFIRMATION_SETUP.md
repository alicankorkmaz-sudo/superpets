# Email Confirmation Setup Guide

This guide explains how email confirmation works in Superpets and how to configure it in Supabase.

## Overview

As of October 14, 2025, Superpets now requires email confirmation for new user signups. This adds an extra layer of security and ensures users have valid email addresses.

## How It Works

### 1. User Signup Flow
1. User fills out the signup form with email and password
2. Frontend calls `supabase.auth.signUp()`
3. If email confirmation is enabled in Supabase:
   - User account is created but not yet confirmed
   - Supabase sends a confirmation email with a magic link
   - Frontend shows "Check Your Email" message
4. User clicks the confirmation link in their email
5. Link redirects to `/auth/callback` with authentication tokens
6. Frontend validates the tokens and logs the user in
7. User is redirected to the dashboard

### 2. Login Attempts Before Confirmation
- If a user tries to login before confirming their email:
  - Supabase returns an error: "Email not confirmed"
  - Frontend shows a helpful message asking them to check their email

## Supabase Configuration

### Step 1: Enable Email Confirmation

1. Go to your Supabase project dashboard
2. Navigate to **Authentication** → **Settings**
3. Scroll to **Email Auth** section
4. Check **Enable email confirmations**
5. Save changes

### Step 2: Configure Redirect URLs

You need to add the following redirect URLs to allow Supabase to redirect users after email confirmation:

1. In Supabase dashboard, go to **Authentication** → **URL Configuration**
2. Add these URLs to **Redirect URLs**:

**Development:**
```
http://localhost:5173/auth/callback
```

**Production:**
```
https://superpets.fun/auth/callback
```

3. Click **Save**

### Step 3: Configure Custom SMTP (Optional but Recommended)

By default, Supabase uses their own SMTP server, but you can configure your own:

1. In Supabase dashboard, go to **Authentication** → **Settings**
2. Scroll to **SMTP Settings**
3. Enable **Use Custom SMTP**
4. Configure your SMTP settings:
   - **Sender email**: noreply@superpets.fun
   - **Sender name**: Superpets
   - **Host**: your-smtp-host.com
   - **Port**: 587 (or 465 for SSL)
   - **Username**: your SMTP username
   - **Password**: your SMTP password
5. Save changes

### Step 4: Customize Email Templates (Optional)

You can customize the confirmation email template:

1. In Supabase dashboard, go to **Authentication** → **Email Templates**
2. Select **Confirm signup**
3. Edit the template HTML and text
4. Available variables:
   - `{{ .ConfirmationURL }}` - The magic link URL
   - `{{ .SiteURL }}` - Your site URL
   - `{{ .Token }}` - The confirmation token
   - `{{ .Email }}` - The user's email address

Example customization:
```html
<h2>Welcome to Superpets!</h2>
<p>Thanks for signing up. Please confirm your email address by clicking the button below:</p>
<a href="{{ .ConfirmationURL }}">Confirm Email</a>
```

## Frontend Implementation

The frontend has been updated to handle email confirmation:

### Files Changed

1. **src/lib/supabase.ts**
   - Added PKCE flow configuration
   - Enabled session detection from URL
   - Added auto-refresh token support

2. **src/components/Auth/SignupForm.tsx**
   - Detects when signup requires email confirmation
   - Shows "Check Your Email" message with instructions
   - Displays the user's email address for reference

3. **src/components/Auth/LoginForm.tsx**
   - Handles "Email not confirmed" errors
   - Shows friendly error message with instructions

4. **src/pages/AuthCallbackPage.tsx** (NEW)
   - Handles the `/auth/callback` route
   - Validates email confirmation tokens
   - Shows loading, success, or error states
   - Redirects to dashboard after successful confirmation

5. **src/App.tsx**
   - Added route handling for `/auth/callback`
   - Imports and renders AuthCallbackPage

## Testing the Flow

### Local Development

1. Start the dev server: `npm run dev`
2. Navigate to signup: `http://localhost:5173/?auth=signup`
3. Create a new account with a valid email address
4. You should see the "Check Your Email" message
5. Check your email inbox for the confirmation email
6. Click the confirmation link (or copy the URL)
7. You'll be redirected to `http://localhost:5173/auth/callback`
8. After confirmation, you'll be redirected to the dashboard

### Production

The flow is identical in production, just with the production URLs:
- Signup: `https://superpets.fun/?auth=signup`
- Callback: `https://superpets.fun/auth/callback`

## Troubleshooting

### Email not received
- Check spam/junk folder
- Verify SMTP settings in Supabase
- Check Supabase logs for email sending errors
- Ensure email address is valid

### "Invalid or expired token" error
- Confirmation links expire after a certain time (configurable in Supabase)
- User needs to sign up again to get a new confirmation email
- Check that redirect URLs are correctly configured

### "Email not confirmed" error on login
- User needs to check their email and click the confirmation link
- If email was not received, they may need to sign up again

### Callback page shows error
- Check browser console for detailed error messages
- Verify redirect URLs in Supabase match exactly
- Ensure tokens in URL are not corrupted (e.g., by email clients)

## Environment Variables

No new environment variables are needed. The existing Supabase variables are sufficient:

```env
VITE_SUPABASE_URL=https://your-project.supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key
```

## Security Considerations

1. **PKCE Flow**: We use PKCE (Proof Key for Code Exchange) for secure authentication
2. **Token Expiration**: Confirmation tokens expire after a configurable time
3. **Rate Limiting**: Supabase has built-in rate limiting for signup endpoints
4. **Email Validation**: Only valid email addresses can receive confirmation emails

## Next Steps

After implementing email confirmation, consider:

1. **Password Reset Flow**: Add a "Forgot Password" feature
2. **Resend Confirmation**: Add a button to resend confirmation email
3. **Email Change**: Allow users to change their email address (requires re-confirmation)
4. **Welcome Email**: Send a separate welcome email after confirmation

## References

- [Supabase Auth Documentation](https://supabase.com/docs/guides/auth)
- [Supabase Email Templates](https://supabase.com/docs/guides/auth/auth-email-templates)
- [PKCE Flow](https://oauth.net/2/pkce/)
