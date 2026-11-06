import { defineStore } from 'pinia';
import { ref } from 'vue';
import { dashboardService } from '../services/dashboardService';
import type {
  MetricCardData,
  WatchlistAsset,
  RiskAlert,
  HistoryLogItem,
  PortfolioAllocationData,
} from '../../../../../Desktop/RiskLense-local/RisksLens-app/src/types/dashboard';

export const useDashboardStore = defineStore('dashboard', () => {
  const metrics = ref<MetricCardData[]>([]);
  const watchlist = ref<{ stocks: WatchlistAsset[]; crypto: WatchlistAsset[] }>({
    stocks: [],
    crypto: [],
  });
  const alerts = ref<RiskAlert[]>([]);
  const history = ref<HistoryLogItem[]>([]);
  const allocation = ref<PortfolioAllocationData>({
    totalValue: '',
    items: [],
  });

  const isLoading = ref<boolean>(false);
  const error = ref<string | null>(null);

  /**
   * Fetch all dashboard data concurrently
   */
  async function fetchDashboardData() {
    isLoading.value = true;
    error.value = null;

    try {
      const [metricsData, watchlistData, alertsData, historyData, allocationData] =
        await Promise.all([
          dashboardService.getHeaderMetrics(),
          dashboardService.getWatchlistAssets(),
          dashboardService.getRiskAlerts(),
          dashboardService.getHistoryLogs(),
          dashboardService.getAllocationData(),
        ]);

      metrics.value = metricsData;
      watchlist.value = watchlistData;
      alerts.value = alertsData;
      history.value = historyData;
      allocation.value = allocationData;
    } catch (err: unknown) {
      const errorResponse = err as { response?: { data?: { message?: string } } };
      error.value = errorResponse.response?.data?.message || 'Failed to load dashboard data.';
    } finally {
      isLoading.value = false;
    }
  }

  return {
    metrics,
    watchlist,
    alerts,
    history,
    allocation,
    isLoading,
    error,
    fetchDashboardData,
  };
});
