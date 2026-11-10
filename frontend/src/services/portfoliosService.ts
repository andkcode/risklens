import axios from 'axios';
import type { PortfolioSummary, PortfolioDetailData } from '../types/portfolio';

export interface PortfoliosListResponse {
  activeCount: number;
  combinedValue: string;
  combinedChange: string;
  items: PortfolioSummary[];
}

export const portfolioService = {
  // Get all portfolios list for sidebar
  async getPortfolios(): Promise<PortfoliosListResponse> {
    const { data } = await axios.get<PortfoliosListResponse>('/api/portfolios');
    return data;
  },

  // Get detailed information for a specific portfolio by string ID
  async getPortfolioById(id: string): Promise<PortfolioDetailData> {
    const { data } = await axios.get<PortfolioDetailData>(`/api/portfolios/${id}`);
    return data;
  },
};
