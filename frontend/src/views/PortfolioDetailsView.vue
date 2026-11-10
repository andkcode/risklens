<script setup lang="ts">
import { usePortfolioDetails } from '../composables/usePortfolioDetails';
import PortfolioHeader from '../components/portfolio/PortfolioHeader.vue';
import PortfolioMetrics from '../components/portfolio/PortfolioMetrics.vue';
import PortfolioPositionsTable from '../components/portfolio/PortfolioPositionsTable.vue';
import PortfolioAddForms from '../components/portfolio/PortfolioAddForms.vue';

const { portfolio, isLoading, errorMessage, handleExport, handleRunAnalysis } =
  usePortfolioDetails();
</script>

<template>
  <div class="h-full overflow-y-auto">
    <div class="p-6 md:p-8 max-w-[1600px] mx-auto min-h-full">
      <!-- Loading State -->
      <div v-if="isLoading" class="flex items-center justify-center py-20">
        <div class="flex flex-col items-center gap-3">
          <div
            class="size-8 rounded-full border-2 border-brand-accent border-t-transparent animate-spin"
          ></div>
          <p class="text-xs text-brand-muted">Loading portfolio data...</p>
        </div>
      </div>

      <!-- Error State -->
      <div
        v-else-if="errorMessage"
        class="flex flex-col items-center justify-center py-20 text-center"
      >
        <div class="text-rose-400 text-sm font-semibold mb-1">{{ errorMessage }}</div>
        <p class="text-xs text-brand-muted">Please select another portfolio from the sidebar.</p>
      </div>

      <!-- Loaded Content -->
      <div v-else-if="portfolio" class="space-y-6">
        <!-- 1. Header & Action Buttons -->
        <PortfolioHeader
          :name="portfolio.name"
          :description="portfolio.description"
          :code="portfolio.code"
          @export="handleExport"
          @run-analysis="handleRunAnalysis"
        />

        <!-- 2. Metrics Summary Cards -->
        <PortfolioMetrics
          :total-value="portfolio.totalValue"
          :value-change-today="portfolio.valueChangeToday"
          :performance-ytd="portfolio.performanceYtd"
          :performance-comparison="portfolio.performanceComparison"
          :risk-score="portfolio.riskScore"
          :risk-level="portfolio.riskLevel"
          :last-analyzed="portfolio.lastAnalyzed"
        />

        <!-- 3. Comprehensive Positions Table -->
        <PortfolioPositionsTable
          :positions="portfolio.positions"
          :cost-basis="portfolio.costBasis"
          :unrealized-profit="portfolio.unrealizedProfit"
        />

        <!-- 4. Add New Assets Forms -->
        <PortfolioAddForms :portfolio-name="portfolio.name" />
      </div>
    </div>
  </div>
</template>
