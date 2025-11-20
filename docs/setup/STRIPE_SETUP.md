# Stripe Payment Integration Setup

This guide walks you through setting up Stripe payments for the Superpets application.

## Prerequisites

- Stripe account (sign up at https://stripe.com)
- Stripe CLI for webhook testing (optional but recommended)

## 1. Get Your Stripe Keys

### Test Mode Keys (for development)

1. Go to https://dashboard.stripe.com/test/apikeys
2. Copy your **Publishable key** (starts with `pk_test_`)
3. Copy your **Secret key** (starts with `sk_test_`)

### Live Mode Keys (for production)

1. Go to https://dashboard.stripe.com/apikeys
2. Copy your **Publishable key** (starts with `pk_live_`)
3. Copy your **Secret key** (starts with `sk_live_`)

## 2. Configure Frontend

Update `.env` in `superpets-web/`:

```env
VITE_STRIPE_PUBLISHABLE_KEY=pk_test_your_key_here
```

For production, use your live publishable key.

## 3. Configure Backend

### Option A: Using application.conf

Edit `superpets-backend/src/main/resources/application.conf`:

```hocon
stripe {
    secretKey = "sk_test_your_secret_key_here"
    webhookSecret = "whsec_your_webhook_secret_here"
}
```

### Option B: Using Environment Variables (Recommended for Production)

Set environment variables:

```bash
export STRIPE_SECRET_KEY=sk_test_your_secret_key_here
export STRIPE_WEBHOOK_SECRET=whsec_your_webhook_secret_here
```

## 4. Install Stripe CLI (for webhook testing)

### macOS
```bash
brew install stripe/stripe-cli/stripe
```

### Linux
```bash
wget https://github.com/stripe/stripe-cli/releases/download/v1.19.4/stripe_1.19.4_linux_x86_64.tar.gz
tar -xvf stripe_1.19.4_linux_x86_64.tar.gz
sudo mv stripe /usr/local/bin/
```

### Windows
Download from https://github.com/stripe/stripe-cli/releases

## 5. Set Up Webhook Forwarding (Development)

1. Login to Stripe CLI:
```bash
stripe login
```

2. Forward webhooks to your local backend:
```bash
stripe listen --forward-to http://localhost:8080/stripe/webhook
```

3. Copy the webhook signing secret (starts with `whsec_`) from the output
4. Add it to your backend configuration:
   - In `application.conf`: `stripe.webhookSecret = "whsec_..."`
   - Or as environment variable: `export STRIPE_WEBHOOK_SECRET=whsec_...`

## 6. Test the Integration

### Start the Backend
```bash
cd superpets-backend
./gradlew run
```

### Start the Frontend
```bash
cd superpets-web
npm run dev
```

### Start Webhook Forwarding
```bash
stripe listen --forward-to http://localhost:8080/stripe/webhook
```

### Test Purchase Flow

1. Navigate to http://localhost:5173
2. Log in with a Firebase account
3. Click "Buy Credits" in the header
4. Select a pricing tier and click "Purchase"
5. You'll be redirected to Stripe Checkout
6. Use test card: `4242 4242 4242 4242`
   - Expiry: Any future date
   - CVC: Any 3 digits
   - ZIP: Any 5 digits
7. Complete the payment
8. You'll be redirected back to the app
9. Your credits should be updated automatically

## 7. Production Setup

### Create Webhook Endpoint in Stripe Dashboard

1. Go to https://dashboard.stripe.com/webhooks
2. Click "Add endpoint"
3. Set URL to: `https://your-production-domain.com/stripe/webhook`
4. Select the following events:
   - `checkout.session.completed` - For successful payments
   - `payment_intent.payment_failed` - For failed payments
   - `charge.refunded` - For refunds
   - `charge.dispute.created` - For disputes
   - `charge.dispute.closed` - For resolved disputes
5. Copy the webhook signing secret
6. Set it in your production environment variables

### Update Success/Cancel URLs

In `superpets-backend/src/main/kotlin/Routing.kt`, update the checkout session creation:

```kotlin
successUrl = "https://your-production-domain.com?payment=success",
cancelUrl = "https://your-production-domain.com?payment=cancelled"
```

### Use Live Keys

Replace all test keys (`pk_test_`, `sk_test_`) with live keys (`pk_live_`, `sk_live_`).

## Pricing Tiers

The following credit packages are configured:

| Credits | Price  | Per Credit |
|---------|--------|------------|
| 10      | $4.99  | $0.50      |
| 25      | $11.99 | $0.48      |
| 50      | $21.49 | $0.43      |
| 100     | $39.99 | $0.40      |

To modify pricing, update:
- Frontend: `superpets-web/src/pages/PricingPage.tsx`
- Backend: `superpets-backend/src/main/kotlin/services/StripeService.kt`

## Features Implemented

### Payment Success/Cancellation Notifications
- Users see a success toast notification when returning from successful payment
- Cancellation toast shown if user cancels payment
- Notifications auto-dismiss after 5 seconds
- URL parameters are automatically cleaned up

### Webhook Event Handlers
The backend handles the following Stripe webhook events:

1. **`checkout.session.completed`** - Adds credits to user account
2. **`payment_intent.payment_failed`** - Logs failed payments
3. **`charge.refunded`** - Deducts credits when refund is issued (requires metadata)
4. **`charge.dispute.created`** - Logs disputes for admin review
5. **`charge.dispute.closed`** - Logs dispute resolution

### Refund Handling
**Important:** For refunds to work correctly and automatically deduct credits, the charge must include `userId` and `credits` in metadata. The current implementation logs refunds but may not always auto-deduct credits if metadata is missing. Consider implementing a manual admin process for refund credit adjustments.

## Troubleshooting

### "Invalid signature" error
- Make sure the webhook secret matches between Stripe and your backend
- Verify the webhook URL is correct
- Check that Stripe CLI is forwarding to the right port

### Credits not added after payment
- Check backend logs for errors
- Verify webhook is being received (check Stripe CLI output)
- Ensure Firestore service is working correctly
- Look for "Successfully added X credits to user..." in logs

### Checkout session creation fails
- Verify Stripe secret key is correct
- Check backend logs for detailed error messages
- Ensure user is authenticated (Firebase token is valid)

### Refunds not deducting credits
- Check if the charge has `userId` and `credits` metadata
- Review backend logs for refund events
- May need manual admin intervention for credit adjustments

## Security Notes

- **Never commit API keys to version control**
- Use environment variables in production
- Verify webhook signatures to prevent fraud
- Use HTTPS in production
- Keep Stripe library updated

## Additional Resources

- [Stripe Documentation](https://stripe.com/docs)
- [Stripe Checkout Guide](https://stripe.com/docs/payments/checkout)
- [Webhook Testing](https://stripe.com/docs/webhooks/test)
- [Test Cards](https://stripe.com/docs/testing)
