<script setup lang="ts">
import { useDashboard } from '../composables/useDashboard';
import BaseButton from '../components/BaseButton.vue';
import DashboardIcon from '../components/icons/DashboardIcons.vue';

import DashboardMetricsGrid from '../components/dashboard/MetricsGrid.vue';
import DashboardWatchlist from '../components/dashboard/Watchlist.vue';
import DashboardAlerts from '../components/dashboard/Alerts.vue';
import DashboardHistoryLog from '../components/dashboard/HistoryLog.vue';
import DashboardPortfolioAllocation from '../components/dashboard/PortfolioAllocation.vue';

const {
  metrics,
  watchlist,
  alerts,
  history,
  allocation,
  isLoading,
  error,
  handleExportReport,
  handleAddPosition,
} = useDashboard();
</script>

<template>
  <div class="flex min-h-screen bg-[#090d12] text-[#e2e6eb]">
    <!-- Main Content Area -->
    <div class="flex-1 flex flex-col min-w-0">
      <!-- Dashboard Body -->
      <main class="flex-1 p-6 space-y-6 overflow-y-auto">
        <!-- Title & Action Buttons -->
        <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div class="text-[9px] font-semibold uppercase tracking-wider text-brand-accent">
              Risk Intelligence / Overview
            </div>
            <h1 class="text-xl font-bold tracking-tight text-white mt-0.5">Dashboard</h1>
            <p class="text-xs text-brand-secondary mt-0.5">
              Real-time portfolio health, market exposure, and historical risk intelligence.
            </p>
          </div>
          <div class="flex items-center gap-3">
            <BaseButton variant="secondary" size="md" @click="handleExportReport">
              <DashboardIcon name="download" :size="14" class="mr-2" />
              <span>Export report</span>
            </BaseButton>
            <BaseButton variant="primary" size="md" @click="handleAddPosition">
              <DashboardIcon name="plus" :size="14" class="mr-2" />
              <span>Add position</span>
            </BaseButton>
          </div>
        </div>

        <!-- Loading State -->
        <div v-if="isLoading" class="flex items-center justify-center py-24">
          <div
            class="size-8 rounded-full border-2 border-brand-accent border-t-transparent animate-spin"
          ></div>
        </div>

        <!-- Error State -->
        <div
          v-else-if="error"
          class="p-4 text-xs text-rose-400 bg-rose-500/10 border border-rose-500/20 rounded-lg"
        >
          {{ error }}
        </div>

        <!-- Loaded Content Grid -->
        <template v-else>
          <!-- 1. Top Metrics Cards -->
          <DashboardMetricsGrid :metrics="metrics" />

          <!-- 2. Watchlist, Portfolio Allocation & Alerts Row -->
          <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
            <DashboardWatchlist
              :stocks="watchlist.stocks"
              :crypto="watchlist.crypto"
              class="lg:col-span-6"
            />
            <DashboardPortfolioAllocation :allocation="allocation" class="lg:col-span-3" />
            <DashboardAlerts :alerts="alerts" class="lg:col-span-3" />
          </div>

          <!-- 3. Analysis History Log -->
          <DashboardHistoryLog :history="history" />
        </template>
      </main>
    </div>
  </div>
</template>
