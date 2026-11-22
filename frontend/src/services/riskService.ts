import axios from 'axios';
import type {
  RiskHeaderData,
  RiskMetricData,
  RiskAssetItem,
  RiskInsightsData,
  ChatMessage,
} from '../types/risk';

export const riskService = {
  async getRiskHeader(): Promise<RiskHeaderData> {
    const { data } = await axios.get<RiskHeaderData>('/api/risks/header');
    return data;
  },

  async getRiskMetrics(): Promise<RiskMetricData> {
    const { data } = await axios.get<RiskMetricData>('/api/risks/metrics');
    return data;
  },

  async getRiskTimelineAssets(): Promise<RiskAssetItem[]> {
    const { data } = await axios.get<RiskAssetItem[]>('/api/risks/timeline');
    return data;
  },

  async getRiskInsights(): Promise<RiskInsightsData> {
    const { data } = await axios.get<RiskInsightsData>('/api/risks/insights');
    return data;
  },

  async getChatMessages(): Promise<ChatMessage[]> {
    const { data } = await axios.get<ChatMessage[]>('/api/risks/chat');
    return data;
  },
};
