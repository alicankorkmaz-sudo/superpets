import { supabase } from './supabase';
import type {
  User,
  EditImageRequest,
  EditImageResponse,
  EditHistory,
  Transaction,
  CreditBalance,
  HeroesResponse
} from './types';
import * as Sentry from '@sentry/react';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

async function getAuthToken(): Promise<string> {
  const { data: { session } } = await supabase.auth.getSession();
  if (!session) throw new Error('Not authenticated');
  return session.access_token;
}

async function apiCall<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  try {
    const token = await getAuthToken();

    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'Authorization': `Bearer ${token}`,
        ...options.headers,
      },
    });

    if (response.status === 401) {
      throw new Error('UNAUTHORIZED');
    }

    if (response.status === 402) {
      throw new Error('INSUFFICIENT_CREDITS');
    }

    if (!response.ok) {
      const errorText = await response.text();
      const error = new Error(errorText || 'Request failed');

      // Report API errors to Sentry
      Sentry.captureException(error, {
        contexts: {
          api: {
            endpoint,
            status: response.status,
            statusText: response.statusText,
          },
        },
      });

      throw error;
    }

    return response.json();
  } catch (error) {
    // Don't re-report errors we already reported
    if (error instanceof Error && error.message !== 'UNAUTHORIZED' && error.message !== 'INSUFFICIENT_CREDITS') {
      Sentry.captureException(error, {
        contexts: {
          api: {
            endpoint,
          },
        },
      });
    }
    throw error;
  }
}

export const api = {
  // Heroes endpoint (public, no auth required)
  async getHeroes(): Promise<HeroesResponse> {
    const response = await fetch(`${API_BASE_URL}/heroes`);
    if (!response.ok) {
      throw new Error('Failed to fetch heroes');
    }
    return response.json();
  },

  // User endpoints
  async getUserProfile(): Promise<User> {
    return apiCall<User>('/user/profile');
  },

  async getCredits(): Promise<CreditBalance> {
    return apiCall<CreditBalance>('/user/credits');
  },

  async addCredits(amount: number, description: string): Promise<{ success: boolean; credits: number }> {
    return apiCall('/user/credits/add', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ amount, description }),
    });
  },

  async getTransactions(): Promise<{ transactions: Transaction[] }> {
    return apiCall('/user/transactions');
  },

  async getEditHistory(): Promise<{ edits: EditHistory[] }> {
    return apiCall('/user/edits');
  },

  // Image editing - URL-based
  async editImage(request: EditImageRequest): Promise<EditImageResponse> {
    return apiCall('/nano-banana/edit', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(request),
    });
  },

  // Image editing - File upload
  async uploadAndEdit(
    file: File,
    options: {
      heroId: string;
      numImages?: number;
      outputFormat?: 'jpeg' | 'png';
    }
  ): Promise<EditImageResponse> {
    const token = await getAuthToken();
    const formData = new FormData();

    formData.append('file', file);
    formData.append('hero_id', options.heroId);
    formData.append('num_images', (options.numImages ?? 1).toString());
    formData.append('output_format', options.outputFormat ?? 'jpeg');

    const response = await fetch(`${API_BASE_URL}/nano-banana/upload-and-edit`, {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}` },
      body: formData,
    });

    if (response.status === 401) throw new Error('UNAUTHORIZED');
    if (response.status === 402) throw new Error('INSUFFICIENT_CREDITS');
    if (!response.ok) throw new Error(await response.text());

    return response.json();
  },

  // Stripe payment endpoints
  async createCheckoutSession(credits: number): Promise<{ sessionId: string; url: string }> {
    return apiCall('/stripe/create-checkout-session', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ credits }),
    });
  },

  // Admin endpoints
  async getAdminStats(): Promise<any> {
    return apiCall('/admin/stats');
  },

  async getAdminUsers(limit?: number, offset?: number): Promise<any> {
    const params = new URLSearchParams();
    if (limit) params.append('limit', limit.toString());
    if (offset) params.append('offset', offset.toString());
    const query = params.toString() ? `?${params.toString()}` : '';
    return apiCall(`/admin/users${query}`);
  },

  async updateUser(userId: string, updates: { isAdmin?: boolean; credits?: number }): Promise<any> {
    return apiCall('/admin/users/update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ userId, ...updates }),
    });
  },

  async getAdminTransactions(limit?: number, offset?: number): Promise<any> {
    const params = new URLSearchParams();
    if (limit) params.append('limit', limit.toString());
    if (offset) params.append('offset', offset.toString());
    const query = params.toString() ? `?${params.toString()}` : '';
    return apiCall(`/admin/transactions${query}`);
  },

  async getAdminEdits(limit?: number, offset?: number): Promise<any> {
    const params = new URLSearchParams();
    if (limit) params.append('limit', limit.toString());
    if (offset) params.append('offset', offset.toString());
    const query = params.toString() ? `?${params.toString()}` : '';
    return apiCall(`/admin/edits${query}`);
  },
};
