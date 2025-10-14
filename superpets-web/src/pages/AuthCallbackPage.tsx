import { useEffect, useState } from 'react';
import { supabase } from '../lib/supabase';
import { CheckCircle, XCircle } from 'lucide-react';

export function AuthCallbackPage() {
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const handleEmailConfirmation = async () => {
      try {
        // The supabase client will automatically handle the confirmation
        // by detecting the tokens in the URL
        const { data, error } = await supabase.auth.getSession();

        if (error) {
          console.error('Email confirmation error:', error);
          setStatus('error');
          setErrorMessage(error.message || 'Failed to confirm email');
          return;
        }

        if (data.session) {
          setStatus('success');
          // Redirect to app after successful confirmation
          setTimeout(() => {
            window.location.href = '/';
          }, 2000);
        } else {
          setStatus('error');
          setErrorMessage('No session found. Please try signing up again.');
        }
      } catch (err: any) {
        console.error('Unexpected error during email confirmation:', err);
        setStatus('error');
        setErrorMessage(err.message || 'An unexpected error occurred');
      }
    };

    handleEmailConfirmation();
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-50 to-secondary-50 flex items-center justify-center p-4">
      <div className="card max-w-md mx-auto text-center">
        {status === 'loading' && (
          <>
            <div className="flex justify-center mb-6">
              <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-primary-500"></div>
            </div>
            <h2 className="text-2xl font-bold mb-4">Confirming Your Email...</h2>
            <p className="text-gray-600">Please wait while we verify your email address.</p>
          </>
        )}

        {status === 'success' && (
          <>
            <div className="flex justify-center mb-6">
              <div className="bg-green-100 p-4 rounded-full">
                <CheckCircle className="text-green-600" size={48} />
              </div>
            </div>
            <h2 className="text-2xl font-bold mb-4 text-green-600">Email Confirmed!</h2>
            <p className="text-gray-600 mb-4">
              Your email has been successfully verified.
            </p>
            <p className="text-sm text-gray-500">
              Redirecting you to the app...
            </p>
          </>
        )}

        {status === 'error' && (
          <>
            <div className="flex justify-center mb-6">
              <div className="bg-red-100 p-4 rounded-full">
                <XCircle className="text-red-600" size={48} />
              </div>
            </div>
            <h2 className="text-2xl font-bold mb-4 text-red-600">Confirmation Failed</h2>
            <p className="text-gray-600 mb-6">{errorMessage}</p>
            <button
              onClick={() => window.location.href = '/?auth=signup'}
              className="btn-primary w-full"
            >
              Back to Sign Up
            </button>
          </>
        )}
      </div>
    </div>
  );
}
