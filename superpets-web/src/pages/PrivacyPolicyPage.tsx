export function PrivacyPolicyPage() {
  return (
    <div className="max-w-4xl mx-auto px-4 py-12 sm:px-6 lg:px-8">
      <div className="bg-white rounded-lg shadow-sm p-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-6">Privacy Policy</h1>
        <p className="text-sm text-gray-500 mb-8">Last Updated: October 8, 2025</p>

        <div className="prose prose-gray max-w-none space-y-6">
          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">1. Introduction</h2>
            <p className="text-gray-700 leading-relaxed">
              Superpets ("we", "us", or "our") is committed to protecting your privacy. This Privacy Policy
              explains how we collect, use, disclose, and safeguard your information when you use our Service.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">2. Information We Collect</h2>

            <h3 className="text-lg font-semibold text-gray-800 mb-2 mt-4">2.1 Information You Provide</h3>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li><strong>Account Information:</strong> Email address and password (managed via Supabase Auth)</li>
              <li><strong>Payment Information:</strong> Processed securely through Stripe (we do not store credit card details)</li>
              <li><strong>Images:</strong> Pet photos you upload for editing</li>
            </ul>

            <h3 className="text-lg font-semibold text-gray-800 mb-2 mt-4">2.2 Automatically Collected Information</h3>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li>Device information and browser type</li>
              <li>IP address and location data</li>
              <li>Usage data and analytics</li>
              <li>Authentication tokens and session data</li>
            </ul>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">3. How We Use Your Information</h2>
            <p className="text-gray-700 leading-relaxed mb-2">We use the collected information to:</p>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li>Provide and maintain the image editing Service</li>
              <li>Process your images using AI models (via fal.ai)</li>
              <li>Manage your account and credit balance</li>
              <li>Process payments and transactions</li>
              <li>Send important service notifications</li>
              <li>Improve and optimize the Service</li>
              <li>Prevent fraud and abuse</li>
              <li>Comply with legal obligations</li>
            </ul>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">4. How We Share Your Information</h2>
            <p className="text-gray-700 leading-relaxed mb-2">We share information with:</p>

            <h3 className="text-lg font-semibold text-gray-800 mb-2 mt-4">4.1 Service Providers</h3>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li><strong>Supabase:</strong> Authentication and database services</li>
              <li><strong>fal.ai:</strong> AI image processing (your uploaded images are sent to their API)</li>
              <li><strong>Stripe:</strong> Payment processing</li>
              <li><strong>Firebase:</strong> Hosting and infrastructure</li>
              <li><strong>Render:</strong> Backend server hosting</li>
            </ul>

            <h3 className="text-lg font-semibold text-gray-800 mb-2 mt-4">4.2 Legal Requirements</h3>
            <p className="text-gray-700 leading-relaxed">
              We may disclose your information if required by law, subpoena, or other legal process, or to
              protect our rights and safety.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">5. Data Storage and Security</h2>
            <p className="text-gray-700 leading-relaxed mb-2">
              <strong>Storage:</strong> Your account data is stored in Supabase (PostgreSQL database).
              Uploaded images are temporarily stored during processing and are not permanently retained.
            </p>
            <p className="text-gray-700 leading-relaxed mb-2">
              <strong>Security:</strong> We implement industry-standard security measures including:
            </p>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li>HTTPS encryption for all data transmission</li>
              <li>JWT-based authentication with secure tokens</li>
              <li>Encrypted password storage via Supabase Auth</li>
              <li>Regular security audits and updates</li>
            </ul>
            <p className="text-gray-700 leading-relaxed mt-2">
              However, no method of transmission over the Internet is 100% secure. We cannot guarantee
              absolute security.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">6. Image Processing and Retention</h2>
            <p className="text-gray-700 leading-relaxed mb-2">
              <strong>Uploaded Images:</strong> Images you upload are sent to fal.ai for AI processing.
              We do not permanently store your original or generated images on our servers beyond the
              active session.
            </p>
            <p className="text-gray-700 leading-relaxed">
              <strong>Third-Party Processing:</strong> fal.ai may temporarily store images according to
              their own privacy policy. We recommend reviewing their privacy policy separately.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">7. Your Rights and Choices</h2>
            <p className="text-gray-700 leading-relaxed mb-2">You have the right to:</p>
            <ul className="list-disc list-inside space-y-1 text-gray-700 ml-4">
              <li><strong>Access:</strong> Request access to your personal data</li>
              <li><strong>Correction:</strong> Request correction of inaccurate data</li>
              <li><strong>Deletion:</strong> Request deletion of your account and data</li>
              <li><strong>Data Portability:</strong> Request a copy of your data in a portable format</li>
              <li><strong>Opt-Out:</strong> Opt out of marketing communications (if any)</li>
            </ul>
            <p className="text-gray-700 leading-relaxed mt-2">
              To exercise these rights, please contact us through our support channels.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">8. Cookies and Tracking</h2>
            <p className="text-gray-700 leading-relaxed">
              We use cookies and similar technologies for authentication, session management, and analytics.
              Essential cookies are required for the Service to function. You can control non-essential cookies
              through your browser settings.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">9. Children's Privacy</h2>
            <p className="text-gray-700 leading-relaxed">
              The Service is not intended for users under 13 years of age. We do not knowingly collect
              personal information from children. If you believe we have collected information from a child,
              please contact us immediately.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">10. International Data Transfers</h2>
            <p className="text-gray-700 leading-relaxed">
              Your information may be transferred to and processed in countries other than your country of
              residence. These countries may have different data protection laws. By using the Service,
              you consent to such transfers.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">11. Changes to This Policy</h2>
            <p className="text-gray-700 leading-relaxed">
              We may update this Privacy Policy from time to time. We will notify you of significant changes
              by posting the new policy with an updated "Last Updated" date. Continued use of the Service
              constitutes acceptance of the updated policy.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">12. Contact Us</h2>
            <p className="text-gray-700 leading-relaxed">
              For questions or concerns about this Privacy Policy or our data practices, please contact us
              through our support channels.
            </p>
          </section>

          <section>
            <h2 className="text-xl font-semibold text-gray-900 mb-3">13. Data Protection Officer</h2>
            <p className="text-gray-700 leading-relaxed">
              If you are located in the European Economic Area (EEA) or UK, you have the right to contact
              our Data Protection Officer regarding data protection matters.
            </p>
          </section>
        </div>
      </div>
    </div>
  );
}
