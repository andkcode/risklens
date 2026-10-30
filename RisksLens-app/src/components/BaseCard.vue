<script setup lang="ts">
defineProps<{
  title?: string
  subtitle?: string
}>()
</script>

<template>
  <div class="bg-white border border-brand-border rounded-2xl p-5 shadow-card transition-all">
    <div v-if="title || $slots.header" class="flex items-center justify-between mb-4 pb-3 border-b border-brand-border">
      <div>
        <h3 class="text-base font-semibold text-brand-primary">{{ title }}</h3>
        <p v-if="subtitle" class="text-xs text-brand-secondary mt-0.5">{{ subtitle }}</p>
      </div>
      <slot name="actions" />
    </div>
    <div>
      <slot />
    </div>
  </div>
</template>