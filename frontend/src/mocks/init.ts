export async function initMsw(): Promise<void> {
  if (!import.meta.env.DEV) {
    return;
  }

  if (import.meta.env.VITE_ENABLE_MOCKS !== 'true') {
    if ('serviceWorker' in navigator) {
      const mockWorkerUrl = new URL('/mockServiceWorker.js', window.location.origin).href;
      const registrations = await navigator.serviceWorker.getRegistrations();
      const mockRegistrations = registrations.filter((registration) =>
        [registration.active, registration.waiting, registration.installing].some(
          (worker) => worker?.scriptURL === mockWorkerUrl,
        ),
      );
      await Promise.all(mockRegistrations.map((registration) => registration.unregister()));
    }
    return;
  }

  const { worker } = await import('./browser');

  await worker.start({
    onUnhandledFrame: 'bypass',
  });
}
