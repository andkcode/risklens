import { http, HttpResponse } from 'msw';

export const dashboardHandlers = [
  // 1. Top header metrics with integrated chart data
  http.get('/api/dashboard/metrics', () => {
    return HttpResponse.json([
      {
        title: 'Overall portfolio value',
        value: '$1.24M',
        change: '+8.24%',
        isPositive: true,
        subtitle: '+$94,820 this month',
        timeframe: '1M',
        chartType: 'line',
        chartData: [30, 40, 35, 50, 49, 60, 70, 65, 85, 10],
      },
      {
        title: 'Total portfolio risk score',
        value: '50 /100',
        riskLevel: 'Medium',
        subtitle: '+6 points vs 30D avg.',
      },
      {
        title: 'Max drawdown',
        value: '-14.5%',
        change: '+3.2%',
        isPositive: false,
        subtitle: 'Peak: Feb 14, 2025',
        timeframe: 'YTD',
        chartType: 'line',
        chartData: [30, 40, 35, 50, 49, 30, 70, 65, 85, 10],
      },
      {
        title: 'Annualized volatility',
        value: '21.8%',
        change: '2.4%',
        subtitle: 'Benchmark: 18.2%',
        timeframe: '90D',
        chartType: 'bars',
        chartData: [20, 35, 25, 45, 60, 40, 55, 75, 65, 80],
      },
    ]);
  }),

  // 2. Watchlist assets (Stocks & Crypto)
  http.get('/api/dashboard/watchlist', () => {
    return HttpResponse.json({
      stocks: [
        {
          id: '1',
          name: 'NVDA',
          price: '$308,482',
          riskScore: 82,
          riskLevel: 'High',
          change24h: '+4.28%',
        },
        {
          id: '2',
          name: 'AAPL',
          price: '$214,136',
          riskScore: 31,
          riskLevel: 'Low',
          change24h: '-0.74%',
        },
        {
          id: '3',
          name: 'MSFT',
          price: '$163,062',
          riskScore: 28,
          riskLevel: 'Low',
          change24h: '+0.38%',
        },
      ],
      crypto: [
        {
          id: '4',
          name: 'BTC',
          price: '$253,765',
          riskScore: 78,
          riskLevel: 'High',
          change24h: '+1.92%',
        },
        {
          id: '5',
          name: 'ETH',
          price: '$181,720',
          riskScore: 64,
          riskLevel: 'Medium',
          change24h: '+2.16%',
        },
        {
          id: '6',
          name: 'SOL',
          price: '$123,185',
          riskScore: 86,
          riskLevel: 'High',
          change24h: '-3.21%',
        },
      ],
    });
  }),

  // 3. Risk alerts feed
  http.get('/api/dashboard/alerts', () => {
    return HttpResponse.json([
      {
        id: '1',
        title: 'SOL volatility spike',
        description: '30D volatility exceeded 86%',
        timeAgo: '8 min ago',
        severity: 'high',
      },
      {
        id: '2',
        title: 'NVDA concentration',
        description: 'Position exceeds 20% threshold',
        timeAgo: '42 min ago',
        severity: 'high',
      },
      {
        id: '3',
        title: 'BTC correlation shift',
        description: 'Correlation with equities at 0.74',
        timeAgo: '2 hours ago',
        severity: 'medium',
      },
      {
        id: '4',
        title: 'VaR model updated',
        description: 'Confidence interval recalculated',
        timeAgo: '6 hours ago',
        severity: 'low',
      },
    ]);
  }),

  // 4. Audit history logs
  http.get('/api/dashboard/history', () => {
    return HttpResponse.json([
      {
        id: '1',
        date: 'May 24, 2025',
        utcTime: '09:42 UTC',
        type: 'Portfolio-wide',
        assetsAnalysed: 'Entire Portfolio',
        primaryRiskFactor: 'Volatility & concentration',
        riskRating: 'High',
        score: 74,
        actionRecommended: 'Reduce equity exposure',
        actionStatus: 'Pending',
      },
      {
        id: '2',
        date: 'May 23, 2025',
        utcTime: '16:18 UTC',
        type: 'Crypto specific',
        assetsAnalysed: 'BTC, ETH, SOL',
        primaryRiskFactor: 'Cross-asset correlation',
        riskRating: 'Medium',
        score: 68,
        actionRecommended: 'Hedge cross-asset risk',
        actionStatus: 'Applied',
      },
      {
        id: '3',
        date: 'May 22, 2025',
        utcTime: '11:05 UTC',
        type: 'Stock specific',
        assetsAnalysed: 'NVDA',
        primaryRiskFactor: 'Market cap concentration',
        riskRating: 'High',
        score: 82,
        actionRecommended: 'Trim position size',
        actionStatus: 'In review',
      },
      {
        id: '4',
        date: 'May 20, 2025',
        utcTime: '14:30 UTC',
        type: 'Portfolio-wide',
        assetsAnalysed: 'Entire Portfolio',
        primaryRiskFactor: 'Value at Risk (95%)',
        riskRating: 'Medium',
        score: 64,
        actionRecommended: 'Adjust VaR threshold',
        actionStatus: 'Applied',
      },
      {
        id: '5',
        date: 'May 18, 2025',
        utcTime: '08:12 UTC',
        type: 'Stock specific',
        assetsAnalysed: 'AAPL, MSFT',
        primaryRiskFactor: 'Sector correlation',
        riskRating: 'Low',
        score: 31,
        actionRecommended: 'Maintain current allocation',
        actionStatus: 'Archived',
      },
    ]);
  }),
  // 5. Portfolio allocation by asset class
  http.get('/api/dashboard/allocation', () => {
    return HttpResponse.json({
      totalValue: '$1.24M',
      items: [
        { label: 'Stocks', percentage: 55.1, amount: '$683,324', color: '#4c8dff' },
        { label: 'Crypto', percentage: 34.7, amount: '$430,280', color: '#a78bfa' },
        { label: 'ETFs', percentage: 6.4, amount: '$79,360', color: '#43d6a0' },
        { label: 'Cash', percentage: 3.8, amount: '$47,036', color: '#606b78' },
      ],
    });
  }),
];
