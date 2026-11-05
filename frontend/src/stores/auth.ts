import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { axiosInstance as apiClient } from '../api/client';

// 1. Describe the user interface
export interface User {
  id: number | string;
  fullName: string;
  email: string;
  role: string;
}

interface AuthResponse {
  accessToken: string;
  refreshToken: string;
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('access_token'));

  // 2. Use the strict interface instead of 'any'
  const user = ref<User | null>(null);

  const isAuthenticated = computed(() => !!token.value);

  async function login(email: string, password?: string): Promise<void> {
    try {
      const response = await apiClient.post<AuthResponse>('/api/v1/auth/login', { email, password });
      token.value = response.data.accessToken;
      user.value = null;

      if (token.value) {
        localStorage.setItem('access_token', token.value);
        localStorage.setItem('refresh_token', response.data.refreshToken);
      }
    } catch (error) {
      console.error('Login action error:', error);
      throw error;
    }
  }

  async function register(fullName: string, email: string, password: string): Promise<void> {
    try {
      const response = await apiClient.post<AuthResponse>('/api/v1/auth/register', { fullName, email, password });
      token.value = response.data.accessToken;
      user.value = null;

      if (token.value) {
        localStorage.setItem('access_token', token.value);
        localStorage.setItem('refresh_token', response.data.refreshToken);
      }
    } catch (error) {
      console.error('Register action error:', error);
      throw error;
    }
  }

  function logout(): void {
    token.value = null;
    user.value = null;
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }

  return {
    token,
    user,
    isAuthenticated,
    login,
    register,
    logout,
  };
});