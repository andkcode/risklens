<script setup lang="ts">
withDefaults(
  defineProps<{
    variant?: 'primary' | 'secondary' | 'danger' | 'outline';
    size?: 'sm' | 'md' | 'lg';
    loading?: boolean;
    disabled?: boolean;
  }>(),
  { variant: 'primary', size: 'md', loading: false, disabled: false },
);
</script>

<template>
  <button
    class="inline-flex items-center justify-center font-semibold transition-all rounded-md cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
    :class="[
      size === 'sm' && 'h-8 px-3 text-[10px]',
      size === 'md' && 'h-9 px-3.5 text-[11px]',
      size === 'lg' && 'h-10 px-4 text-xs',
      variant === 'primary' &&
        'text-white bg-gradient-to-b from-[#4386f5] to-[#2f70df] border border-[#5593f8] shadow-[0_8px_24px_rgba(38,105,220,.2)] hover:brightness-108',
      variant === 'secondary' &&
        'text-[#bac2cc] bg-[#101419] border border-[#2b333d] hover:text-[#ebedf0] hover:bg-[#171d24]',
      variant === 'danger' &&
        'text-white bg-[#ff6978]/20 border border-[#ff6978]/40 hover:bg-[#ff6978]/30',
      variant === 'outline' &&
        'text-[#8994a2] bg-transparent border border-[#222830] hover:border-[#4c8dff] hover:text-[#4c8dff]',
    ]"
    :disabled="disabled || loading"
  >
    <svg
      v-if="loading"
      class="animate-spin -ml-1 mr-2 size-4 text-current"
      fill="none"
      viewBox="0 0 24 24"
    >
      <circle
        class="opacity-25"
        cx="12"
        cy="12"
        r="10"
        stroke="currentColor"
        stroke-width="4"
      ></circle>
      <path
        class="opacity-75"
        fill="currentColor"
        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
      ></path>
    </svg>
    <slot />
  </button>
</template>
