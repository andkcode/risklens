<script setup lang="ts">
import type { MetricCardData } from '../../types/dashboard';
import {
  getTrendColorClass,
  getRiskBadgeClass,
  generateLinePath,
  calculateCircleOffset,
} from '../../utils/riskStyle';
import BaseCard from '../BaseCard.vue';

defineProps<{
  metrics: MetricCardData[];
}>();
</script>

<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
    <BaseCard
      v-for="(metric, index) in metrics"
      :key="index"
      variant="standard"
      class="p-4 space-y-3 flex flex-col"
    >
      <div>
        <!-- Card Header -->
        <div class="flex items-center text-xs text-[#8994a2]">
          <span>{{ metric.title }}</span>
          <span
            v-if="metric.timeframe"
            class="text-[9px] bg-[#171d24] px-1.5 py-0.5 rounded border border-[#2b333d]"
          >
            {{ metric.timeframe }}
          </span>
          <span
            v-else-if="metric.riskLevel"
            class="text-[9px] bg-rose-500/10 text-rose-400 px-1.5 py-0.5 rounded border border-rose-500/20 font-medium"
          >
            LIVE
          </span>
        </div>

        <!-- Risk Score Card Layout (Replaced text value with circular gauge) -->
        <div v-if="metric.riskLevel" class="mt-3 flex items-center ">
          <div>
            <div class="text-[11px] font-medium text-white">Risk evaluation</div>
            <div class="text-[9px] text-[#8994a2] mt-0.5">Score out of 100</div>
          </div>
          <!-- Circular Gauge replacing text value -->
          <div class="relative size-20 flex items-center justify-center shrink-0">
            <svg class="size-full -rotate-90" viewBox="0 0 36 36">
              <path
                class="text-[#222830]"
                stroke-width="3.5"
                stroke="currentColor"
                fill="none"
                d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              />
              <path
                class="text-[#f0ad55]"
                :stroke-dasharray="`100, 100`"
                :stroke-dashoffset="calculateCircleOffset(metric.value)"
                stroke-width="3.5"
                stroke-linecap="round"
                stroke="currentColor"
                fill="none"
                d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
              />
            </svg>
            <span class="absolute text-[20px] font-bold text-white">{{
              metric.value.split('/')[0]
            }}</span>
          </div>
        </div>

        <!-- Standard Metric Value and Change Layout (For other cards) -->
        <div v-else class="flex items-baseline mt-2">
          <span class="text-2xl font-bold text-white">{{ metric.value }}</span>

          <!-- Dynamic trend color based on utility function -->
          <span
            v-if="metric.change"
            class="text-xs font-medium"
            :class="getTrendColorClass(metric.change)"
          >
            {{ metric.change }}
          </span>
        </div>

        <!-- Subtitle / Details -->
        <p class="text-[10px] text-[#8994a2] mt-1">{{ metric.subtitle }}</p>
      </div>

      <!-- Dynamic Line & Bar Charts Rendered from Mocks -->
      <div v-if="metric.chartData && metric.chartData.length > 0" class="pt-2">
        <!-- Line Chart (Overall portfolio value & Max drawdown) -->
        <svg
          v-if="metric.chartType === 'line'"
          viewBox="0 0 200 40"
          class="w-full h-10 overflow-visible"
        >
          <polyline
            fill="none"
            stroke="#4c8dff"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            :points="generateLinePath(metric.chartData)"
          />
        </svg>

        <!-- Bar Chart (Annualized volatility) -->
        <div v-else-if="metric.chartType === 'bars'" class="flex items-end gap-1 h-10 w-full pt-2">
          <div
            v-for="(val, vIdx) in metric.chartData"
            :key="vIdx"
            class="flex-1 bg-[#222830] hover:bg-[#4c8dff] rounded-t transition-colors"
            :style="{ height: `${val}%` }"
          ></div>
        </div>
      </div>
    </BaseCard>
  </div>
</template>
