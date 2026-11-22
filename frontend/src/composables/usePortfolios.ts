import { onMounted, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { storeToRefs } from 'pinia';
import { usePortfoliosStore } from '../stores/portfolios';

export function usePortfolios() {
  const router = useRouter();
  const route = useRoute();
  const store = usePortfoliosStore();

  const {
    portfolios,
    activeCount,
    combinedValue,
    combinedChange,
    selectedId,
    isLoadingList,
    error,
  } = storeToRefs(store);

  const handleSelect = (id: string) => {
    store.selectedId = id;
    void router.push({ name: 'portfolio-detail', params: { id } });
  };

  const handleCreate = () => {
    console.log('Open create portfolio modal');
  };

  watch(
    () => route.params.id,
    (newId) => {
      if (newId) {
        store.selectedId = newId as string;
      }
    },
  );

  onMounted(() => {
    void store.fetchPortfolios();
  });

  return {
    portfolios,
    activeCount,
    combinedValue,
    combinedChange,
    selectedId,
    isLoadingList,
    error,
    handleSelect,
    handleCreate,
  };
}
