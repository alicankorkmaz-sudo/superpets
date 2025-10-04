import { loadStripe } from '@stripe/stripe-js';

// TODO: Replace with your actual Stripe publishable key
const stripePublishableKey = import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY || 'pk_test_...';

export const stripePromise = loadStripe(stripePublishableKey);
