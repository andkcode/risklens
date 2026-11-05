import { http, HttpResponse } from 'msw';

const mockPortfolio = {
  id: '00000000-0000-4000-8000-000000000001',
  name: 'Demo Portfolio',
  baseCurrency: 'USD',
  createdAt: '2025-01-01T00:00:00Z',
  updatedAt: '2025-01-01T00:00:00Z',
};

export const handlers = [
  http.post('/api/v1/auth/login', async ({ request }) => {
    const body = (await request.json()) as { email?: string; password?: string };

    if (!body.email || !body.password) {
      return HttpResponse.json({ message: 'Invalid credentials' }, { status: 400 });
    }

    return HttpResponse.json({
      accessToken: 'mock-jwt-token-risklens-15min',
      refreshToken: 'mock-refresh-token-risklens',
    });
  }),

  http.post('/api/v1/auth/register', () => {
    return HttpResponse.json(
      {
        accessToken: 'mock-jwt-token-risklens-15min',
        refreshToken: 'mock-refresh-token-risklens',
      },
      { status: 201 },
    );
  }),

  http.get('/api/v1/portfolios', () =>
    HttpResponse.json({
      content: [mockPortfolio],
      page: { size: 10, number: 0, totalElements: 1, totalPages: 1 },
    }),
  ),

  http.get('/api/v1/portfolios/:portfolioId/risk-assessments', ({ params }) =>
    HttpResponse.json({
      content: [
        {
          id: '00000000-0000-4000-8000-000000000002',
          portfolioId: params.portfolioId,
          calculatedAt: '2025-01-01T00:00:00Z',
          volatility: 0.185,
          var95: 0.042,
          maxDrawdown: 0.125,
          sharpeRatio: 1.45,
          riskScore: 49,
        },
      ],
      page: { size: 1, number: 0, totalElements: 1, totalPages: 1 },
    }),
  ),
];
