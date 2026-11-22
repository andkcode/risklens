import { http, HttpResponse } from 'msw';
import type {
  RiskMetricsData,
  RiskTimelineItem,
  RiskInsightsData,
  RiskChatMessage,
} from '../types/risk';

export const riskHandlers = [
  http.get('/api/risks/metrics', () => {
    const data: RiskMetricsData = {
      overallScore: 20,
      overallLevel: 'Low',
      scoreChange: 4,
      varAmount: '-$14,250',
      varPercentage: '-3.8%',
      stressCrashPercent: '-20%',
      stressAmount: '-$92,840',
      stressDrawdown: '24.9%',
      stressRecoveryMonths: '8–14',
      heatmapAssetsCount: 9,
      heatmapStats: [
        { level: 'High', percentage: 42, count: 5 },
        { level: 'Medium', percentage: 33, count: 1 },
        { level: 'Low', percentage: 25, count: 3 },
      ],
    };
    return HttpResponse.json(data);
  }),

  http.get('/api/risks/timeline', () => {
    const data: RiskTimelineItem[] = [
      {
        id: '1',
        name: 'Bitcoin',
        category: 'Crypto',
        ticker: 'BTC',
        badgeLetter: 'B',
        liquidityScore: 92,
        volatility: '68.4%',
        scheduleLabel: 'Liquid · No lockup',
        scheduleProgressPercent: 100,
        riskLevel: 'High',
      },
      {
        id: '2',
        name: 'NVIDIA',
        category: 'Stock',
        ticker: 'NVDA',
        badgeLetter: 'N',
        liquidityScore: 96,
        volatility: '52.1%',
        scheduleLabel: 'Liquid · T+1',
        scheduleProgressPercent: 85,
        riskLevel: 'High',
      },
      {
        id: '3',
        name: 'Ethereum',
        category: 'Crypto',
        ticker: 'ETH',
        badgeLetter: 'E',
        liquidityScore: 88,
        volatility: '61.7%',
        scheduleLabel: '12.4 ETH unlock · 48d',
        scheduleProgressPercent: 70,
        riskLevel: 'High',
      },
      {
        id: '4',
        name: 'Tesla',
        category: 'Stock',
        ticker: 'TSLA',
        badgeLetter: 'T',
        liquidityScore: 94,
        volatility: '46.8%',
        scheduleLabel: 'Liquid · T+1',
        scheduleProgressPercent: 60,
        riskLevel: 'Medium',
      },
      {
        id: '5',
        name: 'Apple',
        category: 'Stock',
        ticker: 'AAPL',
        badgeLetter: 'A',
        liquidityScore: 98,
        volatility: '24.2%',
        scheduleLabel: 'Liquid · T+1',
        scheduleProgressPercent: 40,
        riskLevel: 'Low',
      },
    ];
    return HttpResponse.json(data);
  }),

  http.get('/api/risks/insights', () => {
    const data: RiskInsightsData = {
      primaryTitle: 'Why is your Value at Risk high?',
      primaryDescription:
        'Your portfolio has a 42% risk concentration in highly volatile crypto assets. Bitcoin and Ethereum contribute $8,420—or 59%—of total daily VaR. During stressed markets, their correlation with NVDA rises from 0.34 to 0.81, reducing the diversification you expect.',
      items: [
        {
          id: '01',
          number: '01',
          title: 'Concentration exposure',
          impact: 'High impact',
          impactType: 'danger',
          description:
            'Three positions create 68% of downside risk despite representing 51% of market value.',
        },
        {
          id: '02',
          number: '02',
          title: 'Stress correlation',
          impact: 'High impact',
          impactType: 'warning',
          description:
            'Crypto and growth equities increasingly move together during high-volatility periods.',
        },
        {
          id: '03',
          number: '03',
          title: 'Liquidity resilience',
          impact: 'Protective',
          impactType: 'success',
          description:
            'Core positions remain highly liquid, supporting rapid execution if risk limits trigger.',
        },
      ],
      recommendedActions: ['Rebalance recommended', 'High correlation alert', 'Set VaR limit'],
    };
    return HttpResponse.json(data);
  }),

  http.get('/api/risks/chat', () => {
    const data: RiskChatMessage[] = [
      {
        id: '1',
        sender: 'ai',
        text: 'Your current risk score is 78. I can explain any metric or model a lower-risk allocation.',
      },
      {
        id: '2',
        sender: 'user',
        text: 'Why is my portfolio VaR high?',
      },
      {
        id: '3',
        sender: 'ai',
        text: 'Crypto represents 42% of your risk contribution. BTC and ETH also show a 0.81 correlation during market stress.',
      },
    ];
    return HttpResponse.json(data);
  }),
];
