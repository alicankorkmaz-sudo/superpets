import { useState, useEffect } from 'react';
import { api } from '../../lib/api';
import type { Hero, HeroesResponse } from '../../lib/types';
import { Loader2, Shield, Sparkles, Star, Zap } from 'lucide-react';

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
          Select a Hero
        </label>
        <p className="text-xs text-gray-500 mb-4">
          Transform your pet into an amazing superhero character
        </p>

        {/* Tabs */}
        <div className="flex gap-3 mb-4">
          <button
            onClick={() => setActiveTab('classics')}
            className={`flex items-center gap-2 px-5 py-2.5 rounded-lg font-medium transition-all shadow-sm ${
              activeTab === 'classics'
                ? 'bg-gradient-to-r from-blue-600 to-blue-700 text-white shadow-blue-200'
                : 'bg-white text-gray-700 hover:bg-gray-50 border border-gray-200'
            }`}
          >
            <Shield size={18} />
            <span>Classic Heroes</span>
            <span className={`ml-1 px-2 py-0.5 rounded-full text-xs ${
              activeTab === 'classics'
                ? 'bg-blue-500 text-white'
                : 'bg-gray-100 text-gray-600'
            }`}>
              {heroes.classics.length}
            </span>
          </button>
          <button
            onClick={() => setActiveTab('uniques')}
            className={`flex items-center gap-2 px-5 py-2.5 rounded-lg font-medium transition-all shadow-sm ${
              activeTab === 'uniques'
                ? 'bg-gradient-to-r from-purple-600 to-purple-700 text-white shadow-purple-200'
                : 'bg-white text-gray-700 hover:bg-gray-50 border border-gray-200'
            }`}
          >
            <Sparkles size={18} />
            <span>Unique Heroes</span>
            <span className={`ml-1 px-2 py-0.5 rounded-full text-xs ${
              activeTab === 'uniques'
                ? 'bg-purple-500 text-white'
                : 'bg-gray-100 text-gray-600'
            }`}>
              {heroes.uniques.length}
            </span>
          </button>
        </div>

        {/* Hero Grid */}
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-3 max-h-[450px] overflow-y-auto p-3 bg-gradient-to-b from-gray-50 to-white border border-gray-200 rounded-xl shadow-inner">
          {currentHeroes.map((hero) => (
            <HeroCard
              key={hero.id}
              hero={hero}
              isSelected={selectedHeroId === hero.id}
              onSelect={() => onHeroSelect(hero.id === selectedHeroId ? null : hero.id)}
              category={activeTab}
            />
          ))}
        </div>

        {selectedHeroId && (
          <button
            onClick={() => onHeroSelect(null)}
            className="mt-3 text-sm text-blue-600 hover:text-blue-700 font-medium flex items-center gap-1 transition-colors"
          >
            ✕ Clear selection
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
  category: 'classics' | 'uniques';
}

function HeroCard({ hero, isSelected, onSelect, category }: HeroCardProps) {
  const accentColor = category === 'classics' ? 'blue' : 'purple';

  return (
    <button
      onClick={onSelect}
      className={`group p-4 rounded-xl border-2 transition-all text-left relative overflow-hidden ${
        isSelected
          ? `border-${accentColor}-600 bg-gradient-to-br from-${accentColor}-50 to-white shadow-md scale-105`
          : 'border-gray-200 bg-white hover:border-gray-300 hover:shadow-md hover:-translate-y-0.5'
      }`}
    >
      {/* Icon indicator */}
      <div className={`absolute top-2 right-2 ${isSelected ? 'opacity-100' : 'opacity-0'} transition-opacity`}>
        <Star size={16} className={`fill-${accentColor}-600 text-${accentColor}-600`} />
      </div>

      {/* Hero icon */}
      <div className={`mb-2 ${isSelected ? `text-${accentColor}-600` : 'text-gray-400 group-hover:text-gray-600'} transition-colors`}>
        <Zap size={20} />
      </div>

      <div className="font-semibold text-sm text-gray-900 mb-1.5 line-clamp-1">{hero.hero}</div>
      <div className="text-xs text-gray-600 line-clamp-2 mb-2">{hero.identity}</div>

      {/* Scene count badge */}
      <div className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-xs font-medium ${
        isSelected
          ? `bg-${accentColor}-100 text-${accentColor}-700`
          : 'bg-gray-100 text-gray-600'
      }`}>
        <Sparkles size={10} />
        {hero.scene_options.length} scenes
      </div>
    </button>
  );
}
