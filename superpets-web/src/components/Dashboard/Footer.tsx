interface FooterProps {
  onNavigate?: (view: 'editor' | 'pricing' | 'terms' | 'privacy') => void;
}

export function Footer({ onNavigate }: FooterProps) {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-white border-t border-gray-200 mt-auto">
      <div className="max-w-7xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
        <div className="flex flex-col items-center gap-4">
          <div className="flex gap-6 text-sm">
            <button
              onClick={() => onNavigate?.('terms')}
              className="text-gray-600 hover:text-primary-500 transition-colors"
            >
              Terms of Service
            </button>
            <span className="text-gray-300">|</span>
            <button
              onClick={() => onNavigate?.('privacy')}
              className="text-gray-600 hover:text-primary-500 transition-colors"
            >
              Privacy Policy
            </button>
          </div>
          <p className="text-sm text-gray-500">
            © {currentYear} Superpets. All rights reserved.
          </p>
        </div>
      </div>
    </footer>
  );
}
