import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useDashboardStore } from '../stores/dashboard';

export function useDashboard() {
  const dashboardStore = useDashboardStore();
  const { metrics, watchlist, alerts, history, allocation, isLoading, error } =
    storeToRefs(dashboardStore);

  onMounted(() => {
    void dashboardStore.fetchDashboardData();
  });

  const handleExportReport = () => {
    console.log('Exporting dashboard report...');
  };

  const handleAddPosition = () => {
    console.log('Opening add position modal...');
  };

  return {
    metrics,
    watchlist,
    alerts,
    history,
    allocation,
    isLoading,
    error,
    handleExportReport,
    handleAddPosition,
  };
}
