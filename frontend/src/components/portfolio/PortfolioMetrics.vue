<script setup lang="ts">
import BaseCard from '../BaseCard.vue';
import { getRiskBadgeClass } from '../../utils/riskStyle';

defineProps<{
  totalValue?: string;
  valueChangeToday?: string;
  performanceYtd?: string;
  performanceComparison?: string;
  riskScore?: number;
  riskLevel?: string;
  lastAnalyzed?: string;
}>();
</script>

<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 my-6">
    <!-- Total Value Card -->
    <BaseCard variant="standard" class="p-4 flex flex-col justify-between">
      <div class="text-[10px] text-brand-secondary font-semibold uppercase tracking-wider">
        Total value
      </div>
      <div class="text-2xl font-bold text-white mt-2 font-mono tracking-tight">
        {{ totalValue || '$0' }}
      </div>
      <div class="text-[11px] text-emerald-400 mt-1 font-semibold flex items-center gap-1">
        <span>↗</span> {{ valueChangeToday || '+$0 today' }}
      </div>
    </BaseCard>

    <!-- Performance YTD Card -->
    <BaseCard variant="standard" class="p-4 flex flex-col justify-between">
      <div class="text-[10px] text-brand-secondary font-semibold uppercase tracking-wider">
        Performance YTD
      </div>
      <div class="text-2xl font-bold text-emerald-400 mt-2 font-mono tracking-tight">
        {{ performanceYtd || '+0%' }}
      </div>
      <div class="text-[11px] text-brand-secondary mt-1 font-medium">
        {{ performanceComparison || 'vs S&P 500 +0%' }}
      </div>
    </BaseCard>

    <!-- Risk Score Card -->
    <BaseCard variant="standard" class="p-4 flex flex-col justify-between">
      <div class="text-[10px] text-brand-secondary font-semibold uppercase tracking-wider">
        Risk score
      </div>
      <div class="flex items-baseline gap-2 mt-2">
        <span class="text-2xl font-bold text-white font-mono tracking-tight">{{
          riskScore || 50
        }}</span>
        <span class="text-xs text-brand-secondary">/100</span>
        <span
          :class="[
            'ml-auto px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider',
            getRiskBadgeClass(riskLevel),
          ]"
        >
          {{ riskLevel || 'Medium' }}
        </span>
      </div>
      <div class="text-[11px] text-brand-secondary mt-1 font-medium">Comprehensive risk rating</div>
    </BaseCard>

    <!-- Last Analyzed Card -->
    <BaseCard variant="standard" class="p-4 flex flex-col justify-between">
      <div class="text-[10px] text-brand-secondary font-semibold uppercase tracking-wider">
        Last analyzed
      </div>
      <div class="text-2xl font-bold text-white mt-2 tracking-tight">
        {{ lastAnalyzed || 'Never' }}
      </div>
      <div class="text-[11px] text-emerald-400 mt-1 font-semibold flex items-center gap-1.5">
        <span class="size-1.5 rounded-full bg-emerald-400 animate-pulse"></span> Real-time data
        active
      </div>
    </BaseCard>
  </div>
</template>
