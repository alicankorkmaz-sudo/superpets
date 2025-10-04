import { useState } from 'react';
import { api } from '../lib/api';
import type { EditImageResponse } from '../lib/types';

export function useImageEdit() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<EditImageResponse | null>(null);

  const editImages = async (
    file: File,
    options: {
      heroId: string;
      numImages?: number;
      outputFormat?: 'jpeg' | 'png';
    }
  ) => {
    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await api.uploadAndEdit(file, options);
      setResult(response);
      return response;
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Unknown error';

      if (errorMessage === 'INSUFFICIENT_CREDITS') {
        setError('Insufficient credits. Please add more credits to continue.');
      } else if (errorMessage === 'UNAUTHORIZED') {
        setError('Session expired. Please log in again.');
      } else {
        setError(errorMessage);
      }
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { editImages, loading, error, result, setResult };
}


