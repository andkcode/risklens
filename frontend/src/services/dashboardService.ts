import axios from 'axios';
import type {
  MetricCardData,
  WatchlistAsset,
  RiskAlert,
  HistoryLogItem,
  PortfolioAllocationData,
} from '../types/dashboard';

export const dashboardService = {
  async getHeaderMetrics(): Promise<MetricCardData[]> {
    const { data } = await axios.get('/api/dashboard/metrics');
    return data;
  },

  async getWatchlistAssets(): Promise<{ stocks: WatchlistAsset[]; crypto: WatchlistAsset[] }> {
    const { data } = await axios.get('/api/dashboard/watchlist');
    return data;
  },

  async getRiskAlerts(): Promise<RiskAlert[]> {
    const { data } = await axios.get('/api/dashboard/alerts');
    return data;
  },

  async getHistoryLogs(): Promise<HistoryLogItem[]> {
    const { data } = await axios.get('/api/dashboard/history');
    return data;
  },

  async getAllocationData(): Promise<PortfolioAllocationData> {
    const { data } = await axios.get('/api/dashboard/allocation');
    return data;
  },
};
