import { useRef } from 'react';
import { Upload, X } from 'lucide-react';

interface ImageUploaderProps {
  file: File | null;
  onFileChange: (file: File | null) => void;
}

export function ImageUploader({ file, onFileChange }: ImageUploaderProps) {
  const inputRef = useRef<HTMLInputElement>(null);

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFile = e.target.files?.[0] || null;
    onFileChange(selectedFile);
  };

  const removeFile = () => {
    onFileChange(null);
    if (inputRef.current) {
      inputRef.current.value = '';
    }
  };

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h3 className="font-semibold text-gray-700">Upload Image</h3>
        {file && <span className="text-sm text-gray-500">1 image selected</span>}
      </div>

      <div
        onClick={() => inputRef.current?.click()}
        className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center cursor-pointer hover:border-primary-500 transition-colors"
      >
        <Upload className="mx-auto h-12 w-12 text-gray-400 mb-2" />
        <p className="text-gray-600">Click to upload or drag and drop</p>
        <p className="text-sm text-gray-500 mt-1">JPEG or PNG</p>

        <input
          ref={inputRef}
          type="file"
          accept="image/jpeg,image/png"
          onChange={handleFileSelect}
          className="hidden"
        />
      </div>

      {file && (
        <div className="relative group">
          <img
            src={URL.createObjectURL(file)}
            alt="Upload preview"
            className="w-full h-64 object-cover rounded-lg"
          />
          <button
            onClick={(e) => {
              e.stopPropagation();
              removeFile();
            }}
            className="absolute top-2 right-2 bg-red-500 text-white p-1 rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
          >
            <X size={16} />
          </button>
        </div>
      )}
    </div>
  );
}


