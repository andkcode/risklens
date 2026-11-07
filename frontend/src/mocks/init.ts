import { worker } from './browser';

export async function initMocks() {
  // Only enable MSW in development mode
  if (import.meta.env.MODE !== 'development') {
    return;
  }

  // Start the Service Worker and wait for it to be ready
  await worker.start({
    onUnhandledFrame: 'bypass', // Ignore unhandled requests (like loading local .vue files or Vite assets)
  });

  console.log('[MSW] Mock Service Worker initialized successfully.');
}
