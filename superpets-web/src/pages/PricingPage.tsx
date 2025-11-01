import { Check, Coins, Loader2 } from 'lucide-react';
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
      <div className="max-w-6xl mx-auto">
        <div className="text-center mb-8 sm:mb-12">
          <h1 className="text-2xl sm:text-3xl md:text-4xl font-bold text-gray-900 mb-3 sm:mb-4">
            Choose Your Credit Package
          </h1>
          <p className="text-base sm:text-lg text-gray-600">
            1 credit = 1 AI-generated image of your superpet
          </p>
          {error && (
            <div className="mt-4 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
              {error}
            </div>
          )}
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 sm:gap-6">
          {pricingTiers.map((tier) => (
            <div
              key={tier.credits}
              className={`bg-white rounded-2xl shadow-lg p-6 relative ${
                tier.popular ? 'ring-2 ring-primary-500' : ''
              }`}
            >
              {tier.popular && (
                <div className="absolute -top-4 left-1/2 transform -translate-x-1/2">
                  <span className="bg-gradient-to-r from-primary-500 to-secondary-500 text-white px-4 py-1 rounded-full text-sm font-semibold">
                    Most Popular
                  </span>
                </div>
              )}

              <div className="text-center mb-6">
                <div className="flex items-center justify-center gap-2 mb-2">
                  <Coins className="text-primary-500" size={32} />
                  <span className="text-5xl font-bold text-gray-900">
                    {tier.credits}
                  </span>
                </div>
                <p className="text-gray-600">credits</p>
              </div>

              <div className="text-center mb-6">
                <div className="text-4xl font-bold text-gray-900 mb-1">
                  ${tier.price}
                </div>
                <p className="text-sm text-gray-500">
                  ${(tier.price / tier.credits).toFixed(2)} per credit
                </p>
              </div>

              <ul className="space-y-3 mb-6">
                <li className="flex items-center gap-2 text-gray-700">
                  <Check size={20} className="text-green-500 flex-shrink-0" />
                  <span>Generate {tier.credits} images</span>
                </li>
                <li className="flex items-center gap-2 text-gray-700">
                  <Check size={20} className="text-green-500 flex-shrink-0" />
                  <span>All heroes unlocked</span>
                </li>
                <li className="flex items-center gap-2 text-gray-700">
                  <Check size={20} className="text-green-500 flex-shrink-0" />
                  <span>High-quality outputs</span>
                </li>
                <li className="flex items-center gap-2 text-gray-700">
                  <Check size={20} className="text-green-500 flex-shrink-0" />
                  <span>Credits never expire</span>
                </li>
              </ul>

              <button
                onClick={() => handlePurchase(tier.credits)}
                disabled={loading !== null}
                className={`w-full py-3 rounded-lg font-semibold transition-all flex items-center justify-center gap-2 ${
                  tier.popular
                    ? 'bg-gradient-to-r from-primary-500 to-secondary-500 text-white hover:shadow-lg disabled:opacity-50'
                    : 'bg-gray-100 text-gray-900 hover:bg-gray-200 disabled:opacity-50'
                }`}
              >
                {loading === tier.credits ? (
                  <>
                    <Loader2 size={20} className="animate-spin" />
                    Processing...
                  </>
                ) : (
                  'Purchase'
                )}
              </button>
            </div>
          ))}
        </div>

        <div className="mt-8 sm:mt-12 bg-white rounded-2xl shadow-lg p-6 sm:p-8">
          <h2 className="text-xl sm:text-2xl font-bold text-gray-900 mb-4 sm:mb-6 text-center">
            How It Works
          </h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            <div className="text-center">
              <div className="bg-primary-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
                <span className="text-2xl font-bold text-primary-600">1</span>
              </div>
              <h3 className="font-semibold text-gray-900 mb-2">Choose a Package</h3>
              <p className="text-gray-600 text-sm">
                Select the credit package that fits your needs
              </p>
            </div>
            <div className="text-center">
              <div className="bg-primary-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
                <span className="text-2xl font-bold text-primary-600">2</span>
              </div>
              <h3 className="font-semibold text-gray-900 mb-2">Upload & Transform</h3>
              <p className="text-gray-600 text-sm">
                Upload your pet photo and choose a superhero
              </p>
            </div>
            <div className="text-center">
              <div className="bg-primary-100 w-12 h-12 rounded-full flex items-center justify-center mx-auto mb-3">
                <span className="text-2xl font-bold text-primary-600">3</span>
              </div>
              <h3 className="font-semibold text-gray-900 mb-2">Get Your Superpet</h3>
              <p className="text-gray-600 text-sm">
                Download your AI-generated superhero pet images
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
