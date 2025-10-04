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
    <div className="space-y-6">
      <div>
        <label className="block text-sm font-semibold text-gray-700 mb-3">
          Number of Variations: {numImages}
        </label>
        <input
          type="range"
          min="1"
          max="10"
          value={numImages}
          onChange={(e) => onNumImagesChange(parseInt(e.target.value))}
          className="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer accent-primary-500"
        />
        <div className="flex justify-between text-xs text-gray-500 mt-1">
          <span>1</span>
          <span>3</span>
          <span>5</span>
          <span>7</span>
          <span>10</span>
        </div>
      </div>

      <div>
        <label className="block text-sm font-semibold text-gray-700 mb-2">
          Output Format
        </label>
        <div className="flex gap-4">
          <label className="flex items-center cursor-pointer">
            <input
              type="radio"
              value="jpeg"
              checked={outputFormat === 'jpeg'}
              onChange={(e) => onOutputFormatChange(e.target.value as 'jpeg')}
              className="mr-2"
            />
            <span>JPEG</span>
          </label>
          <label className="flex items-center cursor-pointer">
            <input
              type="radio"
              value="png"
              checked={outputFormat === 'png'}
              onChange={(e) => onOutputFormatChange(e.target.value as 'png')}
              className="mr-2"
            />
            <span>PNG</span>
          </label>
        </div>
      </div>
    </div>
  );
}


