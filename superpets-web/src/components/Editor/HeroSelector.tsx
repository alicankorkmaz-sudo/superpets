import { useState, useEffect } from 'react';
import { api } from '../../lib/api';
import type { Hero, HeroesResponse } from '../../lib/types';
import { Loader2, Shuffle, Star } from 'lucide-react';

interface HeroSelectorProps {
  selectedHeroId: string | null;
  onHeroSelect: (heroId: string | null) => void;
}

const POPULAR_HERO_COUNT = 6;

export function HeroSelector({ selectedHeroId, onHeroSelect }: HeroSelectorProps) {
  const [heroes, setHeroes] = useState<HeroesResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

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

  const allHeroes = [...heroes.classics, ...heroes.uniques];

  const selectRandomHero = () => {
    const randomIndex = Math.floor(Math.random() * allHeroes.length);
    onHeroSelect(allHeroes[randomIndex].id);
  };

  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <span className="text-sm font-medium text-gray-700">Pick a hero</span>
        <button
          onClick={selectRandomHero}
          className="flex items-center gap-1.5 px-3 py-1.5 text-sm font-medium text-primary-600 hover:text-primary-700 hover:bg-primary-50 rounded-lg transition-colors"
        >
          <Shuffle size={16} />
          Random
        </button>
      </div>

      <div className="grid grid-cols-3 sm:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-2 max-h-[320px] overflow-y-auto p-2 bg-gray-50 border border-gray-200 rounded-xl">
        {allHeroes.map((hero, index) => (
          <HeroCard
            key={hero.id}
            hero={hero}
            isSelected={selectedHeroId === hero.id}
            isPopular={index < POPULAR_HERO_COUNT}
            onSelect={() => onHeroSelect(hero.id === selectedHeroId ? null : hero.id)}
          />
        ))}
      </div>
    </div>
  );
}

interface HeroCardProps {
  hero: Hero;
  isSelected: boolean;
  isPopular: boolean;
  onSelect: () => void;
}

function HeroCard({ hero, isSelected, isPopular, onSelect }: HeroCardProps) {
  return (
    <button
      onClick={onSelect}
      className={`p-2 sm:p-3 rounded-lg border-2 transition-all text-center relative ${
        isSelected
          ? 'border-primary-600 bg-primary-50 shadow-md'
          : 'border-gray-200 bg-white hover:border-gray-300 hover:shadow-sm'
      }`}
    >
      {isPopular && (
        <div className="absolute -top-1 -right-1">
          <Star size={12} className="fill-amber-400 text-amber-400" />
        </div>
      )}
      <span className="font-medium text-xs sm:text-sm text-gray-900 line-clamp-2">
        {hero.hero}
      </span>
    </button>
  );
}
