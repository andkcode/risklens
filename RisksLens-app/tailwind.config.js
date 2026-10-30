/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          primary: '#0F172A', // Slate 900 (основной темный текст / шапкы)
          secondary: '#475569', // Slate 600 (вторичный текст)
          accent: '#2563EB', // Blue 600 (интерактивные элементы, брендинг)
          hover: '#1D4ED8', // Blue 700 (состояние hover)
          surface: '#F8FAFC', // Slate 50 (фоны карточек и рабочей зоны)
          border: '#E2E8F0', // Slate 200 (разделители, рамки таблиц)
        },
        // Индикаторы рынка и рисков (PnL, уровни риска)
        market: {
          success: '#10B981', // Emerald 500 (прибыль / низкий риск)
          danger: '#EF4444', // Red 500 (убыток / высокий риск)
          warning: '#F59E0B', // Amber 500 (средний риск / предупреждения)
        },
      },
      fontFamily: {
        sans: ['Inter', '-apple-system', 'BlinkMacSystemFont', 'Segoe UI', 'Roboto', 'sans-serif'],

        mono: ['JetBrains Mono', 'Menlo', 'Monaco', 'Consolas', 'monospace'],
      },
      fontSize: {
        '2xs': ['0.625rem', { lineHeight: '0.75rem' }],
      },
      boxShadow: {
        card: '0 1px 3px 0 rgb(0 0 0 / 0.05), 0 1px 2px -1px rgb(0 0 0 / 0.05)',
        dropdown: '0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1)',
      },
    },
  },
  plugins: [],
};
