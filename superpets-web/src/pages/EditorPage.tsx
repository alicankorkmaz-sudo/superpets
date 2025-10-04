import { useState } from 'react';
import { ImageUploader } from '../components/Editor/ImageUploader';
import { HeroSelector } from '../components/Editor/HeroSelector';
import { OutputSettings } from '../components/Editor/OutputSettings';
import { ResultsGallery } from '../components/Editor/ResultsGallery';
import { useImageEdit } from '../hooks/useImageEdit';
import { useCredits } from '../contexts/CreditsContext';
import { Loader2, Sparkles } from 'lucide-react';

export function EditorPage() {
  const [file, setFile] = useState<File | null>(null);
  const [selectedHeroId, setSelectedHeroId] = useState<string | null>(null);
  const [numImages, setNumImages] = useState(1);
  const [outputFormat, setOutputFormat] = useState<'jpeg' | 'png'>('jpeg');
  const [validationError, setValidationError] = useState<string | null>(null);

  const { editImages, loading, error, result } = useImageEdit();
  const { credits, refreshCredits } = useCredits();

  const handleNumImagesChange = (num: number) => {
    setNumImages(num);
    // Clear validation error when user changes the slider
    if (validationError) {
      setValidationError(null);
    }
  };

  const handleGenerate = async () => {
    if (!selectedHeroId || !file) return;

    // Validate credits before making the call
    if (credits < numImages) {
      setValidationError(`Insufficient credits. You have ${credits} credits but need ${numImages}.`);
      return;
    }

    setValidationError(null);

    try {
      await editImages(file, {
        heroId: selectedHeroId,
        numImages,
        outputFormat,
      });
    } catch (err) {
      // Error is already handled in hook
    } finally {
      // Always refresh credits after edit attempt (success or failure)
      console.log('Calling refreshCredits after edit attempt');
      await refreshCredits();
    }
  };

  const canGenerate = file !== null && selectedHeroId !== null && !loading && credits >= numImages;

  return (
    <div className="max-w-7xl mx-auto px-4 py-8 space-y-8">
      {/* Hero Section */}
      <div className="text-center mb-8">
        <h1 className="text-4xl font-bold bg-gradient-to-r from-primary-600 to-secondary-600 bg-clip-text text-transparent mb-3">
          Transform Your Pet into a Superhero
        </h1>
        <p className="text-gray-600 text-lg">
          Upload a photo, choose a hero, and watch the magic happen ✨
        </p>
      </div>

      <div className="card shadow-lg">
        <div className="space-y-6">
          <ImageUploader file={file} onFileChange={setFile} />

          <div className="border-t border-gray-200 pt-6">
            <HeroSelector selectedHeroId={selectedHeroId} onHeroSelect={setSelectedHeroId} />
          </div>

          <div className="border-t border-gray-200 pt-6">
            <OutputSettings
              numImages={numImages}
              onNumImagesChange={handleNumImagesChange}
              outputFormat={outputFormat}
              onOutputFormatChange={setOutputFormat}
            />
          </div>

          {(error || validationError) && (
            <div className="bg-red-50 border-l-4 border-red-500 text-red-700 p-4 rounded-lg flex items-start gap-3">
              <span className="text-xl">⚠️</span>
              <div>
                <p className="font-medium">Error</p>
                <p className="text-sm">{error || validationError}</p>
              </div>
            </div>
          )}

          <button
            onClick={handleGenerate}
            disabled={!canGenerate}
            className="btn-primary w-full flex items-center justify-center gap-2 text-lg py-4 shadow-lg hover:shadow-xl transition-all"
          >
            {loading ? (
              <>
                <Loader2 className="animate-spin" size={24} />
                <span>Generating Your Superhero...</span>
              </>
            ) : (
              <>
                <Sparkles size={24} />
                <span>Generate Images ({numImages} {numImages === 1 ? 'credit' : 'credits'})</span>
              </>
            )}
          </button>
        </div>
      </div>

      {result && <ResultsGallery images={result.images} description={result.description} />}
    </div>
  );
}


