import { useState } from 'react';
import { ImageUploader } from '../components/Editor/ImageUploader';
import { HeroSelector } from '../components/Editor/HeroSelector';
import { OutputSettings } from '../components/Editor/OutputSettings';
import { ResultsGallery } from '../components/Editor/ResultsGallery';
import { useImageEdit } from '../hooks/useImageEdit';
import { useCredits } from '../contexts/CreditsContext';
import { Loader2 } from 'lucide-react';

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
      <div className="card">
        <h2 className="text-2xl font-bold mb-6">Create AI-Edited Pet Images</h2>

        <div className="space-y-6">
          <ImageUploader file={file} onFileChange={setFile} />
          <HeroSelector selectedHeroId={selectedHeroId} onHeroSelect={setSelectedHeroId} />
          <OutputSettings
            numImages={numImages}
            onNumImagesChange={handleNumImagesChange}
            outputFormat={outputFormat}
            onOutputFormatChange={setOutputFormat}
          />

          {(error || validationError) && (
            <div className="bg-red-50 border border-red-200 text-red-700 p-4 rounded-lg">
              {error || validationError}
            </div>
          )}

          <button
            onClick={handleGenerate}
            disabled={!canGenerate}
            className="btn-primary w-full flex items-center justify-center gap-2"
          >
            {loading ? (
              <>
                <Loader2 className="animate-spin" size={20} />
                Generating...
              </>
            ) : (
              <>Generate Images ({numImages} {numImages === 1 ? 'credit' : 'credits'})</>
            )}
          </button>
        </div>
      </div>

      {result && <ResultsGallery images={result.images} description={result.description} />}
    </div>
  );
}


