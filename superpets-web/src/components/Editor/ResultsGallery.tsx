import { Download } from 'lucide-react';
import type { ImageFile } from '../../lib/types';

interface ResultsGalleryProps {
  images: ImageFile[];
  description: string;
}

export function ResultsGallery({ images, description }: ResultsGalleryProps) {
  const downloadImage = async (url: string, index: number) => {
    const response = await fetch(url);
    const blob = await response.blob();
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `superpets-edit-${index + 1}.jpg`;
    link.click();
  };

  return (
    <div className="card">
      <h3 className="text-xl font-bold mb-2">Generated Images</h3>
      <p className="text-gray-600 text-sm mb-6">{description}</p>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {images.map((image, index) => (
          <div key={index} className="relative group">
            <img
              src={image.url}
              alt={`Result ${index + 1}`}
              className="w-full aspect-square object-cover rounded-lg"
            />
            <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-30 transition-all rounded-lg flex items-center justify-center">
              <button
                onClick={() => downloadImage(image.url, index)}
                className="opacity-0 group-hover:opacity-100 bg-white text-gray-800 p-3 rounded-full hover:bg-gray-100 transition-all"
              >
                <Download size={20} />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}


