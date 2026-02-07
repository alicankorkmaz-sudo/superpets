import { useState, useEffect, useRef } from 'react';
import { ImageUploader } from '../components/Editor/ImageUploader';
import { HeroSelector } from '../components/Editor/HeroSelector';
import { OutputSettings } from '../components/Editor/OutputSettings';
import { ResultsGallery } from '../components/Editor/ResultsGallery';
import { LoadingProgress } from '../components/Editor/LoadingProgress';
import { useImageEdit } from '../hooks/useImageEdit';
import { useCredits } from '../contexts/CreditsContext';
import { Loader2, Sparkles, AlertTriangle } from 'lucide-react';

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
  const resultsRef = useRef<HTMLDivElement>(null);

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

    // Calculate estimated time based on number of images
    // Base time: 10 seconds for 1 image, add 3 seconds per additional image
    const estimatedTotalTime = 10 + (numImages - 1) * 3;
    const timeMultiplier = estimatedTotalTime / 10; // Scale delays based on estimated time

    const steps = [
      { progress: 15, message: 'Uploading your image...', delay: 800 * timeMultiplier },
      { progress: 35, message: 'Processing with AI magic...', delay: 1500 * timeMultiplier },
      { progress: 60, message: numImages > 1 ? `Generating ${numImages} superhero images...` : 'Generating your superhero...', delay: 2500 * timeMultiplier },
      { progress: 80, message: 'Adding final touches...', delay: 2000 * timeMultiplier },
      { progress: 95, message: 'Almost there...', delay: 1500 * timeMultiplier },
    ];

    const timers: NodeJS.Timeout[] = [];
    let cumulativeDelay = 0;

    steps.forEach((step) => {
      cumulativeDelay += step.delay;
      const timer = setTimeout(() => {
        setProgress(step.progress);
        setCurrentStep(step.message);
      }, cumulativeDelay);
      timers.push(timer);
    });

    return () => {
      timers.forEach(timer => clearTimeout(timer));
    };
  }, [loading, numImages]);

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

  // Scroll to results on mobile when generation completes
  useEffect(() => {
    if (result && !loading && resultsRef.current) {
      // Small delay to ensure DOM is updated
      setTimeout(() => {
        resultsRef.current?.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }, 100);
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

  // Calculate estimated time based on number of images
  // Base time: 10 seconds for 1 image, add 3 seconds per additional image
  const estimatedTotalTime = 10 + (numImages - 1) * 3;
  const estimatedTimeLeft = startTime ? Math.max(0, Math.round(estimatedTotalTime - (Date.now() - startTime) / 1000)) : estimatedTotalTime;

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
        <ImageUploader file={file} onFileChange={setFile} />
      </div>

      <div className="card shadow-lg">
        <HeroSelector selectedHeroId={selectedHeroId} onHeroSelect={setSelectedHeroId} />
      </div>

      <div className="card shadow-lg">
        <OutputSettings
          numImages={numImages}
          onNumImagesChange={handleNumImagesChange}
          outputFormat={outputFormat}
          onOutputFormatChange={setOutputFormat}
        />
      </div>

      {(error || validationError) && (
        <div className="bg-red-50 border border-red-200 text-red-700 p-4 rounded-lg flex items-start gap-3">
          <AlertTriangle size={20} className="flex-shrink-0 mt-0.5" />
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

      {result ? (
        <div ref={resultsRef}>
          <ResultsGallery images={result.images} description={result.description} />
        </div>
      ) : (
        <div className="border-2 border-dashed border-gray-200 rounded-xl p-8 sm:p-12 text-center">
          <Sparkles size={32} className="mx-auto text-gray-300 mb-3" />
          <p className="text-sm text-gray-400">Your superhero images will appear here</p>
        </div>
      )}
    </div>
  );
}


