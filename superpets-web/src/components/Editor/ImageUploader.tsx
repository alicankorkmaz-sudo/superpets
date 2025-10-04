import { useRef } from 'react';
import { Upload, X, Image } from 'lucide-react';

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
        <div className="flex items-center gap-2">
          <Image size={20} className="text-gray-600" />
          <h3 className="font-semibold text-gray-700">Upload Pet Image</h3>
        </div>
        {file && (
          <span className="flex items-center gap-1 text-sm bg-green-100 text-green-700 px-3 py-1 rounded-full font-medium">
            ✓ Image ready
          </span>
        )}
      </div>

      <div
        onClick={() => inputRef.current?.click()}
        className={`border-2 border-dashed rounded-xl p-10 text-center cursor-pointer transition-all ${
          file
            ? 'border-green-300 bg-green-50 hover:bg-green-100'
            : 'border-gray-300 bg-gray-50 hover:border-primary-500 hover:bg-primary-50'
        }`}
      >
        <Upload className={`mx-auto h-12 w-12 mb-3 ${file ? 'text-green-500' : 'text-gray-400'}`} />
        <p className={`font-medium mb-1 ${file ? 'text-green-700' : 'text-gray-600'}`}>
          {file ? 'Change image' : 'Click to upload or drag and drop'}
        </p>
        <p className="text-sm text-gray-500">JPEG or PNG format</p>

        <input
          ref={inputRef}
          type="file"
          accept="image/jpeg,image/png"
          onChange={handleFileSelect}
          className="hidden"
        />
      </div>

      {file && (
        <div className="relative group rounded-xl overflow-hidden shadow-lg border-2 border-gray-200">
          <img
            src={URL.createObjectURL(file)}
            alt="Upload preview"
            className="w-full h-72 object-cover"
          />
          <button
            onClick={(e) => {
              e.stopPropagation();
              removeFile();
            }}
            className="absolute top-3 right-3 bg-red-500 hover:bg-red-600 text-white p-2 rounded-full opacity-0 group-hover:opacity-100 transition-all shadow-lg"
          >
            <X size={18} />
          </button>
          <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/60 to-transparent p-4">
            <p className="text-white text-sm font-medium">{file.name}</p>
          </div>
        </div>
      )}
    </div>
  );
}


