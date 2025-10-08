export function TermsOfServicePage() {
  return (
    <div className="max-w-4xl mx-auto px-4 py-12 sm:px-6 lg:px-8">
      <div className="bg-white rounded-lg shadow-sm p-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Terms of Service</h1>
        <p className="text-sm text-gray-500 mb-8">Last Updated: October 8, 2025</p>

        <div className="prose prose-gray max-w-none space-y-6">
          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">1. Acceptance of Terms</h2>
            <p className="text-gray-700 leading-relaxed">
              By accessing and using Superpets ("the Service"), you agree to be bound by these Terms of Service.
              If you do not agree to these terms, please do not use the Service.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">2. Description of Service</h2>
            <p className="text-gray-700 leading-relaxed">
              Superpets is an AI-powered image editing service that transforms pet photos into superhero versions
              using Google's Nano Banana model. The Service operates on a credit-based system where users purchase
              credits to generate edited images.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">3. User Accounts</h2>
            <p className="text-gray-700 leading-relaxed mb-2">
              To use the Service, you must create an account. You agree to:
            </p>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li>Provide accurate and complete information</li>
              <li>Maintain the security of your account credentials</li>
              <li>Accept responsibility for all activities under your account</li>
              <li>Notify us immediately of any unauthorized access</li>
            </ul>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">4. Credits and Payments</h2>
            <p className="text-gray-700 leading-relaxed mb-2">
              <strong>Credit System:</strong> New users receive 5 free credits upon account creation.
              Each generated image costs 1 credit.
            </p>
            <p className="text-gray-700 leading-relaxed mb-2">
              <strong>Purchases:</strong> Credits can be purchased through our payment processor (Stripe).
              All purchases are final and non-refundable unless required by law.
            </p>
            <p className="text-gray-700 leading-relaxed">
              <strong>Failed Generations:</strong> Credits are deducted before image generation. If generation
              fails due to technical issues on our end, credits may be refunded at our discretion.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">5. Acceptable Use</h2>
            <p className="text-gray-700 leading-relaxed mb-2">You agree not to:</p>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li>Upload images that violate intellectual property rights</li>
              <li>Upload inappropriate, offensive, or illegal content</li>
              <li>Use the Service to harass, harm, or impersonate others</li>
              <li>Attempt to reverse engineer or abuse the Service</li>
              <li>Resell or redistribute generated images commercially without permission</li>
            </ul>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">6. Intellectual Property</h2>
            <p className="text-gray-700 leading-relaxed mb-2">
              <strong>Your Content:</strong> You retain ownership of the images you upload. By using the Service,
              you grant us a limited license to process and store your images for the purpose of providing the Service.
            </p>
            <p className="text-gray-700 leading-relaxed">
              <strong>Generated Images:</strong> You own the generated images for personal use. Commercial use
              may require additional licensing depending on the hero characters depicted.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">7. Disclaimers and Limitations</h2>
            <p className="text-gray-700 leading-relaxed mb-2">
              The Service is provided "AS IS" without warranties of any kind. We do not guarantee:
            </p>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li>Uninterrupted or error-free operation</li>
              <li>Specific quality or accuracy of generated images</li>
              <li>Storage or availability of uploaded or generated images beyond the session</li>
            </ul>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">8. Limitation of Liability</h2>
            <p className="text-gray-700 leading-relaxed">
              To the maximum extent permitted by law, Superpets shall not be liable for any indirect, incidental,
              special, consequential, or punitive damages arising from your use of the Service.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">9. Termination</h2>
            <p className="text-gray-700 leading-relaxed">
              We reserve the right to suspend or terminate your account at any time for violation of these terms
              or for any other reason. Upon termination, unused credits are forfeited without refund.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">10. Changes to Terms</h2>
            <p className="text-gray-700 leading-relaxed">
              We may modify these terms at any time. Continued use of the Service after changes constitutes
              acceptance of the updated terms.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">11. Governing Law</h2>
            <p className="text-gray-700 leading-relaxed">
              These terms are governed by the laws of the jurisdiction where Superpets operates, without regard
              to conflict of law principles.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">12. Contact</h2>
            <p className="text-gray-700 leading-relaxed">
              For questions about these terms, please contact us through our support channels.
            </p>
          </section>
        </div>
      </div>
    </div>
  );
}
