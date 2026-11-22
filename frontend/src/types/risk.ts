export type RiskLevel = 'High' | 'Medium' | 'Low';
export type ImpactType = 'danger' | 'warning' | 'success';

export interface HeatmapStat {
  level: RiskLevel;
  percentage: number;
  count: number;
}


export interface RiskMetricsData {
  overallScore: number;
  overallLevel: RiskLevel;
  scoreChange: number;
  varAmount: string;
  varPercentage: string;
  stressCrashPercent: string;
  stressAmount: string;
  stressDrawdown: string;
  stressRecoveryMonths: string;
  heatmapAssetsCount: number;
  heatmapStats: HeatmapStat[];
}


export interface RiskTimelineItem {
  id: string;
  name: string;
  category: string;
  ticker: string;
  badgeLetter: string;
  liquidityScore: number;
  volatility: string;
  scheduleLabel: string;
  scheduleProgressPercent: number;
  riskLevel: RiskLevel;
}

export interface RiskInsightItem {
  id: string;
  number: string;
  title: string;
  impact: string;
  impactType: ImpactType;
  description: string;
}

export interface RiskInsightsData {
  primaryTitle: string;
  primaryDescription: string;
  items: RiskInsightItem[];
  recommendedActions: string[];
}


export interface RiskChatMessage {
  id: string;
  sender: 'ai' | 'user';
  text: string;
}


export type ChatMessage = RiskChatMessage;
