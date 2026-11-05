import { http, HttpResponse } from 'msw';

export const handlers = [
  // Эндпоинт входа
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

  // Эндпоинт регистрации
  http.post('/api/v1/auth/register', () => {
    return HttpResponse.json(
      {
        accessToken: 'mock-jwt-token-risklens-15min',
        refreshToken: 'mock-refresh-token-risklens',
      },
      { status: 201 },
    );
  }),
];
