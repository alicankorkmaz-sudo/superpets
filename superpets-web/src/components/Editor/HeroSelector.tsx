import { useState, useEffect } from 'react';
import { api } from '../../lib/api';
import type { Hero, HeroesResponse } from '../../lib/types';
import { Loader2 } from 'lucide-react';

interface HeroSelectorProps {
  selectedHeroId: string | null;
  onHeroSelect: (heroId: string | null) => void;
}

export function HeroSelector({ selectedHeroId, onHeroSelect }: HeroSelectorProps) {
  const [heroes, setHeroes] = useState<HeroesResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<'classics' | 'uniques'>('classics');

  useEffect(() => {
    loadHeroes();
  }, []);

  const loadHeroes = async () => {
    try {
      setLoading(true);
      const data = await api.getHeroes();
      setHeroes(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load heroes');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-8">
        <Loader2 className="animate-spin" size={24} />
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 p-4 rounded-lg">
        {error}
      </div>
    );
  }

  if (!heroes) return null;

  const currentHeroes = activeTab === 'classics' ? heroes.classics : heroes.uniques;

  return (
    <div className="space-y-4">
      <div>
        <label className="block text-sm font-semibold text-gray-700 mb-2">
          Select a Hero (Optional)
        </label>
        <p className="text-xs text-gray-500 mb-3">
          Choose a hero to transform your pet, or use a custom prompt below
        </p>

        {/* Tabs */}
        <div className="flex gap-2 mb-4">
          <button
            onClick={() => setActiveTab('classics')}
            className={`px-4 py-2 rounded-lg font-medium transition-colors ${
              activeTab === 'classics'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            Classic Heroes ({heroes.classics.length})
          </button>
          <button
            onClick={() => setActiveTab('uniques')}
            className={`px-4 py-2 rounded-lg font-medium transition-colors ${
              activeTab === 'uniques'
                ? 'bg-blue-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            Unique Heroes ({heroes.uniques.length})
          </button>
        </div>

        {/* Hero Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-3 max-h-[400px] overflow-y-auto p-2 border border-gray-200 rounded-lg">
          {currentHeroes.map((hero) => (
            <HeroCard
              key={hero.id}
              hero={hero}
              isSelected={selectedHeroId === hero.id}
              onSelect={() => onHeroSelect(hero.id === selectedHeroId ? null : hero.id)}
            />
          ))}
        </div>

        {selectedHeroId && (
          <button
            onClick={() => onHeroSelect(null)}
            className="mt-3 text-sm text-blue-600 hover:text-blue-700 font-medium"
          >
            Clear selection
          </button>
        )}
      </div>
    </div>
  );
}

interface HeroCardProps {
  hero: Hero;
  isSelected: boolean;
  onSelect: () => void;
}

function HeroCard({ hero, isSelected, onSelect }: HeroCardProps) {
  return (
    <button
      onClick={onSelect}
      className={`p-3 rounded-lg border-2 transition-all text-left ${
        isSelected
          ? 'border-blue-600 bg-blue-50'
          : 'border-gray-200 bg-white hover:border-gray-300 hover:bg-gray-50'
      }`}
    >
      <div className="font-semibold text-sm text-gray-900 mb-1">{hero.hero}</div>
      <div className="text-xs text-gray-600 line-clamp-2">{hero.identity}</div>
      <div className="text-xs text-gray-400 mt-1">
        {hero.scene_options.length} scenes
      </div>
    </button>
  );
}
