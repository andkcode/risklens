<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { portfolioService, type PortfolioResponseDto } from '../api/portfolioService';
import { riskService, type RiskAssessmentResponseDto } from '../api/riskService';
import { isAxiosError } from 'axios';

const selectedPortfolio = ref<PortfolioResponseDto | null>(null);
const latestAssessment = ref<RiskAssessmentResponseDto | null>(null);
const isLoading = ref(true);
const errorMessage = ref('');

const fetchDashboardData = async () => {
  isLoading.value = true;
  errorMessage.value = '';
  try {
    const portfolios = await portfolioService.getPortfolios(0, 1);
    selectedPortfolio.value = portfolios.content[0] ?? null;

    if (!selectedPortfolio.value) {
      latestAssessment.value = null;
      return;
    }

    const pageData = await riskService.getRiskAssessments(selectedPortfolio.value.id, 0, 1);
    if (pageData.content.length > 0) {
      latestAssessment.value = pageData.content[0];
    } else {
      latestAssessment.value = null;
    }
  } catch (err: unknown) {
    if (isAxiosError(err)) {
      const responseData = err.response?.data;
      errorMessage.value =
        (typeof responseData?.detail === 'string' && responseData.detail) ||
        (typeof responseData?.message === 'string' && responseData.message) ||
        (err.response?.status === 401
          ? 'Your session has expired. Please sign in again.'
          : 'Failed to load portfolio risk metrics.');
    } else {
      errorMessage.value = 'An unexpected error occurred.';
    }
  } finally {
    isLoading.value = false;
  }
};

const formatVaR = (value: number): string =>
  `${(value * 100).toFixed(2)}%`;

onMounted(() => {
  void fetchDashboardData();
});
</script>

<template>
  <div class="p-6 space-y-6 bg-brand-bg min-h-screen text-brand-primary">
    <!-- Header -->
    <div>
      <h1 class="text-2xl font-bold tracking-tight text-brand-primary">Dashboard of Risks</h1>
      <p class="text-sm text-brand-secondary mt-1">
        Real-time portfolio exposure and downside risk analytics.
      </p>
    </div>

    <!-- Loading State -->
    <div v-if="isLoading" class="flex items-center justify-center py-16">
      <p class="text-sm font-medium text-brand-secondary animate-pulse">
        Loading risk metrics from backend...
      </p>
    </div>

    <!-- Error State -->
    <div
      v-else-if="errorMessage"
      class="rounded-lg bg-rose-500/10 border border-rose-500/20 p-4 text-xs text-rose-500 text-center font-medium"
    >
      {{ errorMessage }}
    </div>

    <!-- Dashboard Content Cards -->
    <div v-else-if="latestAssessment" class="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
      <!-- Portfolio ID Card -->
      <div class="rounded-xl border border-brand-border bg-brand-card p-5 shadow-sm">
        <span class="text-xs font-semibold uppercase tracking-wider text-brand-secondary">Portfolio</span>
        <p class="mt-2 text-sm text-brand-primary truncate">{{ selectedPortfolio?.name }}</p>
      </div>

      <!-- Overall VaR Card -->
      <div class="rounded-xl border border-brand-border bg-brand-card p-5 shadow-sm">
        <span class="text-xs font-semibold uppercase tracking-wider text-brand-secondary">
          VaR (95%)
        </span>
        <p class="mt-2 text-3xl font-bold text-rose-500">{{ formatVaR(latestAssessment.var95) }}</p>
      </div>

      <!-- Active Risk Score Card -->
      <div class="rounded-xl border border-brand-border bg-brand-card p-5 shadow-sm">
        <span class="text-xs font-semibold uppercase tracking-wider text-brand-secondary">
          Active Risk Score
        </span>
        <p class="mt-2 text-3xl font-bold text-brand-accent">{{ latestAssessment.riskScore }}</p>
      </div>
    </div>

    <!-- Empty State if no assessments found -->
    <div v-else class="text-center py-12 text-brand-secondary">
      <p v-if="selectedPortfolio">No risk assessments found for {{ selectedPortfolio.name }}.</p>
      <p v-else>No portfolios found. Create a portfolio to view risk metrics.</p>
    </div>
  </div>
</template>
