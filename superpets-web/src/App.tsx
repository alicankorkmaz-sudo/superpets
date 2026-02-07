import { useAuth } from './hooks/useAuth';
import { Header } from './components/Dashboard/Header';
import { Footer } from './components/Dashboard/Footer';
import { LoginForm } from './components/Auth/LoginForm';
import { SignupForm } from './components/Auth/SignupForm';
import { LandingPage } from './pages/LandingPage';
import { EditorPage } from './pages/EditorPage';
import { PricingPage } from './pages/PricingPage';
import { TermsOfServicePage } from './pages/TermsOfServicePage';
import { PrivacyPolicyPage } from './pages/PrivacyPolicyPage';
import { AdminDashboardPage } from './pages/AdminDashboardPage';
import { AuthCallbackPage } from './pages/AuthCallbackPage';
import { CreditsProvider } from './contexts/CreditsContext';
import { useState, useEffect } from 'react';
import { CheckCircle, XCircle, Loader2 } from 'lucide-react';
import * as Sentry from '@sentry/react';

type View = 'editor' | 'pricing' | 'terms' | 'privacy' | 'admin';
type AuthView = 'landing' | 'login' | 'signup';

function App() {
  const { user, loading } = useAuth();
  const [currentView, setCurrentView] = useState<View>('editor');
  const [notification, setNotification] = useState<{ type: 'success' | 'error'; message: string } | null>(null);

  // Set Sentry user context when user logs in
  useEffect(() => {
    if (user) {
      Sentry.setUser({
        id: user.id,
        email: user.email,
      });
    } else {
      Sentry.setUser(null);
    }
  }, [user]);

  // Get auth view from URL
  const getAuthViewFromUrl = (): AuthView => {
    const params = new URLSearchParams(window.location.search);
    const view = params.get('auth');
    if (view === 'login' || view === 'signup') return view;
    return 'landing';
  };

  const [authView, setAuthView] = useState<AuthView>(getAuthViewFromUrl());

  // Navigate to auth view with URL update
  const navigateToAuthView = (view: AuthView) => {
    setAuthView(view);
    if (view === 'landing') {
      window.history.pushState({}, '', '/');
    } else {
      window.history.pushState({}, '', `/?auth=${view}`);
    }
  };

  // Handle browser back/forward buttons
  useEffect(() => {
    const handlePopState = () => {
      setAuthView(getAuthViewFromUrl());
    };

    window.addEventListener('popstate', handlePopState);
    return () => window.removeEventListener('popstate', handlePopState);
  }, []);

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

  // Handle email confirmation callback
  if (window.location.pathname === '/auth/callback') {
    return <AuthCallbackPage />;
  }

  // Handle legal pages (accessible to all users)
  if (window.location.pathname === '/terms') {
    return <TermsOfServicePage />;
  }
  if (window.location.pathname === '/privacy') {
    return <PrivacyPolicyPage />;
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <Loader2 size={40} className="animate-spin text-primary-500 mx-auto" />
          <p className="mt-4 text-gray-600">Loading...</p>
        </div>
      </div>
    );
  }

  if (!user) {
    if (authView === 'landing') {
      return (
        <LandingPage
          onGetStarted={() => navigateToAuthView('signup')}
          onLogin={() => navigateToAuthView('login')}
        />
      );
    }

    return (
      <div className="min-h-screen bg-gradient-to-br from-primary-50 to-secondary-50 flex items-center justify-center p-4">
        {authView === 'signup' ? (
          <SignupForm
            onSwitchToLogin={() => navigateToAuthView('login')}
            onBack={() => navigateToAuthView('landing')}
          />
        ) : (
          <LoginForm
            onSwitchToSignup={() => navigateToAuthView('signup')}
            onBack={() => navigateToAuthView('landing')}
          />
        )}
      </div>
    );
  }

  return (
    <CreditsProvider>
      <div className="min-h-screen bg-gray-50 flex flex-col">
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

        <div className="flex-grow">
          {currentView === 'editor' && <EditorPage />}
          {currentView === 'pricing' && <PricingPage />}
          {currentView === 'terms' && <TermsOfServicePage />}
          {currentView === 'privacy' && <PrivacyPolicyPage />}
          {currentView === 'admin' && <AdminDashboardPage />}
        </div>

        <Footer onNavigate={setCurrentView} />
      </div>
    </CreditsProvider>
  );
}

export default App
