<script setup lang="ts">
import type { PortfolioDetailData } from '../../types/portfolios';
import BaseCard from '../BaseCard.vue';
import { getRiskBadgeClass } from '../../utils/riskStyle';

defineProps<{
  portfolio: PortfolioDetailData;
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
        {{ portfolio.totalValue }}
      </div>
      <div class="text-[11px] text-emerald-400 mt-1 font-semibold flex items-center gap-1">
        <span>↗</span> {{ portfolio.valueChangeToday }}
      </div>
    </BaseCard>

    <!-- Performance YTD Card -->
    <BaseCard variant="standard" class="p-4 flex flex-col justify-between">
      <div class="text-[10px] text-brand-secondary font-semibold uppercase tracking-wider">
        Performance YTD
      </div>
      <div class="text-2xl font-bold text-emerald-400 mt-2 font-mono tracking-tight">
        {{ portfolio.performanceYtd }}
      </div>
      <div class="text-[11px] text-brand-secondary mt-1 font-medium">
        {{ portfolio.performanceComparison }}
      </div>
    </BaseCard>

    <!-- Risk Score Card -->
    <BaseCard variant="standard" class="p-4 flex flex-col justify-between">
      <div class="text-[10px] text-brand-secondary font-semibold uppercase tracking-wider">
        Risk score
      </div>
      <div class="flex items-baseline gap-2 mt-2">
        <span class="text-2xl font-bold text-white font-mono tracking-tight">{{
          portfolio.riskScore
        }}</span>
        <span class="text-xs text-brand-secondary">/100</span>
        <span
          :class="[
            'ml-auto px-2 py-0.5 rounded text-[10px] font-bold uppercase tracking-wider',
            getRiskBadgeClass(portfolio.riskLevel),
          ]"
        >
          {{ portfolio.riskLevel }}
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
        {{ portfolio.lastAnalyzed }}
      </div>
      <div class="text-[11px] text-emerald-400 mt-1 font-semibold flex items-center gap-1.5">
        <span class="size-1.5 rounded-full bg-emerald-400 animate-pulse"></span> Real-time data
        active
      </div>
    </BaseCard>
  </div>
</template>
