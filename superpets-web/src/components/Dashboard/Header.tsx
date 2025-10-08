import { LogOut, Coins, RefreshCw, ShoppingCart, Home } from 'lucide-react';
import { useAuth } from '../../hooks/useAuth';
import { useCredits } from '../../contexts/CreditsContext';

interface HeaderProps {
  currentView?: 'editor' | 'pricing' | 'terms' | 'privacy';
  onNavigate?: (view: 'editor' | 'pricing' | 'terms' | 'privacy') => void;
}

export function Header({ currentView = 'editor', onNavigate }: HeaderProps) {
  const { user, signOut } = useAuth();
  const { credits, refreshing } = useCredits();

  console.log('🎯 Header rendering with credits:', credits);

  return (
    <header className="bg-white shadow-sm sticky top-0 z-10">
      <div className="max-w-7xl mx-auto px-4 py-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold bg-gradient-to-r from-primary-500 to-secondary-500 bg-clip-text text-transparent">
            🐾 Superpets
          </h1>

          <div className="flex items-center gap-6">
            <button
              onClick={() => onNavigate?.('editor')}
              className={`flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                currentView === 'editor'
                  ? 'bg-primary-100 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              <Home size={20} />
              <span>Editor</span>
            </button>

            <button
              onClick={() => onNavigate?.('pricing')}
              className={`flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                currentView === 'pricing'
                  ? 'bg-primary-100 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              <ShoppingCart size={20} />
              <span>Buy Credits</span>
            </button>

            <div className="flex items-center gap-2 bg-gradient-to-r from-primary-500 to-secondary-500 text-white px-4 py-2 rounded-full">
              <Coins size={20} />
              <span className="font-bold">{credits}</span>
              <span className="text-sm">credits</span>
              {refreshing && (
                <RefreshCw size={16} className="animate-spin ml-1" />
              )}
            </div>

            <div className="text-sm text-gray-600">
              {user?.email}
            </div>

            <button
              onClick={signOut}
              className="flex items-center gap-2 text-gray-600 hover:text-gray-800 transition-colors"
            >
              <LogOut size={20} />
              <span>Sign Out</span>
            </button>
          </div>
        </div>
      </div>
    </header>
  );
}


