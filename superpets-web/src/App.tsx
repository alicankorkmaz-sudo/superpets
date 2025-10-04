import { useAuth } from './hooks/useAuth';
import { Header } from './components/Dashboard/Header';
import { LoginForm } from './components/Auth/LoginForm';
import { SignupForm } from './components/Auth/SignupForm';
import { EditorPage } from './pages/EditorPage';
import { PricingPage } from './pages/PricingPage';
import { CreditsProvider } from './contexts/CreditsContext';
import { useState, useEffect } from 'react';
import { CheckCircle, XCircle } from 'lucide-react';

type View = 'editor' | 'pricing';

function App() {
  const { user, loading } = useAuth();
  const [showSignup, setShowSignup] = useState(false);
  const [currentView, setCurrentView] = useState<View>('editor');
  const [notification, setNotification] = useState<{ type: 'success' | 'error'; message: string } | null>(null);

  // Check for payment status in URL
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const paymentStatus = params.get('payment');

    if (paymentStatus === 'success') {
      setNotification({
        type: 'success',
        message: 'Payment successful! Your credits have been added to your account.'
      });
      // Clear URL parameter
      window.history.replaceState({}, '', window.location.pathname);
      // Auto-dismiss after 5 seconds
      setTimeout(() => setNotification(null), 5000);
    } else if (paymentStatus === 'cancelled') {
      setNotification({
        type: 'error',
        message: 'Payment was cancelled. No charges were made.'
      });
      window.history.replaceState({}, '', window.location.pathname);
      setTimeout(() => setNotification(null), 5000);
    }
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-500 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-primary-50 to-secondary-50 flex items-center justify-center p-4">
        {showSignup ? (
          <SignupForm onSwitchToLogin={() => setShowSignup(false)} />
        ) : (
          <LoginForm onSwitchToSignup={() => setShowSignup(true)} />
        )}
      </div>
    );
  }

  return (
    <CreditsProvider>
      <div className="min-h-screen bg-gray-50">
        <Header currentView={currentView} onNavigate={setCurrentView} />

        {/* Payment Notification Toast */}
        {notification && (
          <div className="fixed top-20 right-4 z-50 animate-slide-in">
            <div className={`flex items-center gap-3 px-6 py-4 rounded-lg shadow-lg ${
              notification.type === 'success'
                ? 'bg-green-50 border border-green-200 text-green-800'
                : 'bg-red-50 border border-red-200 text-red-800'
            }`}>
              {notification.type === 'success' ? (
                <CheckCircle className="flex-shrink-0" size={24} />
              ) : (
                <XCircle className="flex-shrink-0" size={24} />
              )}
              <p className="font-medium">{notification.message}</p>
              <button
                onClick={() => setNotification(null)}
                className="ml-4 text-gray-500 hover:text-gray-700"
              >
                ✕
              </button>
            </div>
          </div>
        )}

        {currentView === 'editor' ? <EditorPage /> : <PricingPage />}
      </div>
    </CreditsProvider>
  );
}

export default App
