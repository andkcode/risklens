import { ref, onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import type { PortfolioSummary } from '../types/portfolio';
import { portfolioService } from '../services/portfoliosService';

export function usePortfolios() {
  const router = useRouter();
  const route = useRoute();

  const portfolios = ref<PortfolioSummary[]>([]);
  const activeCount = ref(0);
  const combinedValue = ref('$0');
  const combinedChange = ref('');

  const selectedId = ref<string>((route.params.id as string) || '1');

  const loadPortfolios = async () => {
    try {
      const response = await portfolioService.getPortfolios();
      portfolios.value = response.items;
      activeCount.value = response.activeCount;
      combinedValue.value = response.combinedValue;
      combinedChange.value = response.combinedChange;
    } catch (error) {
      console.error('Failed to load portfolios list:', error);
    }
  };

  const handleSelect = (id: string) => {
    selectedId.value = id;
    void router.push({ name: 'portfolio-detail', params: { id } });
  };

  const handleCreate = () => {
    console.log('Open create portfolio modal');
  };

  watch(
    () => route.params.id,
    (newId) => {
      if (newId) {
        selectedId.value = newId as string;
      }
    },
  );

  onMounted(() => {
    void loadPortfolios();
  });

  return {
    portfolios,
    activeCount,
    combinedValue,
    combinedChange,
    selectedId,
    handleSelect,
    handleCreate,
  };
}
