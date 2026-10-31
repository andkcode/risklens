import { http, HttpResponse } from 'msw';

export const handlers = [
  // Эндпоинт входа
  http.post('/api/auth/login', async ({ request }) => {
    const body = (await request.json()) as { email?: string; password?: string };

    if (!body.email || !body.password) {
      return HttpResponse.json({ message: 'Invalid credentials' }, { status: 400 });
    }

    return HttpResponse.json({
      token: 'mock-jwt-token-risklens-15min',
      user: {
        id: 'usr_1',
        email: body.email,
        fullName: 'RiskLense Analyst',
      },
    });
  }),

  // Эндпоинт регистрации
  http.post('/api/auth/register', async ({ request }) => {
    const body = (await request.json()) as { email?: string; fullName?: string };

    return HttpResponse.json(
      {
        token: 'mock-jwt-token-risklens-15min',
        user: {
          id: 'usr_2',
          email: body.email || 'new@risklens.com',
          fullName: body.fullName || 'New User',
        },
      },
      { status: 201 },
    );
  }),
];
