export interface User {
  id: string;
  email: string;
  fullName: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface Position {
  id: string;
  symbol: string;
  name: string;
  volume: number;
  entryPrice: number;
  currentPrice: number;
  pnl: number;
  riskScore: 'low' | 'medium' | 'high';
}

export interface Portfolio {
  id: string;
  name: string;
  totalValue: number;
  dailyChangePercent: number;
  var95: number; // Value at Risk 95%
  positions: Position[];
}

export interface RiskAnalysis {
  portfolioId: string;
  stressTestScenario: string;
  projectedLoss: number;
  volatility: number;
}
