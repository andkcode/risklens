<script setup lang="ts">
import type { PropType } from 'vue';

defineProps({
  title: {
    type: String,
    default: '',
  },
  variant: {
    type: String as PropType<'success' | 'danger' | 'warning' | 'info'>,
    default: 'info',
  },
});
</script>

<template>
  <div
      class="p-4 rounded-xl border flex items-start gap-3 text-sm"
      :class="[
      variant === 'success' && 'bg-emerald-50 border-emerald-200 text-emerald-900',
      variant === 'danger' && 'bg-red-50 border-red-200 text-red-900',
      variant === 'warning' && 'bg-amber-50 border-amber-200 text-amber-900',
      variant === 'info' && 'bg-blue-50 border-blue-200 text-blue-900'
    ]"
  >
    <div class="flex-1">
      <h4 v-if="title" class="font-semibold mb-0.5">{{ title }}</h4>
      <slot />
    </div>
  </div>
</template>