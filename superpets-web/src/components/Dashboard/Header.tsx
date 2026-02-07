import { LogOut, Coins, RefreshCw, ShoppingCart, Home, Shield, PawPrint } from 'lucide-react';
import { useAuth } from '../../hooks/useAuth';
import { useCredits } from '../../contexts/CreditsContext';
import { useState, useEffect } from 'react';
import { api } from '../../lib/api';
import type { User as AppUser } from '../../lib/types';

interface HeaderProps {
  currentView?: 'editor' | 'pricing' | 'terms' | 'privacy' | 'admin';
  onNavigate?: (view: 'editor' | 'pricing' | 'terms' | 'privacy' | 'admin') => void;
}

export function Header({ currentView = 'editor', onNavigate }: HeaderProps) {
  const { user, signOut } = useAuth();
  const { credits, refreshing } = useCredits();
  const [userProfile, setUserProfile] = useState<AppUser | null>(null);
  const [menuOpen, setMenuOpen] = useState(false);

  console.log('🎯 Header rendering with credits:', credits);

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const profile = await api.getUserProfile();
        setUserProfile(profile);
      } catch (error) {
        console.error('Failed to fetch user profile:', error);
      }
    };

    if (user) {
      fetchUserProfile();
    }
  }, [user]);

  return (
    <header className="bg-white shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 py-3 sm:py-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between">
          {/* Logo */}
          <h1 className="text-xl sm:text-2xl font-bold bg-gradient-to-r from-primary-500 to-secondary-500 bg-clip-text text-transparent flex-shrink-0 flex items-center gap-1.5">
            <PawPrint size={20} className="text-primary-500 sm:w-6 sm:h-6" />
            Superpets
          </h1>

          {/* Desktop Navigation */}
          <div className="hidden lg:flex items-center gap-3 xl:gap-6">
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

            {userProfile?.isAdmin && (
              <button
                onClick={() => onNavigate?.('admin')}
                className={`flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                  currentView === 'admin'
                    ? 'bg-purple-100 text-purple-700'
                    : 'text-gray-600 hover:bg-gray-100'
                }`}
              >
                <Shield size={20} />
                <span>Admin</span>
              </button>
            )}

            <div className="flex items-center gap-2 bg-gradient-to-r from-primary-500 to-secondary-500 text-white px-3 xl:px-4 py-2 rounded-full">
              <Coins size={18} />
              <span className="font-bold">{credits}</span>
              <span className="text-xs xl:text-sm">credits</span>
              {refreshing && (
                <RefreshCw size={14} className="animate-spin ml-1" />
              )}
            </div>

            <div className="text-xs xl:text-sm text-gray-600 max-w-[150px] truncate">
              {user?.email}
            </div>

            <button
              onClick={signOut}
              className="flex items-center gap-2 text-gray-600 hover:text-gray-800 transition-colors"
            >
              <LogOut size={20} />
              <span className="hidden xl:inline">Sign Out</span>
            </button>
          </div>

          {/* Mobile Menu */}
          <div className="lg:hidden flex items-center gap-3">
            {/* Credits Badge (Mobile) */}
            <div className="flex items-center gap-1.5 bg-gradient-to-r from-primary-500 to-secondary-500 text-white px-2.5 py-1.5 rounded-full text-sm">
              <Coins size={16} />
              <span className="font-bold">{credits}</span>
            </div>

            {/* Hamburger Menu */}
            <button
              onClick={() => setMenuOpen(!menuOpen)}
              className="p-2 text-gray-600 hover:text-gray-800"
              aria-label="Toggle menu"
            >
              {menuOpen ? (
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              ) : (
                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                </svg>
              )}
            </button>
          </div>
        </div>

        {/* Mobile Dropdown Menu */}
        {menuOpen && (
          <div className="lg:hidden mt-3 pt-3 border-t border-gray-200 space-y-2">
            <button
              onClick={() => {
                onNavigate?.('editor');
                setMenuOpen(false);
              }}
              className={`w-full flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                currentView === 'editor'
                  ? 'bg-primary-100 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              <Home size={20} />
              <span>Editor</span>
            </button>

            <button
              onClick={() => {
                onNavigate?.('pricing');
                setMenuOpen(false);
              }}
              className={`w-full flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                currentView === 'pricing'
                  ? 'bg-primary-100 text-primary-700'
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              <ShoppingCart size={20} />
              <span>Buy Credits</span>
            </button>

            {userProfile?.isAdmin && (
              <button
                onClick={() => {
                  onNavigate?.('admin');
                  setMenuOpen(false);
                }}
                className={`w-full flex items-center gap-2 px-3 py-2 rounded-lg transition-colors ${
                  currentView === 'admin'
                    ? 'bg-purple-100 text-purple-700'
                    : 'text-gray-600 hover:bg-gray-100'
                }`}
              >
                <Shield size={20} />
                <span>Admin</span>
              </button>
            )}

            <div className="px-3 py-2 text-sm text-gray-600 border-t border-gray-100 mt-2 pt-3">
              <div className="truncate">{user?.email}</div>
            </div>

            <button
              onClick={() => {
                signOut();
                setMenuOpen(false);
              }}
              className="w-full flex items-center gap-2 px-3 py-2 text-gray-600 hover:text-gray-800 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <LogOut size={20} />
              <span>Sign Out</span>
            </button>
          </div>
        )}
      </div>
    </header>
  );
}


