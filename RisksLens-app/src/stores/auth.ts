import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { apiClient } from '../api/client';

// 1. Описываем интерфейс пользователя
export interface User {
  id: number | string;
  fullName: string;
  email: string;
  role: string;
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('access_token'));

  // 2. Используем интерфейс вместо any
  const user = ref<User | null>(null);

  const isAuthenticated = computed(() => !!token.value);

  async function login(email: string, password?: string) {
    try {
      const response = await apiClient.post('/auth/login', { email, password });
      token.value = response.data.token;
      user.value = response.data.user;

      if (token.value) {
        localStorage.setItem('access_token', token.value);
      }
    } catch (error) {
      console.error('Login action error:', error);
      throw error;
    }
  }

  async function register(fullName: string, email: string, password: string) {
    try {
      const response = await apiClient.post('/auth/register', { fullName, email, password });
      token.value = response.data.token;
      user.value = response.data.user;

      if (token.value) {
        localStorage.setItem('access_token', token.value);
      }
    } catch (error) {
      console.error('Register action error:', error);
      throw error;
    }
  }

  function logout() {
    token.value = null;
    user.value = null;
    localStorage.removeItem('access_token');
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
