<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import type { WatchlistAsset } from '../../types/dashboard';
import { getRiskBadgeClass, getTrendColorClass } from '../../utils/riskStyle';
import BaseCard from '../BaseCard.vue';

defineProps<{
  stocks: WatchlistAsset[];
  crypto: WatchlistAsset[];
}>();
</script>

<template>
  <BaseCard
    variant="standard"
    title="Asset risk watchlist"
    subtitle="Highest-risk positions · Real-time"
    class="lg:col-span-6 flex flex-col"
  >
    <template #header-actions>
      <button class="text-[10px] text-brand-accent hover:underline font-medium">View scanner →</button>
    </template>

    <!-- Vertical stack of two separate tables: Stocks and Crypto -->
    <div class="flex flex-col space-y-4">
      <!-- Stocks Table -->
      <div>
        <div class="text-[10px] font-semibold text-brand-secondary mb-2 flex justify-between">
          <span>Stocks</span>
          <span>{{ stocks.length }} monitored</span>
        </div>
        <div class="border border-brand-border rounded-lg overflow-hidden">
          <table class="w-full text-left text-xs">
            <thead class="bg-brand-sidebar text-brand-muted-2 text-[8px] uppercase">
            <tr>
              <th class="py-2 px-2">Asset</th>
              <th class="py-2 px-2">Price</th>
              <th class="py-2 px-2">Risk</th>
              <th class="py-2 px-2 text-right">24h</th>
            </tr>
            </thead>
            <tbody class="divide-y divide-brand-border">
            <tr v-for="stock in stocks" :key="stock.id" class="hover:bg-brand-surface-hover">
              <td
                class="py-2 px-2 font-semibold text-brand-primary flex items-center gap-1.5 whitespace-nowrap"
              >
                  <span
                    class="size-5 rounded bg-brand-surface-raised grid place-items-center text-[9px] text-market-success shrink-0"
                  >
                    {{ stock.name[0] }}
                  </span>
                <span class="truncate">{{ stock.name }}</span>
              </td>
              <td class="py-2 px-2 text-brand-secondary whitespace-nowrap">{{ stock.price }}</td>
              <td class="py-2 px-2 whitespace-nowrap">
                <!-- Dynamic risk badge styling based on riskLevel -->
                <span
                  class="px-1.5 py-0.5 rounded text-[10px] font-semibold"
                  :class="getRiskBadgeClass(stock.riskLevel)"
                >
                    {{ stock.riskScore }}
                  </span>
              </td>
              <!-- Dynamic trend color for stocks change -->
              <td
                class="py-2 px-2 text-right whitespace-nowrap"
                :class="getTrendColorClass(stock.change24h)"
              >
                {{ stock.change24h }}
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Crypto Table -->
      <div>
        <div class="text-[10px] font-semibold text-brand-secondary mb-2 flex justify-between">
          <span>Crypto assets</span>
          <span>{{ crypto.length }} monitored</span>
        </div>
        <div class="border border-brand-border rounded-lg overflow-hidden">
          <table class="w-full text-left text-xs">
            <thead class="bg-brand-sidebar text-brand-muted-2 text-[8px] uppercase">
            <tr>
              <th class="py-2 px-2">Asset</th>
              <th class="py-2 px-2">Price</th>
              <th class="py-2 px-2">Risk</th>
              <th class="py-2 px-2 text-right">24h</th>
            </tr>
            </thead>
            <tbody class="divide-y divide-brand-border">
            <tr v-for="cryptoItem in crypto" :key="cryptoItem.id" class="hover:bg-brand-surface-hover">
              <td
                class="py-2 px-2 font-semibold text-brand-primary flex items-center gap-1.5 whitespace-nowrap"
              >
                  <span
                    class="size-5 rounded bg-brand-surface-raised grid place-items-center text-[9px] text-amber-400 shrink-0"
                  >
                    {{ cryptoItem.name[0] }}
                  </span>
                <span class="truncate">{{ cryptoItem.name }}</span>
              </td>
              <td class="py-2 px-2 text-brand-secondary whitespace-nowrap">{{ cryptoItem.price }}</td>
              <td class="py-2 px-2 whitespace-nowrap">
                <!-- Dynamic risk badge styling based on riskLevel -->
                <span
                  class="px-1.5 py-0.5 rounded text-[10px] font-semibold"
                  :class="getRiskBadgeClass(cryptoItem.riskLevel)"
                >
                    {{ cryptoItem.riskScore }}
                  </span>
              </td>
              <!-- Dynamic trend color for crypto change -->
              <td
                class="py-2 px-2 text-right whitespace-nowrap"
                :class="getTrendColorClass(cryptoItem.change24h)"
              >
                {{ cryptoItem.change24h }}
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </BaseCard>
</template>
