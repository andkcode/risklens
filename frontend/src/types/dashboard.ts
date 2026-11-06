export interface MetricCardData {
  title: string;
  value: string;
  change?: string;
  isPositive?: boolean;
  subtitle: string;
  timeframe?: string;
  riskLevel?: 'Low' | 'Medium' | 'High';
  chartType?: 'line' | 'bars';
  chartData?: number[];
}

export interface AllocationItem {
  label: string;
  percentage: number;
  amount: string;
  color: string;
}

export interface PortfolioAllocationData {
  totalValue: string;
  items: AllocationItem[];
}

export interface WatchlistAsset {
  id: string;
  name: string;
  price: string;
  riskScore: number;
  riskLevel: 'High' | 'Medium' | 'Low';
  change24h: string;
}

export interface RiskAlert {
  id: string;
  title: string;
  description: string;
  timeAgo: string;
  severity: 'high' | 'medium' | 'low';
}

export interface HistoryLogItem {
  id: string;
  date: string;
  utcTime: string;
  type: string;
  assetsAnalysed: string;
  primaryRiskFactor: string;
  riskRating: 'High' | 'Medium' | 'Low';
  score: number;
  actionStatus: 'Pending' | 'Applied' | 'In review' | 'Archived';
}
