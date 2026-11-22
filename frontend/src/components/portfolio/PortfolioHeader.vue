<script setup lang="ts">
import type { PortfolioDetailData } from '../../types/portfolios';
import BaseButton from '../BaseButton.vue';
import BaseIcon from '../icons/PortfolioIcons.vue';

defineProps<{
  portfolio: PortfolioDetailData;
}>();

defineEmits<{
  (e: 'export'): void;
  (e: 'run-analysis'): void;
}>();
</script>

<template>
  <div
    class="flex flex-col md:flex-row md:items-center justify-between gap-4 pb-6 border-b border-brand-border"
  >
    <div>
      <div class="text-[11px] font-semibold text-brand-accent tracking-[0.08em] uppercase mb-1">
        Portfolios / {{ portfolio.code || 'PORTFOLIO' }}
      </div>
      <h1 class="text-2xl font-bold text-white tracking-tight">
        {{ portfolio.name }}
      </h1>
      <p class="text-xs text-brand-secondary mt-1">
        {{ portfolio.description }}
      </p>
    </div>
    <div class="flex items-center gap-3">
      <BaseButton
        variant="secondary"
        class="bg-brand-sidebar border-brand-border text-white hover:border-brand-accent hover:text-brand-accent px-4 py-2 text-xs font-medium rounded-lg flex items-center gap-2 transition-colors"
        @click="$emit('export')"
      >
        <BaseIcon name="export" />
        Export
      </BaseButton>
      <BaseButton
        variant="primary"
        class="bg-brand-accent hover:bg-blue-600 text-white px-4 py-2 text-xs font-semibold rounded-lg flex items-center gap-2 shadow-[0_4px_16px_rgba(47,112,223,0.3)] transition-colors"
        @click="$emit('run-analysis')"
      >
        <BaseIcon name="analysis" />
        Run analysis
      </BaseButton>
    </div>
  </div>
</template>
