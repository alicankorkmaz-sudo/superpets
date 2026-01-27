import { Coins, Loader2 } from 'lucide-react';
import { useState } from 'react';
import { api } from '../lib/api';

interface PricingTier {
  credits: number;
  price: number;
  popular?: boolean;
}

const pricingTiers: PricingTier[] = [
  { credits: 10, price: 4.99 },
  { credits: 25, price: 11.99, popular: true },
  { credits: 50, price: 21.49 },
  { credits: 100, price: 39.99 },
];

export function PricingPage() {
  const [loading, setLoading] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);

  const handlePurchase = async (credits: number) => {
    try {
      setLoading(credits);
      setError(null);

      const response = await api.createCheckoutSession(credits);

      // Redirect to Stripe checkout
      window.location.href = response.url;
    } catch (err) {
      console.error('Error creating checkout session:', err);
      setError(err instanceof Error ? err.message : 'Failed to create checkout session');
      setLoading(null);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-50 to-secondary-50 py-8 sm:py-12 px-4">
      <div className="max-w-4xl mx-auto">
        <div className="text-center mb-8 sm:mb-12">
          <h1 className="text-2xl sm:text-3xl md:text-4xl font-bold text-gray-900 mb-3 sm:mb-4">
            Buy Credits
          </h1>
          <p className="text-base sm:text-lg text-gray-600">
            1 credit = 1 image
          </p>
          {error && (
            <div className="mt-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
              {error}
            </div>
          )}
        </div>

        <div className="grid grid-cols-2 lg:grid-cols-4 gap-3 sm:gap-4">
          {pricingTiers.map((tier) => (
            <div
              key={tier.credits}
              className={`bg-white rounded-xl shadow-lg p-4 sm:p-6 relative ${
                tier.popular ? 'ring-2 ring-primary-500' : ''
              }`}
            >
              {tier.popular && (
                <div className="absolute -top-3 left-1/2 transform -translate-x-1/2">
                  <span className="bg-primary-500 text-white px-3 py-0.5 rounded-full text-xs font-semibold">
                    Popular
                  </span>
                </div>
              )}

              <div className="text-center mb-4">
                <div className="flex items-center justify-center gap-1.5 mb-1">
                  <Coins className="text-primary-500" size={24} />
                  <span className="text-3xl sm:text-4xl font-bold text-gray-900">
                    {tier.credits}
                  </span>
                </div>
                <p className="text-sm text-gray-500">credits</p>
              </div>

              <div className="text-center mb-4">
                <div className="text-2xl sm:text-3xl font-bold text-gray-900">
                  ${tier.price}
                </div>
              </div>

              <button
                onClick={() => handlePurchase(tier.credits)}
                disabled={loading !== null}
                className={`w-full py-2.5 rounded-lg font-semibold text-sm transition-all flex items-center justify-center gap-2 ${
                  tier.popular
                    ? 'bg-primary-500 text-white hover:bg-primary-600 disabled:opacity-50'
                    : 'bg-gray-100 text-gray-900 hover:bg-gray-200 disabled:opacity-50'
                }`}
              >
                {loading === tier.credits ? (
                  <>
                    <Loader2 size={16} className="animate-spin" />
                    Processing...
                  </>
                ) : (
                  'Buy'
                )}
              </button>
            </div>
          ))}
        </div>

        <p className="text-center text-sm text-gray-500 mt-6">
          Credits never expire
        </p>
      </div>
    </div>
  );
}
