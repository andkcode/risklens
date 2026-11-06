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
    class="lg:col-span-6 flex flex-col justify-between"
  >
    <template #header-actions>
      <button class="text-[10px] text-[#4c8dff] hover:underline font-medium">View scanner →</button>
    </template>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
      <!-- Stocks Table -->
      <div>
        <div class="text-[10px] font-semibold text-[#8994a2] mb-2 flex justify-between">
          <span>Stocks</span>
          <span>3 monitored</span>
        </div>
        <div class="border border-[#222830] rounded-lg overflow-hidden">
          <table class="w-full text-left text-xs">
            <thead class="bg-[#0d1115] text-[#606b78] text-[8px] uppercase">
              <tr>
                <th class="py-2 px-2">Asset</th>
                <th class="py-2 px-2">Price</th>
                <th class="py-2 px-2">Risk</th>
                <th class="py-2 px-2 text-right">24h</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-[#1c2229]">
              <tr v-for="stock in stocks" :key="stock.id" class="hover:bg-[#141a21]">
                <td
                  class="py-2 px-2 font-semibold text-white flex items-center gap-1.5 whitespace-nowrap"
                >
                  <span
                    class="size-5 rounded bg-[#1b222c] grid place-items-center text-[9px] text-emerald-400 shrink-0"
                  >
                    {{ stock.name[0] }}
                  </span>
                  <span class="truncate">{{ stock.name }}</span>
                </td>
                <td class="py-2 px-2 text-[#8994a2] whitespace-nowrap">{{ stock.price }}</td>
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
        <div class="text-[10px] font-semibold text-[#8994a2] mb-2 flex justify-between">
          <span>Crypto assets</span>
          <span>3 monitored</span>
        </div>
        <div class="border border-[#222830] rounded-lg overflow-hidden">
          <table class="w-full text-left text-xs">
            <thead class="bg-[#0d1115] text-[#606b78] text-[8px] uppercase">
              <tr>
                <th class="py-2 px-2">Asset</th>
                <th class="py-2 px-2">Price</th>
                <th class="py-2 px-2">Risk</th>
                <th class="py-2 px-2 text-right">24h</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-[#1c2229]">
              <tr v-for="crypto in crypto" :key="crypto.id" class="hover:bg-[#141a21]">
                <td
                  class="py-2 px-2 font-semibold text-white flex items-center gap-1.5 whitespace-nowrap"
                >
                  <span
                    class="size-5 rounded bg-[#1b222c] grid place-items-center text-[9px] text-amber-400 shrink-0"
                  >
                    {{ crypto.name[0] }}
                  </span>
                  <span class="truncate">{{ crypto.name }}</span>
                </td>
                <td class="py-2 px-2 text-[#8994a2] whitespace-nowrap">{{ crypto.price }}</td>
                <td class="py-2 px-2 whitespace-nowrap">
                  <!-- Dynamic risk badge styling based on riskLevel -->
                  <span
                    class="px-1.5 py-0.5 rounded text-[10px] font-semibold"
                    :class="getRiskBadgeClass(crypto.riskLevel)"
                  >
                    {{ crypto.riskScore }}
                  </span>
                </td>
                <!-- Dynamic trend color for crypto change -->
                <td
                  class="py-2 px-2 text-right whitespace-nowrap"
                  :class="getTrendColorClass(crypto.change24h)"
                >
                  {{ crypto.change24h }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </BaseCard>
</template>
