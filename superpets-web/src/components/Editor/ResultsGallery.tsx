import { Download, Sparkles } from 'lucide-react';
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
    <div className="card shadow-lg animate-fade-in">
      <div className="flex items-start gap-2 sm:gap-3 mb-4 sm:mb-6">
        <Sparkles className="text-primary-600 mt-1 flex-shrink-0" size={24} />
        <div>
          <h3 className="text-xl sm:text-2xl font-bold text-gray-900 mb-1 sm:mb-2">Your Superhero Pets!</h3>
          <p className="text-sm sm:text-base text-gray-600">{description}</p>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 sm:gap-6">
        {images.map((image, index) => (
          <div
            key={index}
            className="relative group rounded-xl overflow-hidden shadow-md hover:shadow-2xl transition-all duration-300 animate-scale-in"
            style={{ animationDelay: `${index * 100}ms` }}
          >
            <img
              src={image.url}
              alt={`Result ${index + 1}`}
              className="w-full aspect-square object-cover transform group-hover:scale-105 transition-transform duration-300"
            />
            <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-end justify-between p-4">
              <span className="text-white font-medium text-sm">Image {index + 1}</span>
              <button
                onClick={() => downloadImage(image.url, index)}
                className="bg-white text-gray-800 p-3 rounded-full hover:bg-primary-500 hover:text-white transition-all shadow-lg transform hover:scale-110"
                title="Download image"
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


