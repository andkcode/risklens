<script setup lang="ts">
import { usePortfolios } from '../composables/usePortfolios';
import { getPortfolioBadgeColor } from '../utils/riskStyle';
import BaseButton from '../components/BaseButton.vue';
import BaseCard from '../components/BaseCard.vue';

const {
  portfolios,
  activeCount,
  combinedValue,
  combinedChange,
  selectedId,
  handleSelect,
  handleCreate,
} = usePortfolios();
</script>

<template>
  <div class="flex h-full min-h-0">
    <aside
      class="w-76 h-full min-h-0 shrink-0 border-r border-[#1a212c] bg-[#0c1014] flex flex-col justify-between p-4 select-none"
    >
      <div class="flex flex-col h-full space-y-4">
        <!-- Header: Portfolios & Add Button -->
        <div class="flex items-center justify-between px-1 pt-1">
          <div>
            <h2 class="text-[11px] font-bold text-[#f0f3f6] tracking-[0.08em] uppercase">
              Portfolios
            </h2>
            <p class="text-[10px] text-[#717d8a] mt-0.5 font-normal">{{ activeCount }} active</p>
          </div>
          <BaseButton
            variant="secondary"
            size="icon"
            class="size-8 bg-[#131922] border-[#222c38] hover:border-brand-accent hover:text-brand-accent rounded-lg shadow-sm transition-colors"
            @click="handleCreate"
          >
            <svg
              class="w-4 h-4 text-[#f0f3f6]"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="2.5"
            >
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4" />
            </svg>
          </BaseButton>
        </div>

        <!-- Portfolios List -->
        <div class="space-y-2.5 overflow-y-auto pr-1 flex-1">
          <div
            v-for="item in portfolios"
            :key="item.id"
            class="relative p-3 rounded-xl border transition-all cursor-pointer flex items-center justify-between group"
            :class="[
              String(selectedId) === String(item.id)
                ? 'bg-[linear-gradient(90deg,rgba(24,35,52,0.6)_0%,rgba(16,23,34,0.9)_100%)] border-[#2f70df] shadow-[0_4px_20px_rgba(35,95,200,0.18)]'
                : 'bg-brand-surface border-[#1d2633] hover:border-[#2a3648] hover:bg-[#141a23]',
            ]"
            @click="handleSelect(String(item.id))"
          >
            <!-- Active selection indicator bar on the left -->
            <div
              v-if="String(selectedId) === String(item.id)"
              class="absolute left-0 top-3 bottom-3 w-0.75 bg-[#4386f5] rounded-r"
            ></div>

            <div class="flex items-center gap-3 min-w-0 pl-1">
              <!-- Badge code styled via riskStyle utility function -->
              <span
                class="size-9 rounded-lg bg-[#151c27] border border-[#253142] flex items-center justify-center text-xs font-bold shrink-0 shadow-inner"
                :class="getPortfolioBadgeColor(item.code)"
              >
                {{ item.code }}
              </span>
              <div class="min-w-0">
                <h4 class="text-xs font-semibold text-[#f0f3f6] truncate tracking-tight">
                  {{ item.name }}
                </h4>
                <p class="text-[10px] text-[#717d8a] mt-0.5 truncate font-normal">
                  {{ item.assetsCount }} assets · {{ item.totalValue }}
                </p>
              </div>
            </div>
            <span
              class="text-[#4e5a6b] group-hover:text-[#f0f3f6] text-xs font-medium pl-2 transition-colors"
            >
              ›
            </span>
          </div>
        </div>

        <!-- Combined Portfolio Value Widget at Bottom -->
        <BaseCard
          variant="standard"
          class="p-4 bg-brand-surface border-[#1d2633] rounded-xl shadow-lg mt-auto"
        >
          <div class="text-[10px] text-[#717d8a] font-medium tracking-wide uppercase">
            Combined portfolio value
          </div>
          <div class="text-2xl font-bold text-[#f0f3f6] mt-1 font-mono tracking-tight">
            {{ combinedValue }}
          </div>
          <div class="text-[10px] text-market-success mt-1.5 flex items-center gap-1 font-semibold">
            <span class="text-xs">↗</span> {{ combinedChange }}
          </div>
        </BaseCard>
      </div>
    </aside>
    <div class="h-full min-w-0 flex-1">
      <RouterView />
    </div>
  </div>
</template>
