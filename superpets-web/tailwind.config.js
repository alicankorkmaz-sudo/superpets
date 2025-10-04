/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          500: '#7C3AED',
          600: '#6D28D9',
          700: '#5B21B6',
        },
        secondary: {
          500: '#3B82F6',
          600: '#2563EB',
        }
      },
      keyframes: {
        'slide-in': {
          '0%': { transform: 'translateX(100%)', opacity: '0' },
          '100%': { transform: 'translateX(0)', opacity: '1' },
        }
      },
      animation: {
        'slide-in': 'slide-in 0.3s ease-out',
      }
    },
  },
  plugins: [],
}

