/**
 * Represents an authenticated user profile
 */
export interface User {
  id: string;
  email: string;
  fullName: string;
}

/**
 * Credentials required for user login
 */
export interface LoginCredentials {
  email: string;
  password: string;
}

/**
 * Data required for new user registration
 */
export interface RegisterData {
  fullName: string;
  email: string;
  password: string;
}

/**
 * Server response structure upon successful login or registration
 */
export interface AuthResponse {
  token: string;
  user: User;
}
