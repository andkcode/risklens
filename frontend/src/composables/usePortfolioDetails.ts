import { ref, watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import type { PortfolioDetailData } from '../types/portfolio';
import { portfolioService } from '../services/portfoliosService';

export function usePortfolioDetails() {
  const route = useRoute();

  const portfolio = ref<PortfolioDetailData | null>(null);
  const isLoading = ref(true);
  const errorMessage = ref('');

  // Fetch portfolio details via portfolioService directly assigned to portfolio.value
  const loadPortfolioData = async (id: string | string[]) => {
    isLoading.value = true;
    errorMessage.value = '';
    try {
      const portfolioId = Array.isArray(id) ? id[0] : id;
      if (!portfolioId) return;
      portfolio.value = await portfolioService.getPortfolioById(portfolioId);
    } catch (error) {
      console.error('Failed to load portfolio details:', error);
      errorMessage.value = 'Portfolio not found or unavailable.';
      portfolio.value = null;
    } finally {
      isLoading.value = false;
    }
  };

  // Load data on initial mount with fallback to '1'
  onMounted(() => {
    const id = (route.params.id as string) || '1';
    void loadPortfolioData(id);
  });

  // Watch for route parameter changes (switching portfolios in sidebar)
  watch(
    () => route.params.id,
    (newId) => {
      if (newId) {
        void loadPortfolioData(newId as string);
      }
    },
  );

  const handleExport = () => {
    console.log('Exporting portfolio report...');
  };

  const handleRunAnalysis = () => {
    console.log('Running AI Risk Analysis...');
  };

  return {
    portfolio,
    isLoading,
    errorMessage,
    handleExport,
    handleRunAnalysis,
  };
}
