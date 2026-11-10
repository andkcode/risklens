<script setup lang="ts">
import { ref, computed } from 'vue';
import BaseCard from '../BaseCard.vue';
import BaseTable from '../BaseTable.vue';
import BaseIcon from '../icons/PortfolioIcons.vue';
import { getRiskBadgeClass } from '../../utils/riskStyle';

interface Position {
  id: string;
  name: string;
  status: string;
  ticker: string;
  quantity: number;
  avgPurchasePrice: string;
  currentMarketValue: string;
  allocationPercent: number;
  riskRating: string;
  profitLoss: string;
  isPositive: boolean;
}

const props = defineProps<{
  positions?: Position[];
  costBasis?: string;
  unrealizedProfit?: string;
}>();

const searchQuery = ref('');

const filteredPositions = computed(() => {
  if (!props.positions) return [];
  if (!searchQuery.value.trim()) return props.positions;
  const q = searchQuery.value.toLowerCase();
  return props.positions.filter(
    (p) => p.name.toLowerCase().includes(q) || p.ticker.toLowerCase().includes(q),
  );
});
</script>

<template>
  <BaseCard
    title="Comprehensive positions"
    :subtitle="`${positions?.length || 0} active positions · Market values update in real time`"
    variant="standard"
    class="my-6"
  >
    <!-- Header Actions (Search Input) -->
    <template #header-actions>
      <div class="relative w-72 max-w-[45%] shrink-0">
        <span
          class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-brand-muted"
        >
          <BaseIcon name="search" />
        </span>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search positions..."
          class="w-full bg-brand-sidebar border border-brand-border rounded-lg pl-9 pr-4 py-2 text-xs text-white placeholder-brand-muted focus:outline-none focus:ring-1 focus:ring-brand-accent transition-colors"
        />
      </div>
    </template>

    <!-- BaseTable Component -->
    <BaseTable>
      <template #header>
        <th class="p-3">Asset name</th>
        <th class="p-3">Ticker</th>
        <th class="p-3">Quantity</th>
        <th class="p-3">Avg. purchase price</th>
        <th class="p-3">Current market value</th>
        <th class="p-3">Allocation</th>
        <th class="p-3">Risk rating</th>
        <th class="p-3 text-right">P/L</th>
      </template>
      <template #body>
        <tr
          v-for="pos in filteredPositions"
          :key="pos.id"
          class="hover:bg-[#141a21] text-xs transition-colors group"
        >
          <!-- Asset Name -->
          <td class="p-3">
            <div class="flex items-center gap-3">
              <div
                class="size-7 rounded bg-brand-sidebar border border-brand-border flex items-center justify-center font-bold text-brand-accent text-xs shrink-0"
              >
                {{ pos.ticker.charAt(0) }}
              </div>
              <div>
                <div
                  class="font-semibold text-white group-hover:text-brand-accent transition-colors"
                >
                  {{ pos.name }}
                </div>
                <div class="text-[10px] text-brand-secondary mt-0.5">{{ pos.status }}</div>
              </div>
            </div>
          </td>
          <!-- Ticker -->
          <td class="p-3 font-mono font-medium text-white">{{ pos.ticker }}</td>
          <!-- Quantity -->
          <td class="p-3 font-mono text-white">{{ pos.quantity }}</td>
          <!-- Avg Purchase Price -->
          <td class="p-3 font-mono text-brand-secondary">{{ pos.avgPurchasePrice }}</td>
          <!-- Current Market Value -->
          <td class="p-3 font-mono font-semibold text-white">
            {{ pos.currentMarketValue }}
          </td>
          <!-- Allocation Progress Bar -->
          <td class="p-3">
            <div class="flex items-center gap-2">
              <span class="font-mono text-[11px] text-white w-9">{{ pos.allocationPercent }}%</span>
              <div class="w-20 h-1.5 bg-brand-border rounded-full overflow-hidden">
                <div
                  class="h-full bg-brand-accent rounded-full"
                  :style="{ width: `${Math.min(pos.allocationPercent, 100)}%` }"
                ></div>
              </div>
            </div>
          </td>
          <!-- Risk Rating Badge -->
          <td class="p-3">
            <span
              :class="[
                'px-2.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wider',
                getRiskBadgeClass(pos.riskRating),
              ]"
            >
              {{ pos.riskRating }}
            </span>
          </td>
          <!-- P/L -->
          <td
            class="p-3 text-right font-mono font-semibold"
            :class="pos.isPositive ? 'text-emerald-400' : 'text-rose-400'"
          >
            {{ pos.profitLoss }}
          </td>
        </tr>
        <tr v-if="filteredPositions.length === 0">
          <td colspan="8" class="py-8 text-center text-brand-secondary text-xs">
            No matching positions found.
          </td>
        </tr>
      </template>
    </BaseTable>

    <!-- Table Footer Summary -->
    <div
      class="mt-4 pt-4 border-t border-brand-border flex flex-col sm:flex-row items-center justify-between text-xs text-brand-secondary gap-2"
    >
      <div>
        Portfolio cost basis:
        <span class="font-mono font-semibold text-white">{{ costBasis || '$0' }}</span>
      </div>
      <div>
        Unrealized profit:
        <span class="font-mono font-semibold text-emerald-400">{{
          unrealizedProfit || '+$0'
        }}</span>
      </div>
    </div>
  </BaseCard>
</template>
