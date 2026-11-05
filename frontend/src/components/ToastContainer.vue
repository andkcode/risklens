<!-- frontend/src/components/ToastContainer.vue -->
<script setup lang="ts">import { useToastStore, type ToastType } from '../stores/toast';

const toastStore = useToastStore();

const getBorderColor = (type: ToastType): string => {
  switch (type) {
    case 'success':
      return 'border-emerald-500/30 bg-emerald-500/10 text-emerald-500';
    case 'error':
      return 'border-rose-500/30 bg-rose-500/10 text-rose-500';
    case 'warning':
      return 'border-amber-500/30 bg-amber-500/10 text-amber-500';
    default:
      return 'border-brand-border bg-brand-card text-brand-primary';
  }
};
</script>

<template>
  <aside
    aria-label="Notifications"
    class="fixed bottom-5 right-5 z-50 flex flex-col gap-3 max-w-sm w-full px-4 pointer-events-none"
  >
    <transition-group
      enter-active-class="transform transition duration-300 ease-out"
      enter-from-class="translate-y-2 opacity-0 sm:translate-y-0 sm:translate-x-2"
      enter-to-class="translate-y-0 opacity-100 sm:translate-x-0"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0 translate-x-2"
    >
      <div
        v-for="toast in toastStore.toasts"
        :key="toast.id"
        class="pointer-events-auto flex items-start justify-between rounded-xl border p-4 shadow-lg backdrop-blur-md bg-brand-card text-brand-primary border-brand-border"
      >
        <div class="space-y-1 pr-3">
          <p v-if="toast.title" class="text-xs font-semibold uppercase tracking-wider text-brand-secondary">
            {{ toast.title }}
          </p>
          <p class="text-sm font-medium text-brand-primary">
            {{ toast.message }}
          </p>
        </div>

        <button
          type="button"
          class="text-brand-secondary hover:text-brand-primary transition-colors text-lg leading-none"
          aria-label="Close notification"
          @click="toastStore.removeToast(toast.id)"
        >
          &times;
        </button>
      </div>
    </transition-group>
  </aside>
</template>