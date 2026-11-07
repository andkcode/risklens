<script setup lang="ts">
withDefaults(
  defineProps<{
    title?: string;
    subtitle?: string;
    variant?: 'standard' | 'auth';
  }>(),
  {
    variant: 'standard',
    title: undefined,
    subtitle: undefined,
  },
);
</script>

<template>
  <div
    class="transition-all"
    :class="[
      variant === 'standard' &&
        'bg-brand-surface border border-brand-border rounded-[9px] shadow-card',

      variant === 'auth' &&
        'relative bg-[rgba(16,20,25,.96)] border border-[#252d36] rounded-[10px] p-8 shadow-[0_28px_80px_rgba(0,0,0,.38),0_0_0_1px_rgba(255,255,255,.012)] before:absolute before:-top-px before:left-[20%] before:right-[20%] before:h-px before:bg-linear-to-r before:from-transparent before:via-[rgba(76,141,255,.72)] before:to-transparent',
    ]"
  >
    <div
      v-if="variant === 'standard' && (title || $slots.header || $slots['header-actions'])"
      class="h-16.5 px-4.75 flex items-center justify-between border-b border-brand-border"
    >
      <div>
        <h3 v-if="title" class="text-xs font-semibold text-[#d9dde3] tracking-tight">
          {{ title }}
        </h3>
        <p v-if="subtitle" class="mt-1 text-[9px] text-brand-secondary">{{ subtitle }}</p>
      </div>
      <slot name="header-actions" />
    </div>

    <div :class="variant === 'standard' ? 'p-4.75' : ''">
      <slot />
    </div>
  </div>
</template>
