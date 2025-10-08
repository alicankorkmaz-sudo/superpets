import { Sparkles, Zap, Shield, Heart, ArrowRight } from 'lucide-react';

interface LandingPageProps {
  onGetStarted: () => void;
  onLogin: () => void;
}

export function LandingPage({ onGetStarted, onLogin }: LandingPageProps) {
  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-50 via-white to-secondary-50">
      {/* Navigation */}
      <nav className="bg-white/80 backdrop-blur-sm border-b border-gray-200 sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <Shield className="text-primary-600" size={32} />
            <span className="text-2xl font-bold bg-gradient-to-r from-primary-600 to-secondary-600 bg-clip-text text-transparent">
              SuperPets
            </span>
          </div>
          <button
            onClick={onLogin}
            className="btn-secondary"
          >
            Log In
          </button>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="max-w-7xl mx-auto px-4 py-20 text-center">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-5xl md:text-6xl font-bold mb-6 leading-tight">
            Transform Your Pet into a
            <span className="block bg-gradient-to-r from-primary-600 to-secondary-600 bg-clip-text text-transparent">
              Superhero
            </span>
          </h1>
          <p className="text-xl text-gray-600 mb-8 leading-relaxed">
            Powered by cutting-edge AI, SuperPets transforms your beloved pets into epic superheroes.
            Upload a photo, choose from 29+ unique heroes, and watch the magic unfold.
          </p>
          <div className="flex gap-4 justify-center flex-wrap">
            <button
              onClick={onGetStarted}
              className="btn-primary px-8 py-4 text-lg flex items-center gap-2 shadow-xl hover:shadow-2xl transition-all"
            >
              Get Started Free
              <ArrowRight size={24} />
            </button>
            <button
              onClick={() => {
                document.getElementById('how-it-works')?.scrollIntoView({ behavior: 'smooth' });
              }}
              className="btn-secondary px-8 py-4 text-lg"
            >
              See How It Works
            </button>
          </div>
          <p className="text-sm text-gray-500 mt-4">
            ✨ Start with 5 free credits • No credit card required
          </p>
        </div>
      </section>

      {/* Features Section */}
      <section className="max-w-7xl mx-auto px-4 py-16">
        <div className="grid md:grid-cols-3 gap-8">
          <div className="card text-center hover:shadow-xl transition-shadow">
            <div className="flex justify-center mb-4">
              <div className="bg-primary-100 p-4 rounded-full">
                <Sparkles className="text-primary-600" size={32} />
              </div>
            </div>
            <h3 className="text-xl font-bold mb-3">AI-Powered Magic</h3>
            <p className="text-gray-600">
              Using Google's Nano Banana model via fal.ai, we create stunning superhero transformations while preserving your pet's unique features.
            </p>
          </div>

          <div className="card text-center hover:shadow-xl transition-shadow">
            <div className="flex justify-center mb-4">
              <div className="bg-secondary-100 p-4 rounded-full">
                <Shield className="text-secondary-600" size={32} />
              </div>
            </div>
            <h3 className="text-xl font-bold mb-3">29+ Unique Heroes</h3>
            <p className="text-gray-600">
              Choose from classic superheroes or unique characters. Each hero has 10 different scene variations for unlimited creativity.
            </p>
          </div>

          <div className="card text-center hover:shadow-xl transition-shadow">
            <div className="flex justify-center mb-4">
              <div className="bg-green-100 p-4 rounded-full">
                <Zap className="text-green-600" size={32} />
              </div>
            </div>
            <h3 className="text-xl font-bold mb-3">Lightning Fast</h3>
            <p className="text-gray-600">
              Generate up to 10 images in parallel. High-quality results in seconds, not minutes.
            </p>
          </div>
        </div>
      </section>

      {/* How It Works */}
      <section id="how-it-works" className="bg-white py-16">
        <div className="max-w-5xl mx-auto px-4">
          <h2 className="text-4xl font-bold text-center mb-12">How It Works</h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="bg-primary-600 text-white rounded-full w-12 h-12 flex items-center justify-center text-xl font-bold mx-auto mb-4">
                1
              </div>
              <h3 className="text-lg font-bold mb-2">Upload Your Pet Photo</h3>
              <p className="text-gray-600">
                Choose your favorite photo of your pet. Works with dogs, cats, and any furry friend!
              </p>
            </div>

            <div className="text-center">
              <div className="bg-primary-600 text-white rounded-full w-12 h-12 flex items-center justify-center text-xl font-bold mx-auto mb-4">
                2
              </div>
              <h3 className="text-lg font-bold mb-2">Select a Hero</h3>
              <p className="text-gray-600">
                Pick from classics like Superman and Batman, or unique characters like Cyber Ninja and Cosmic Guardian.
              </p>
            </div>

            <div className="text-center">
              <div className="bg-primary-600 text-white rounded-full w-12 h-12 flex items-center justify-center text-xl font-bold mx-auto mb-4">
                3
              </div>
              <h3 className="text-lg font-bold mb-2">Generate & Download</h3>
              <p className="text-gray-600">
                Our AI creates your superhero pet in seconds. Download in high quality and share with the world!
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Pricing Teaser */}
      <section className="max-w-5xl mx-auto px-4 py-16 text-center">
        <h2 className="text-4xl font-bold mb-6">Simple, Transparent Pricing</h2>
        <p className="text-xl text-gray-600 mb-8">
          1 credit = 1 image. Start with 5 free credits, no credit card required.
        </p>
        <div className="inline-block card max-w-md hover:shadow-xl transition-shadow">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Heart className="text-red-500" size={32} />
            <span className="text-3xl font-bold">New User Bonus</span>
          </div>
          <p className="text-5xl font-bold text-primary-600 mb-2">5 Free Credits</p>
          <p className="text-gray-600 mb-6">Create 5 superhero images for free!</p>
          <button
            onClick={onGetStarted}
            className="btn-primary w-full py-3 text-lg"
          >
            Claim Your Free Credits
          </button>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-gradient-to-r from-primary-600 to-secondary-600 py-16">
        <div className="max-w-4xl mx-auto px-4 text-center text-white">
          <h2 className="text-4xl font-bold mb-4">Ready to Transform Your Pet?</h2>
          <p className="text-xl mb-8 opacity-90">
            Join thousands of pet owners who've turned their furry friends into legendary heroes.
          </p>
          <button
            onClick={onGetStarted}
            className="bg-white text-primary-600 px-8 py-4 rounded-lg font-bold text-lg hover:bg-gray-100 transition-colors shadow-xl hover:shadow-2xl flex items-center gap-2 mx-auto"
          >
            Start Creating for Free
            <ArrowRight size={24} />
          </button>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 text-white py-8">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <div className="flex items-center justify-center gap-2 mb-4">
            <Shield size={24} />
            <span className="text-xl font-bold">SuperPets</span>
          </div>
          <p className="text-gray-400 text-sm mb-4">
            Transform your pets into superheroes with AI-powered magic
          </p>
          <div className="flex justify-center gap-6 text-sm text-gray-400">
            <button
              onClick={() => {
                // This will be handled by the parent component
                window.location.href = '/?view=terms';
              }}
              className="hover:text-white transition-colors"
            >
              Terms of Service
            </button>
            <button
              onClick={() => {
                window.location.href = '/?view=privacy';
              }}
              className="hover:text-white transition-colors"
            >
              Privacy Policy
            </button>
          </div>
          <p className="text-gray-500 text-xs mt-6">
            © 2025 SuperPets. All rights reserved.
          </p>
        </div>
      </footer>
    </div>
  );
}
