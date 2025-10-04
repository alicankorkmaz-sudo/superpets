import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import type { ReactNode } from 'react';
import { api } from '../lib/api';

interface CreditsContextType {
  credits: number;
  loading: boolean;
  refreshing: boolean;
  fetchCredits: () => Promise<void>;
  refreshCredits: () => Promise<void>;
  addCredits: (amount: number, description: string) => Promise<{ success: boolean; credits: number }>;
}

const CreditsContext = createContext<CreditsContextType | undefined>(undefined);

interface CreditsProviderProps {
  children: ReactNode;
}

export function CreditsProvider({ children }: CreditsProviderProps) {
  const [credits, setCredits] = useState<number>(0);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const fetchCredits = useCallback(async () => {
    try {
      const data = await api.getCredits();
      setCredits(data.credits);
    } catch (error) {
      console.error('Failed to fetch credits:', error);
    } finally {
      setLoading(false);
    }
  }, []);

  const refreshCredits = useCallback(async () => {
    setRefreshing(true);
    try {
      console.log('🔄 Refreshing credits in context...');
      const data = await api.getCredits();
      console.log('✅ Credits fetched and updated in context:', data.credits);
      setCredits(data.credits);
    } catch (error) {
      console.error('❌ Failed to refresh credits:', error);
    } finally {
      setRefreshing(false);
    }
  }, []);

  const addCredits = useCallback(async (amount: number, description: string) => {
    const result = await api.addCredits(amount, description);
    if (result.success) {
      setCredits(result.credits);
    }
    return result;
  }, []);

  useEffect(() => {
    fetchCredits();
  }, [fetchCredits]);

  const value = {
    credits,
    loading,
    refreshing,
    fetchCredits,
    refreshCredits,
    addCredits,
  };

  return (
    <CreditsContext.Provider value={value}>
      {children}
    </CreditsContext.Provider>
  );
}

export function useCredits() {
  const context = useContext(CreditsContext);
  if (context === undefined) {
    throw new Error('useCredits must be used within a CreditsProvider');
  }
  return context;
}
