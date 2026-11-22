<script setup lang="ts">
import BaseButton from '../BaseButton.vue';
import RiskIcons from '../icons/RiskIcons.vue';

defineProps<{
  portfolioName: string;
  lastAnalyzedTime: string;
}>();

defineEmits<{
  (e: 'export-pdf'): void;
  (e: 'export-csv'): void;
  (e: 'print-report'): void;
}>();
</script>

<template>
  <div
    class="risk-page-heading flex flex-col md:flex-row md:items-center justify-between gap-4 mb-6"
  >
    <div class="min-w-0">
      <p class="eyebrow text-xs text-brand-accent uppercase tracking-wider font-semibold mb-1">
        Risk intelligence / Detailed analysis
      </p>
      <h1 class="text-xl md:text-2xl font-bold text-white tracking-tight">
        Portfolio Risk Breakdown &amp; Analytics
      </h1>
      <p class="text-xs text-brand-secondary mt-1">
        Quantitative exposure analysis across {{ portfolioName }} · Calculated
        {{ lastAnalyzedTime }}
      </p>
    </div>
    <div class="risk-export-actions flex items-center gap-2 flex-wrap shrink-0">
      <BaseButton variant="secondary" size="sm" @click="$emit('export-pdf')">
        <RiskIcons name="download" :size="14" class="mr-1.5" />
        Export PDF
      </BaseButton>
      <BaseButton variant="secondary" size="sm" @click="$emit('export-csv')">
        <RiskIcons name="download" :size="14" class="mr-1.5" />
        Export CSV
      </BaseButton>
      <BaseButton variant="secondary" size="sm" @click="$emit('print-report')">
        <RiskIcons name="external" :size="14" class="mr-1.5" />
        Print report
      </BaseButton>
    </div>
  </div>
</template>
