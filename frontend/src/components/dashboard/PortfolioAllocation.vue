<script setup lang="ts">
import type { PortfolioAllocationData } from '../../types/dashboard';
import { getCumulativeOffset } from '../../utils/riskStyle';
import BaseCard from '../BaseCard.vue';

defineProps<{
  allocation: PortfolioAllocationData;
}>();
</script>

<template>
  <BaseCard
    variant="standard"
    title="Portfolio allocation"
    subtitle="By asset class"
    class="lg:col-span-3 flex flex-col justify-between"
  >
    <template #header-actions>
      <button class="text-[#8994a2] hover:text-white transition-colors">
        <svg class="size-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"
          />
        </svg>
      </button>
    </template>

    <!-- Donut Chart Container -->
    <div class="relative flex items-center justify-center py-4">
      <div class="relative size-44 flex items-center justify-center">
        <svg class="size-full -rotate-90" viewBox="0 0 36 36">
          <!-- Background track -->
          <path
            class="text-[#222830]"
            stroke-width="4.5"
            stroke="currentColor"
            fill="none"
            d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
          />
          <!-- Allocation Slices -->
          <path
            v-for="(item, idx) in allocation.items"
            :key="item.label"
            :stroke="item.color"
            :stroke-dasharray="`${item.percentage} ${100 - item.percentage}`"
            :stroke-dashoffset="getCumulativeOffset(allocation.items, idx)"
            stroke-width="4.5"
            stroke-linecap="butt"
            fill="none"
            d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
            class="transition-all duration-500"
          />
        </svg>

        <!-- Center Value -->
        <div
          class="absolute inset-0 flex flex-col items-center justify-center text-center pointer-events-none"
        >
          <span class="text-xl font-bold text-white tracking-tight">{{
            allocation.totalValue
          }}</span>
          <span class="text-[10px] text-[#8994a2]">Total value</span>
        </div>
      </div>
    </div>

    <!-- Legend -->
    <div class="grid grid-cols-2 gap-y-2.5 gap-x-4 pt-2 text-xs">
      <div
        v-for="item in allocation.items"
        :key="item.label"
        class="flex items-center justify-between"
      >
        <div class="flex items-center gap-2">
          <span
            class="size-2 rounded-full shrink-0"
            :style="{ backgroundColor: item.color }"
          ></span>
          <span class="text-[#8994a2] text-[11px]">{{ item.label }}</span>
        </div>
        <span class="font-semibold text-white text-[11px]">{{ item.percentage }}%</span>
      </div>
    </div>
  </BaseCard>
</template>
