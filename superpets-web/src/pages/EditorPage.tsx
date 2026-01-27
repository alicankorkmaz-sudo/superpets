import { useState, useEffect } from 'react';
import { ImageUploader } from '../components/Editor/ImageUploader';
import { HeroSelector } from '../components/Editor/HeroSelector';
import { OutputSettings } from '../components/Editor/OutputSettings';
import { ResultsGallery } from '../components/Editor/ResultsGallery';
import { LoadingProgress } from '../components/Editor/LoadingProgress';
import { useImageEdit } from '../hooks/useImageEdit';
import { useCredits } from '../contexts/CreditsContext';
import { Loader2, Sparkles } from 'lucide-react';

export function EditorPage() {
  const [file, setFile] = useState<File | null>(null);
  const [selectedHeroId, setSelectedHeroId] = useState<string | null>(null);
  const [numImages, setNumImages] = useState(1);
  const [outputFormat, setOutputFormat] = useState<'jpeg' | 'png'>('jpeg');
  const [validationError, setValidationError] = useState<string | null>(null);
  const [progress, setProgress] = useState(0);
  const [currentStep, setCurrentStep] = useState('');
  const [startTime, setStartTime] = useState<number | null>(null);

  const { editImages, loading, error, result } = useImageEdit();
  const { credits, refreshCredits } = useCredits();

  // Simulate progress with realistic steps
  useEffect(() => {
    if (!loading) {
      setProgress(0);
      setCurrentStep('');
      setStartTime(null);
      return;
    }

    setStartTime(Date.now());
    setProgress(0);
    setCurrentStep('Uploading your image...');

    const steps = [
      { threshold: 15, message: 'Uploading your image...', duration: 1000 },
      { threshold: 35, message: 'Processing with AI magic...', duration: 2000 },
      { threshold: 60, message: 'Generating your superhero...', duration: 3000 },
      { threshold: 85, message: 'Adding final touches...', duration: 2500 },
      { threshold: 95, message: 'Almost there...', duration: 1500 },
    ];

    let currentProgress = 0;
    let stepIndex = 0;

    const interval = setInterval(() => {
      if (stepIndex < steps.length) {
        const step = steps[stepIndex];
        const increment = (step.threshold - currentProgress) / (step.duration / 100);

        currentProgress += increment;

        if (currentProgress >= step.threshold) {
          currentProgress = step.threshold;
          stepIndex++;
          if (stepIndex < steps.length) {
            setCurrentStep(steps[stepIndex].message);
          }
        }

        setProgress(Math.min(currentProgress, 95));
      }
    }, 100);

    return () => clearInterval(interval);
  }, [loading]);

  // Complete progress when result is received
  useEffect(() => {
    if (result && loading === false) {
      setProgress(100);
      setCurrentStep('Done!');
      setTimeout(() => {
        setProgress(0);
        setCurrentStep('');
      }, 500);
    }
  }, [result, loading]);

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

  const estimatedTimeLeft = startTime ? Math.max(0, Math.round(10 - (Date.now() - startTime) / 1000)) : 10;

  return (
    <div className="max-w-7xl mx-auto px-4 py-4 sm:py-6 md:py-8 space-y-6 sm:space-y-8">
      {loading && (
        <LoadingProgress
          progress={progress}
          currentStep={currentStep}
          estimatedTimeLeft={estimatedTimeLeft}
        />
      )}

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
            className="btn-primary w-full flex items-center justify-center gap-2 text-base sm:text-lg py-3 sm:py-4 shadow-lg hover:shadow-xl transition-all"
          >
            {loading ? (
              <>
                <Loader2 className="animate-spin" size={20} />
                <span className="text-sm sm:text-base">Generating Your Superhero...</span>
              </>
            ) : (
              <>
                <Sparkles size={20} />
                <span className="text-sm sm:text-base">Generate Images ({numImages} {numImages === 1 ? 'credit' : 'credits'})</span>
              </>
            )}
          </button>
        </div>
      </div>

      {result && <ResultsGallery images={result.images} description={result.description} />}
    </div>
  );
}


