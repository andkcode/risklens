<script setup lang="ts">
import { onMounted } from 'vue';
import { useDashboardStore } from '../stores/dashboard';
import BaseButton from '../components/BaseButton.vue';
import DashboardIcon from '../../../../../WebstormProjects/risklens/frontend/src/components/icons/DashboardIcons.vue';

// Импорт модульных компонентов дашборда
import DashboardMetricsGrid from '../../../../../WebstormProjects/risklens/frontend/src/components/dashboard/MetricsGrid.vue';
import DashboardWatchlist from '../../../../../WebstormProjects/risklens/frontend/src/components/dashboard/Watchlist.vue';
import DashboardAlerts from '../../../../../WebstormProjects/risklens/frontend/src/components/dashboard/Alerts.vue';
import DashboardHistoryLog from '../../../../../WebstormProjects/risklens/frontend/src/components/dashboard/HistoryLog.vue';
import DashboardPortfolioAllocation from '../../../../../WebstormProjects/risklens/frontend/src/components/dashboard/PortfolioAllocation.vue';

const dashboardStore = useDashboardStore();

onMounted(() => {
  dashboardStore.fetchDashboardData();
});
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
            <BaseButton variant="secondary" size="md">
              <DashboardIcon name="download" :size="14" class="mr-2" />
              <span>Export report</span>
            </BaseButton>
            <BaseButton variant="primary" size="md">
              <DashboardIcon name="plus" :size="14" class="mr-2" />
              <span>Add position</span>
            </BaseButton>
          </div>
        </div>

        <!-- 1. Top Metrics Cards -->
        <DashboardMetricsGrid :metrics="dashboardStore.metrics" />

        <!-- 2. Watchlist, Portfolio Allocation & Alerts Row (Equal height cards, space at bottom) -->
        <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
          <DashboardWatchlist
            :stocks="dashboardStore.watchlist.stocks"
            :crypto="dashboardStore.watchlist.crypto"
            class="lg:col-span-6"
          />
          <DashboardPortfolioAllocation
            :allocation="dashboardStore.allocation"
            class="lg:col-span-3"
          />
          <DashboardAlerts :alerts="dashboardStore.alerts" class="lg:col-span-3" />
        </div>

        <!-- 3. Analysis History Log -->
        <DashboardHistoryLog :history="dashboardStore.history" />
      </main>
    </div>
  </div>
</template>
