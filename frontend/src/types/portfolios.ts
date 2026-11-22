export interface PortfolioSummary {
  id: string; // Unified as string to match route params
  code: string;
  name: string;
  assetsCount: number;
  totalValue: string;
  riskScore: number;
  riskLevel: 'Low' | 'Medium' | 'High';
}

export interface PortfolioPosition {
  id: string;
  name: string;
  status: string;
  ticker: string;
  quantity: number;
  avgPurchasePrice: string;
  currentMarketValue: string;
  allocationPercent: number;
  riskRating: 'Low' | 'Medium' | 'High';
  profitLoss: string;
  isPositive: boolean;
}

export interface PortfolioDetailData {
  id: string;
  code: string;
  name: string;
  description: string;
  totalValue: string;
  valueChangeToday: string;
  performanceYtd: string;
  performanceComparison: string;
  riskScore: number;
  riskLevel: 'Low' | 'Medium' | 'High';
  lastAnalyzed: string;
  costBasis: string;
  unrealizedProfit: string;
  positions: PortfolioPosition[];
}
