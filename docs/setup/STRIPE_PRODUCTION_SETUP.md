# Production Stripe Configuration Guide

This guide provides step-by-step instructions for configuring Stripe for production testing. The backend code has been updated with production URLs. Now you need to configure environment variables and webhooks.

---

## Step 1: Get Your Stripe Live Keys

### 1.1 Navigate to Stripe Dashboard
- Go to: https://dashboard.stripe.com/apikeys
- Ensure you're in **Live mode** (toggle in top right should show "Live")

### 1.2 Copy Your Keys
You'll need two keys:

1. **Publishable key** (starts with `pk_live_`)
   - This is visible on the page
   - Copy and save it temporarily

2. **Secret key** (starts with `sk_live_`)
   - Click "Reveal test key" to show it
   - Copy and save it temporarily
   - ⚠️ **Keep this secret!** Never commit it to code

> [!CAUTION]
> If your Stripe account is not yet activated for live mode, you can use **test mode keys** (`pk_test_` and `sk_test_`) to test the flow without processing real payments.

---

## Step 2: Configure Railway (Backend)

### 2.1 Add Stripe Secret Key

1. Go to your Railway project: https://railway.com/project/b7df09da-2741-413c-8474-4baab3059775
2. Select your backend service
3. Click on the **Variables** tab
4. Click **+ New Variable**
5. Add:
   - **Variable name**: `STRIPE_SECRET_KEY`
   - **Value**: Your Stripe secret key (starts with `sk_live_` or `sk_test_`)
6. Click **Add**

> [!NOTE]
> Don't deploy yet - we'll add the webhook secret first, then deploy once.

---

## Step 3: Configure Stripe Webhook

### 3.1 Create Webhook Endpoint

1. Go to: https://dashboard.stripe.com/webhooks
2. Click **+ Add endpoint**
3. Configure:
   - **Endpoint URL**: `https://api.superpets.fun/stripe/webhook`
   - **Description**: `Superpets Production Webhook`
   - **Listen to**: Select "Events on your account"

### 3.2 Select Events

Click **Select events** and choose:
- ✅ `checkout.session.completed`
- ✅ `payment_intent.payment_failed`
- ✅ `charge.refunded`
- ✅ `charge.dispute.created`
- ✅ `charge.dispute.closed`

Click **Add events**, then **Add endpoint**

### 3.3 Get Webhook Signing Secret

1. After creating the endpoint, you'll see it in the list
2. Click on the endpoint to view details
3. In the "Signing secret" section, click **Reveal**
4. Copy the secret (starts with `whsec_`)

### 3.4 Add Webhook Secret to Railway

1. Return to Railway Variables tab
2. Click **+ New Variable**
3. Add:
   - **Variable name**: `STRIPE_WEBHOOK_SECRET`
   - **Value**: The webhook signing secret (starts with `whsec_`)
4. Click **Add**

---

## Step 4: Deploy Backend

### 4.1 Verify Environment Variables

In Railway, verify you have:
- ✅ `STRIPE_SECRET_KEY` (sk_live_* or sk_test_*)
- ✅ `STRIPE_WEBHOOK_SECRET` (whsec_*)
- ✅ All other existing variables (SUPABASE_DB_URL, FAL_API_KEY, etc.)

### 4.2 Deploy

**Option A: Automatic (if GitHub is linked)**
1. Commit and push the updated `Routing.kt` file:
   ```bash
   cd /Users/alican.korkmaz/Code/superpets
   git add superpets-backend/src/main/kotlin/Routing.kt
   git commit -m "Update Stripe redirect URLs for production"
   git push origin main
   ```
2. Railway will automatically detect and deploy

**Option B: Manual (using Railway CLI)**
1. Navigate to backend directory:
   ```bash
   cd /Users/alican.korkmaz/Code/superpets/superpets-backend
   ```
2. Deploy:
   ```bash
   railway up
   ```

### 4.3 Verify Deployment

1. Check Railway logs for successful deployment
2. Look for: "Application started" or similar message
3. Verify no errors related to Stripe configuration

---

## Step 5: Configure GitHub Secrets (Frontend)

### 5.1 Update Stripe Publishable Key

1. Go to: https://github.com/alicankorkmaz-sudo/superpets/settings/secrets/actions
2. Find `VITE_STRIPE_PUBLISHABLE_KEY` in the list
3. Click the **Update** button (pencil icon)
4. Replace the value with your Stripe publishable key (starts with `pk_live_` or `pk_test_`)
5. Click **Update secret**

### 5.2 Trigger Frontend Deployment

**Option A: Automatic (push a change)**
1. Make any small change in `superpets-web/` directory (e.g., add a comment)
2. Commit and push to trigger GitHub Actions:
   ```bash
   cd /Users/alican.korkmaz/Code/superpets
   git add superpets-web/
   git commit -m "Trigger frontend deployment with updated Stripe key"
   git push origin main
   ```

**Option B: Manual (Firebase CLI)**
1. Build with production environment:
   ```bash
   cd /Users/alican.korkmaz/Code/superpets/superpets-web
   npm run build
   ```
2. Deploy to Firebase:
   ```bash
   firebase deploy
   ```

### 5.3 Verify Deployment

1. Go to: https://github.com/alicankorkmaz-sudo/superpets/actions
2. Check the latest workflow run
3. Ensure it completes successfully
4. Visit https://superpets.fun to verify the site is updated

---

## Step 6: Test the Integration

### 6.1 Test Checkout Creation

1. Navigate to: https://superpets.fun
2. Log in with your account
3. Click **Buy Credits** or navigate to `/pricing`
4. Click **Purchase** on any tier
5. **Expected**: Redirected to Stripe Checkout page
6. **Verify**: URL is `checkout.stripe.com`
7. **Verify**: Pricing matches the selected tier

### 6.2 Test Successful Payment

**Using Test Card** (if using test mode keys):
- Card number: `4242 4242 4242 4242`
- Expiry: Any future date (e.g., 12/34)
- CVC: Any 3 digits (e.g., 123)
- ZIP: Any 5 digits (e.g., 12345)

**Using Real Card** (if using live mode keys):
- Use your actual credit card
- ⚠️ **This will be a real charge!**

**Steps**:
1. Enter payment details on Stripe Checkout
2. Click **Pay**
3. **Expected**: Redirected to `https://superpets.fun?payment=success`
4. **Expected**: Success toast notification appears
5. Check your credit balance
6. **Expected**: Credits are added to your account

### 6.3 Verify Webhook

1. Go to: https://dashboard.stripe.com/webhooks
2. Click on your webhook endpoint
3. Check the **Recent events** section
4. **Expected**: Recent `checkout.session.completed` event with **200** response
5. Click on the event to see details
6. **Expected**: Response body shows `{"received":true}`

### 6.4 Check Railway Logs

1. Go to Railway dashboard
2. Click on your backend service
3. Click **Deployments** → Latest deployment → **View Logs**
4. Search for: "Successfully added"
5. **Expected**: Log entry like: `Successfully added X credits to user [userId]`

### 6.5 Test Payment Cancellation

1. Start checkout process again
2. On Stripe Checkout page, click the **Back arrow** (top left)
3. **Expected**: Redirected to `https://superpets.fun?payment=cancelled`
4. **Expected**: Cancellation toast notification appears
5. **Expected**: No credits are added

---

## Step 7: Optional - Test Refund

> [!WARNING]
> Only test refunds if you've made a real payment or if you're comfortable with the refund process.

1. Go to: https://dashboard.stripe.com/payments
2. Find your test payment
3. Click on it to view details
4. Click **Refund payment**
5. Enter refund amount and confirm
6. Check Railway logs for refund processing
7. **Expected**: Credits deducted from user account (if charge has metadata)

---

## Troubleshooting

### Issue: "Invalid signature" error in webhook

**Cause**: Webhook secret doesn't match

**Solution**:
1. Verify `STRIPE_WEBHOOK_SECRET` in Railway matches the secret from Stripe Dashboard
2. Redeploy backend after updating the secret

### Issue: Credits not added after payment

**Cause**: Webhook not receiving events or processing failed

**Solution**:
1. Check Stripe Dashboard webhook logs for errors
2. Check Railway backend logs for error messages
3. Verify webhook URL is correct: `https://api.superpets.fun/stripe/webhook`
4. Ensure webhook events include `checkout.session.completed`

### Issue: Redirected to localhost after payment

**Cause**: Backend not deployed with updated URLs

**Solution**:
1. Verify `Routing.kt` has production URLs
2. Redeploy backend
3. Check Railway logs to confirm new deployment

### Issue: "Stripe is not defined" error on frontend

**Cause**: Publishable key not set or incorrect

**Solution**:
1. Verify `VITE_STRIPE_PUBLISHABLE_KEY` in GitHub secrets
2. Rebuild and redeploy frontend
3. Check browser console for the actual error

---

## Summary Checklist

Before considering the setup complete, verify:

- ✅ Backend deployed with production URLs
- ✅ `STRIPE_SECRET_KEY` set in Railway
- ✅ `STRIPE_WEBHOOK_SECRET` set in Railway
- ✅ Webhook endpoint created in Stripe Dashboard
- ✅ `VITE_STRIPE_PUBLISHABLE_KEY` updated in GitHub secrets
- ✅ Frontend deployed with new publishable key
- ✅ Test payment completed successfully
- ✅ Credits added to account after payment
- ✅ Webhook received and processed (200 response)
- ✅ Payment cancellation works correctly

---

## Next Steps

After successful testing:

1. **Monitor**: Keep an eye on Stripe Dashboard for the first few transactions
2. **Document**: Update `STRIPE_SETUP.md` with any production-specific notes
3. **Alert**: Set up email notifications in Stripe for failed payments
4. **Review**: Check Railway logs daily for the first week to catch any issues

**You're ready for production Stripe payments! 🎉**
