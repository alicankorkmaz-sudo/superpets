import { Sparkles, Loader2 } from 'lucide-react';
import { useMemo } from 'react';

interface LoadingProgressProps {
  progress: number; // 0-100
  currentStep: string;
  estimatedTimeLeft?: number; // in seconds
}

const PRO_TIPS = [
  { emoji: '💡', text: 'Each image uses 1 credit' },
  { emoji: '⚡', text: 'Try different heroes for unique styles' },
  { emoji: '🎨', text: 'Higher quality images work best' },
  { emoji: '🦸', text: 'Each generation is unique every time' },
  { emoji: '✨', text: 'You can generate up to 10 images at once' },
  { emoji: '🎯', text: 'Clear pet photos give better results' },
  { emoji: '🔥', text: 'Check out the gallery for inspiration' },
];

export function LoadingProgress({ progress, currentStep, estimatedTimeLeft }: LoadingProgressProps) {
  const randomTip = useMemo(() => {
    return PRO_TIPS[Math.floor(Math.random() * PRO_TIPS.length)];
  }, []);
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
      <div className="bg-white rounded-2xl shadow-2xl p-8 max-w-md w-full mx-4 space-y-6">
        {/* Animated Icon */}
        <div className="flex justify-center">
          <div className="relative">
            <div className="absolute inset-0 bg-gradient-to-r from-purple-400 to-pink-400 rounded-full blur-xl opacity-50 animate-pulse"></div>
            <div className="relative bg-gradient-to-r from-purple-500 to-pink-500 p-6 rounded-full">
              <Sparkles className="w-10 h-10 text-white animate-pulse" />
            </div>
          </div>
        </div>

        {/* Title */}
        <div className="text-center space-y-2">
          <h3 className="text-2xl font-bold text-gray-900">Creating Your Superhero</h3>
          <p className="text-gray-600">AI is working its magic...</p>
        </div>

        {/* Progress Bar */}
        <div className="space-y-3">
          <div className="relative h-3 bg-gray-200 rounded-full overflow-hidden">
            <div
              className="absolute inset-y-0 left-0 bg-gradient-to-r from-purple-500 to-pink-500 rounded-full transition-all duration-500 ease-out"
              style={{ width: `${progress}%` }}
            >
              {/* Animated shine effect */}
              <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/30 to-transparent animate-shimmer"></div>
            </div>
          </div>

          <div className="flex items-center justify-between text-sm">
            <span className="text-gray-700 font-medium">{currentStep}</span>
            <span className="text-gray-500">{Math.round(progress)}%</span>
          </div>
        </div>

        {/* Estimated Time */}
        {estimatedTimeLeft !== undefined && estimatedTimeLeft > 0 && (
          <div className="text-center">
            <p className="text-sm text-gray-500">
              <Loader2 className="inline-block w-4 h-4 mr-1 animate-spin" />
              About {estimatedTimeLeft} seconds remaining
            </p>
          </div>
        )}

        {/* Fun Tips */}
        <div className="bg-purple-50 border border-purple-200 rounded-lg p-4">
          <p className="text-sm text-purple-900 text-center">
            {randomTip.emoji} <span className="font-medium">Pro Tip:</span> {randomTip.text}
          </p>
        </div>
      </div>
    </div>
  );
}
