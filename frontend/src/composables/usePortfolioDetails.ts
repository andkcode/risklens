import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { storeToRefs } from 'pinia';
import { usePortfoliosStore } from '../stores/portfolios';

export function usePortfolioDetails() {
  const route = useRoute();
  const store = usePortfoliosStore();

  const {
    currentPortfolio: portfolio,
    isLoadingDetails: isLoading,
    error: errorMessage,
  } = storeToRefs(store);

  const stockForm = ref({ ticker: '', quantity: '', price: '', date: '' });
  const cryptoForm = ref({ search: '', quantity: '', price: '', network: '' });

  const loadPortfolioData = async (id: string | string[]) => {
    const portfolioId = Array.isArray(id) ? id[0] : id;
    if (!portfolioId) return;
    await store.fetchPortfolioById(portfolioId);
  };

  onMounted(() => {
    const id = (route.params.id as string) || '1';
    void loadPortfolioData(id);
  });

  watch(
    () => route.params.id,
    (newId) => {
      if (newId) void loadPortfolioData(newId as string);
    },
  );

  const handleAddStock = () => {
    if (!stockForm.value.ticker) return;
    console.log('Add stock submitted:', stockForm.value);
  };

  const handleAddCrypto = () => {
    if (!cryptoForm.value.search) return;
    console.log('Add crypto submitted:', cryptoForm.value);
  };

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
    stockForm,
    cryptoForm,
    handleAddStock,
    handleAddCrypto,
    handleExport,
    handleRunAnalysis,
  };
}
