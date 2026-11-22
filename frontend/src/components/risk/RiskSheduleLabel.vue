<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import BaseCard from '../BaseCard.vue';
import { getRiskBadgeClass } from '../../utils/riskStyle';

interface RiskTimelineItem {
  id: string;
  name: string;
  category: string;
  ticker: string;
  badgeLetter: string;
  liquidityScore: number;
  volatility: string;
  scheduleLabel: string;
  scheduleProgressPercent: number;
  riskLevel: string;
}

defineProps<{
  items: RiskTimelineItem[];
  modelValue: string;
}>();

defineEmits<{
  (e: 'update:modelValue', range: string): void;
}>();
</script>

<template>
  <BaseCard variant="standard" class="risk-schedule-card my-6">
    <div class="table-toolbar flex flex-col sm:flex-row sm:items-center justify-between gap-3 mb-4">
      <div>
        <span class="text-sm font-bold text-white tracking-tight">
          Asset Risk Schedule &amp; Volatility Timeline
        </span>
        <p class="text-[11px] text-brand-secondary mt-0.5">
          Forward-looking liquidity, volatility, and lockup risk by position
        </p>
      </div>
      <div
        class="range-toggle flex items-center bg-brand-sidebar border border-brand-border rounded-lg p-1 gap-1 self-start sm:self-auto"
      >
        <button
          v-for="range in ['1M', '3M', '6M', '1Y']"
          :key="range"
          :class="[
            'px-2.5 py-1 text-xs font-medium rounded transition-colors',
            modelValue === range
              ? 'bg-brand-accent text-white'
              : 'text-brand-secondary hover:text-white',
          ]"
          @click="$emit('update:modelValue', range)"
        >
          {{ range }}
        </button>
      </div>
    </div>

    <div class="schedule-table border border-brand-border rounded-lg overflow-x-auto">
      <div min-w-[700px]>
        <!-- Шапка таблицы с адаптивной сеткой -->
        <div
          class="schedule-row schedule-head grid grid-cols-12 p-3 bg-brand-sidebar text-[10px] text-brand-secondary uppercase tracking-wider font-semibold gap-2"
        >
          <span class="col-span-3">Asset name</span>
          <span class="col-span-2">Liquidity</span>
          <span class="col-span-2">Volatility</span>
          <span class="col-span-3">Expiry / lockup schedule</span>
          <span class="col-span-2 text-right">Risk level</span>
        </div>

        <!-- Строки таблицы -->
        <div
          v-for="asset in items"
          :key="asset.id || asset.ticker"
          class="schedule-row grid grid-cols-12 p-3 items-center border-t border-brand-border hover:bg-[#141a21] text-xs transition-colors gap-2"
        >
          <!-- Имя и тикер -->
          <span class="col-span-3 flex items-center gap-2 min-w-0">
            <span
              class="size-6 shrink-0 rounded bg-brand-sidebar border border-brand-border flex items-center justify-center font-bold text-xs text-brand-accent"
            >
              {{ asset.badgeLetter || asset.ticker[0] }}
            </span>
            <div class="min-w-0 truncate">
              <strong class="text-white block truncate">{{ asset.ticker }}</strong>
              <small class="block text-[10px] text-brand-secondary truncate">
                {{ asset.name }} · {{ asset.category }}
              </small>
            </div>
          </span>

          <!-- Ликвидность -->
          <span class="col-span-2 font-mono font-semibold text-white flex items-center gap-1.5">
            {{ asset.liquidityScore }}/100
          </span>

          <!-- Волатильность -->
          <span class="col-span-2 font-mono text-brand-secondary truncate">
            {{ asset.volatility }} · 3M
          </span>

          <!-- Расписание / Локап -->
          <span class="col-span-3 text-brand-secondary text-[11px] truncate">
            {{ asset.scheduleLabel }}
          </span>

          <!-- Уровень риска -->
          <span class="col-span-2 flex justify-end">
            <span
              class="px-2 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wider inline-block text-center"
              :class="getRiskBadgeClass(asset.riskLevel)"
            >
              {{ asset.riskLevel }}
            </span>
          </span>
        </div>
      </div>
    </div>
  </BaseCard>
</template>
