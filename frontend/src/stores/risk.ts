import { defineStore } from 'pinia';
import { ref } from 'vue';
import { riskService } from '../services/riskService';
import type {
  RiskHeaderData,
  RiskMetricData,
  RiskAssetItem,
  RiskInsightsData,
  ChatMessage,
} from '../types/risk';

export const useRiskStore = defineStore('risk', () => {
  const header = ref<RiskHeaderData>({
    breadcrumb: '',
    title: '',
    subtitle: '',
  });

  const metrics = ref<RiskMetricData>({
    overallScore: 0,
    overallLevel: '',
    scoreChange: '',
    varAmount: '',
    varPercentage: '',
    varSubtitle: '',
    stressCrashPercent: '',
    stressAmount: '',
    stressDrawdown: '',
    stressRecovery: '',
    heatmapAssetsCount: 0,
    heatmapStats: [],
  });

  const timelineAssets = ref<RiskAssetItem[]>([]);

  const insights = ref<RiskInsightsData>({
    primaryTitle: '',
    primaryDescription: '',
    items: [],
    recommendedActions: [],
  });

  const chatMessages = ref<ChatMessage[]>([]);

  const isLoading = ref<boolean>(false);
  const error = ref<string | null>(null);

  /**
   * Fetch all risk analytics data concurrently
   */
  async function fetchRiskData() {
    isLoading.value = true;
    error.value = null;

    try {
      const [headerData, metricsData, timelineData, insightsData, chatData] = await Promise.all([
        riskService.getRiskHeader(),
        riskService.getRiskMetrics(),
        riskService.getRiskTimelineAssets(),
        riskService.getRiskInsights(),
        riskService.getChatMessages(),
      ]);

      header.value = headerData;
      metrics.value = metricsData;
      timelineAssets.value = timelineData;
      insights.value = insightsData;
      chatMessages.value = chatData;
    } catch (err: unknown) {
      const errorResponse = err as { response?: { data?: { message?: string } } };
      error.value = errorResponse.response?.data?.message || 'Failed to load risk analytics data.';
    } finally {
      isLoading.value = false;
    }
  }

  return {
    header,
    metrics,
    timelineAssets,
    insights,
    chatMessages,
    isLoading,
    error,
    fetchRiskData,
  };
});
