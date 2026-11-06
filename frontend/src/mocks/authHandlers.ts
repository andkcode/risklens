import { http, HttpResponse } from 'msw';
import type { RegisterData, LoginCredentials } from '../../../../../Desktop/RiskLense-local/RisksLens-app/src/types/auth';

// Key for browser localStorage to persist mock users between reloads
const MOCK_USERS_STORAGE_KEY = 'risk_lens_mock_db_users';

interface MockUser extends RegisterData {
  id: string;
}

// Initialize mock database with a default user, syncing with localStorage
const getInitialUsers = (): MockUser[] => {
  const stored = localStorage.getItem(MOCK_USERS_STORAGE_KEY);
  if (stored) {
    try {
      return JSON.parse(stored) as MockUser[];
    } catch {
      // Fallback if JSON is corrupted
    }
  }
  return [
    {
      id: '1',
      email: 'john@company.com',
      password: 'Password123!',
      fullName: 'John Doe',
    },
  ];
};

let mockUsers: MockUser[] = getInitialUsers();

// Helper to save users state
const saveMockUsers = (users: MockUser[]) => {
  mockUsers = users;
  localStorage.setItem(MOCK_USERS_STORAGE_KEY, JSON.stringify(users));
};

export const authHandlers = [
  // 1. Register handler (Adds user to mock database)
  http.post('/api/auth/register', async ({ request }) => {
    const body = (await request.json()) as RegisterData;

    // Check if user already exists
    const existingUser = mockUsers.find((u) => u.email === body.email);
    if (existingUser) {
      return HttpResponse.json(
        { message: 'User with this email address already exists.' },
        { status: 400 },
      );
    }

    // Create new user record
    const newUser: MockUser = {
      id: Date.now().toString(),
      email: body.email,
      password: body.password,
      fullName: body.fullName,
    };

    // Save to mock database
    saveMockUsers([...mockUsers, newUser]);

    // Return token and safe user object (excluding password)
    return HttpResponse.json({
      token: `mock-jwt-token-${newUser.id}`,
      user: {
        id: newUser.id,
        email: newUser.email,
        fullName: newUser.fullName,
      },
    });
  }),

  // 2. Login handler (Validates credentials against mock database)
  http.post('/api/auth/login', async ({ request }) => {
    const body = (await request.json()) as LoginCredentials;

    // Find user matching email and password
    const user = mockUsers.find((u) => u.email === body.email && u.password === body.password);

    if (!user) {
      return HttpResponse.json({ message: 'Invalid email or password.' }, { status: 401 });
    }

    return HttpResponse.json({
      token: `mock-jwt-token-${user.id}`,
      user: {
        id: user.id,
        email: user.email,
        fullName: user.fullName,
      },
    });
  }),

  // 3. Get current user handler
  http.get('/api/auth/me', ({ request }) => {
    const authHeader = request.headers.get('Authorization');
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      return HttpResponse.json({ message: 'Unauthorized' }, { status: 401 });
    }

    // For mock purposes, return the most recently active or first user
    const user = mockUsers[0];
    return HttpResponse.json({
      id: user.id,
      email: user.email,
      fullName: user.fullName,
    });
  }),
];
