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
    <div className="space-y-6 p-5 bg-gradient-to-br from-gray-50 to-white rounded-xl border border-gray-200">
      <div className="flex items-center gap-2 mb-4">
        <Settings size={20} className="text-gray-600" />
        <h3 className="font-semibold text-gray-700">Output Settings</h3>
      </div>

      <div>
        <label className="block text-sm font-semibold text-gray-700 mb-3">
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
        <div className="flex justify-between text-xs text-gray-500 mt-2 px-1">
          <span className={numImages === 1 ? 'font-bold text-primary-600' : ''}>1</span>
          <span className={numImages === 3 ? 'font-bold text-primary-600' : ''}>3</span>
          <span className={numImages === 5 ? 'font-bold text-primary-600' : ''}>5</span>
          <span className={numImages === 7 ? 'font-bold text-primary-600' : ''}>7</span>
          <span className={numImages === 10 ? 'font-bold text-primary-600' : ''}>10</span>
        </div>
      </div>

      <div>
        <label className="flex items-center gap-2 text-sm font-semibold text-gray-700 mb-3">
          <ImageIcon size={16} />
          Output Format
        </label>
        <div className="flex gap-3">
          <label className={`flex-1 flex items-center justify-center gap-2 p-3 rounded-lg border-2 cursor-pointer transition-all ${
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
          <label className={`flex-1 flex items-center justify-center gap-2 p-3 rounded-lg border-2 cursor-pointer transition-all ${
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


