import { defineStore } from 'pinia';
import { ref } from 'vue';
import { portfolioService } from '../services/portfoliosService';
import type { PortfolioSummary, PortfolioDetailData } from '../types/portfolios';

export const usePortfoliosStore = defineStore('portfolios', () => {
  const portfolios = ref<PortfolioSummary[]>([]);
  const activeCount = ref(0);
  const combinedValue = ref('$0');
  const combinedChange = ref('');

  const currentPortfolio = ref<PortfolioDetailData | null>(null);
  const selectedId = ref<string>('1');

  const isLoadingList = ref(false);
  const isLoadingDetails = ref(false);
  const error = ref<string | null>(null);

  // Fetch all portfolios list for sidebar/overview
  async function fetchPortfolios() {
    isLoadingList.value = true;
    error.value = null;
    try {
      const response = await portfolioService.getPortfolios();
      portfolios.value = response.items;
      activeCount.value = response.activeCount;
      combinedValue.value = response.combinedValue;
      combinedChange.value = response.combinedChange;
    } catch (err: unknown) {
      const errorResponse = err as { response?: { data?: { message?: string } } };
      error.value = errorResponse.response?.data?.message || 'Failed to load portfolios list.';
    } finally {
      isLoadingList.value = false;
    }
  }

  // Fetch specific portfolio details by ID
  async function fetchPortfolioById(id: string) {
    isLoadingDetails.value = true;
    error.value = null;
    try {
      selectedId.value = id;
      currentPortfolio.value = await portfolioService.getPortfolioById(id);
    } catch (err: unknown) {
      const errorResponse = err as { response?: { data?: { message?: string } } };
      error.value = errorResponse.response?.data?.message || 'Portfolio not found or unavailable.';
      currentPortfolio.value = null;
    } finally {
      isLoadingDetails.value = false;
    }
  }

  return {
    portfolios,
    activeCount,
    combinedValue,
    combinedChange,
    currentPortfolio,
    selectedId,
    isLoadingList,
    isLoadingDetails,
    error,
    fetchPortfolios,
    fetchPortfolioById,
  };
});
