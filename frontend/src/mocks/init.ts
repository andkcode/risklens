export async function initMsw(): Promise<void> {
  if (import.meta.env.DEV) {
    const { worker } = await import('./browser');

    await worker.start({
      onUnhandledFrame: 'bypass',
    });
  }
}
