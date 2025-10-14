import { useState } from 'react';
import { useAuth } from '../../hooks/useAuth';
import { ArrowLeft, Mail } from 'lucide-react';

export function SignupForm({ onSwitchToLogin, onBack }: { onSwitchToLogin: () => void; onBack?: () => void }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [confirmationPending, setConfirmationPending] = useState(false);
  const { signUp } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const result = await signUp(email, password);

      // Check if email confirmation is required
      if (result?.user && !result?.session) {
        setConfirmationPending(true);
      }
    } catch (err: any) {
      setError(err.message || 'Failed to sign up');
    } finally {
      setLoading(false);
    }
  };

  // Show confirmation message if email confirmation is pending
  if (confirmationPending) {
    return (
      <div className="card max-w-md mx-auto text-center">
        <div className="flex justify-center mb-6">
          <div className="bg-primary-100 p-4 rounded-full">
            <Mail className="text-primary-600" size={48} />
          </div>
        </div>
        <h2 className="text-2xl font-bold mb-4 bg-gradient-to-r from-primary-500 to-secondary-500 bg-clip-text text-transparent">
          Check Your Email
        </h2>
        <p className="text-gray-600 mb-6">
          We've sent a confirmation link to <strong>{email}</strong>.
        </p>
        <p className="text-gray-600 mb-6">
          Please click the link in the email to verify your account and complete the signup process.
        </p>
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
          <p className="text-sm text-blue-800">
            <strong>Important:</strong> The confirmation link will redirect you back to Superpets.
            Make sure to click it on the same device and browser.
          </p>
        </div>
        <button
          onClick={onSwitchToLogin}
          className="btn-secondary w-full"
        >
          Back to Login
        </button>
      </div>
    );
  }

  return (
    <div className="card max-w-md mx-auto">
      {onBack && (
        <button
          onClick={onBack}
          className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-4 transition-colors"
        >
          <ArrowLeft size={20} />
          <span>Back to home</span>
        </button>
      )}
      <h2 className="text-2xl font-bold mb-6 text-center bg-gradient-to-r from-primary-500 to-secondary-500 bg-clip-text text-transparent">
        Create your Superpets account
      </h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Email
          </label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="input-field"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Password
          </label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="input-field"
            required
          />
        </div>

        {error && (
          <div className="bg-red-50 text-red-600 p-3 rounded-lg text-sm">
            {error}
          </div>
        )}

        <button type="submit" className="btn-primary w-full" disabled={loading}>
          {loading ? 'Creating account...' : 'Sign Up'}
        </button>
      </form>

      <p className="mt-4 text-center text-sm text-gray-600">
        Already have an account?{' '}
        <button
          onClick={onSwitchToLogin}
          className="text-primary-500 hover:text-primary-600 font-semibold"
        >
          Sign in
        </button>
      </p>
    </div>
  );
}


