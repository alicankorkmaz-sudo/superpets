interface OutputSettingsProps {
  numImages: number;
  onNumImagesChange: (num: number) => void;
  outputFormat: 'jpeg' | 'png';
  onOutputFormatChange: (format: 'jpeg' | 'png') => void;
}

const PRESETS = [1, 3, 5];

export function OutputSettings({
  numImages,
  onNumImagesChange,
}: OutputSettingsProps) {
  return (
    <div className="space-y-3">
      <span className="text-sm font-medium text-gray-700">How many images?</span>
      <div className="flex gap-2">
        {PRESETS.map((num) => (
          <button
            key={num}
            onClick={() => onNumImagesChange(num)}
            className={`flex-1 py-2.5 px-4 rounded-lg font-medium text-sm transition-all ${
              numImages === num
                ? 'bg-primary-600 text-white shadow-md'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            {num}
          </button>
        ))}
      </div>
    </div>
  );
}
