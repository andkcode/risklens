<script setup lang="ts">
import { usePortfolioDetails } from '../composables/usePortfolioDetails';
import PortfolioHeader from '../components/portfolio/PortfolioHeader.vue';
import PortfolioMetrics from '../components/portfolio/PortfolioMetrics.vue';
import PortfolioPositionsTable from '../components/portfolio/PortfolioPositionsTable.vue';
import PortfolioAddForms from '../components/portfolio/PortfolioAddForms.vue';

const {
  portfolio,
  isLoading,
  errorMessage,
  stockForm,
  cryptoForm,
  handleAddStock,
  handleAddCrypto,
  handleExport,
  handleRunAnalysis,
} = usePortfolioDetails();
</script>

<template>
  <div class="h-full overflow-y-auto p-6 space-y-6">
    <!-- Loading State -->
    <div v-if="isLoading" class="flex justify-center py-20">
      <div
        class="size-8 rounded-full border-2 border-brand-accent border-t-transparent animate-spin"
      ></div>
    </div>

    <!-- Error State -->
    <div v-else-if="errorMessage" class="text-rose-400 text-xs">
      {{ errorMessage }}
    </div>

    <!-- Content Components -->
    <template v-else-if="portfolio">
      <PortfolioHeader
        :portfolio="portfolio"
        @export="handleExport"
        @run-analysis="handleRunAnalysis"
      />
      <PortfolioMetrics :portfolio="portfolio" />
      <PortfolioPositionsTable :portfolio="portfolio" />
      <PortfolioAddForms
        v-model:stock-form="stockForm"
        v-model:crypto-form="cryptoForm"
        @add-stock="handleAddStock"
        @add-crypto="handleAddCrypto"
      />
    </template>
  </div>
</template>
