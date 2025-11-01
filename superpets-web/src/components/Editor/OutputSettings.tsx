import { Settings, Image as ImageIcon } from 'lucide-react';

interface OutputSettingsProps {
  numImages: number;
  onNumImagesChange: (num: number) => void;
  outputFormat: 'jpeg' | 'png';
  onOutputFormatChange: (format: 'jpeg' | 'png') => void;
}

export function OutputSettings({
  numImages,
  onNumImagesChange,
  outputFormat,
  onOutputFormatChange,
}: OutputSettingsProps) {
  return (
    <div className="space-y-4 sm:space-y-6 p-3 sm:p-5 bg-gradient-to-br from-gray-50 to-white rounded-xl border border-gray-200">
      <div className="flex items-center gap-2 mb-2 sm:mb-4">
        <Settings size={18} className="sm:w-5 sm:h-5 text-gray-600" />
        <h3 className="text-sm sm:text-base font-semibold text-gray-700">Output Settings</h3>
      </div>

      <div>
        <label className="block text-xs sm:text-sm font-semibold text-gray-700 mb-2 sm:mb-3">
          Number of Variations: <span className="text-primary-600">{numImages}</span>
        </label>
        <input
          type="range"
          min="1"
          max="10"
          value={numImages}
          onChange={(e) => onNumImagesChange(parseInt(e.target.value))}
          className="w-full h-2.5 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-primary-500"
        />
        <div className="relative w-full mt-2 pb-6">
          {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((num) => {
            const position = ((num - 1) / (10 - 1)) * 100;
            return (
              <span
                key={num}
                className={`absolute text-xs -translate-x-1/2 ${
                  numImages === num ? 'font-bold text-primary-600' : 'text-gray-400'
                }`}
                style={{ left: `${position}%` }}
              >
                {num}
              </span>
            );
          })}
        </div>
      </div>

      <div>
        <label className="flex items-center gap-2 text-xs sm:text-sm font-semibold text-gray-700 mb-2 sm:mb-3">
          <ImageIcon size={14} className="sm:w-4 sm:h-4" />
          Output Format
        </label>
        <div className="flex gap-2 sm:gap-3">
          <label className={`flex-1 flex items-center justify-center gap-2 p-2 sm:p-3 rounded-lg border-2 cursor-pointer transition-all text-sm sm:text-base ${
            outputFormat === 'jpeg'
              ? 'border-primary-600 bg-primary-50 text-primary-700 font-medium'
              : 'border-gray-200 bg-white hover:border-gray-300'
          }`}>
            <input
              type="radio"
              value="jpeg"
              checked={outputFormat === 'jpeg'}
              onChange={(e) => onOutputFormatChange(e.target.value as 'jpeg')}
              className="sr-only"
            />
            <span>JPEG</span>
            {outputFormat === 'jpeg' && <span className="text-xs">✓</span>}
          </label>
          <label className={`flex-1 flex items-center justify-center gap-2 p-2 sm:p-3 rounded-lg border-2 cursor-pointer transition-all text-sm sm:text-base ${
            outputFormat === 'png'
              ? 'border-primary-600 bg-primary-50 text-primary-700 font-medium'
              : 'border-gray-200 bg-white hover:border-gray-300'
          }`}>
            <input
              type="radio"
              value="png"
              checked={outputFormat === 'png'}
              onChange={(e) => onOutputFormatChange(e.target.value as 'png')}
              className="sr-only"
            />
            <span>PNG</span>
            {outputFormat === 'png' && <span className="text-xs">✓</span>}
          </label>
        </div>
      </div>
    </div>
  );
}


