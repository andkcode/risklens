import { defineStore } from 'pinia';
import { ref } from 'vue';

export type ToastType = 'success' | 'error' | 'info' | 'warning';

export interface ToastMessage {
  id: string;
  title?: string;
  message: string;
  type: ToastType;
  duration?: number; // duration in ms
}

export const useToastStore = defineStore('toast', () => {
  const toasts = ref<ToastMessage[]>([]);

  function addToast(
    message: string,
    type: ToastType = 'info',
    title?: string,
    duration = 4000
  ): void {
    const id = Math.random().toString(36).slice(2, 9);
    const newToast: ToastMessage = { id, message, type, title, duration };

    toasts.value.push(newToast);

    if (duration > 0) {
      setTimeout(() => {
        removeToast(id);
      }, duration);
    }
  }

  function removeToast(id: string): void {
    toasts.value = toasts.value.filter(t => t.id !== id);
  }

  // Helper shortcuts
  const success = (message: string, title = 'Success') => addToast(message, 'success', title);
  const error = (message: string, title = 'Error') => addToast(message, 'error', title);
  const info = (message: string, title = 'Info') => addToast(message, 'info', title);
  const warning = (message: string, title = 'Warning') => addToast(message, 'warning', title);

  return {
    toasts,
    addToast,
    removeToast,
    success,
    error,
    info,
    warning,
  };
});