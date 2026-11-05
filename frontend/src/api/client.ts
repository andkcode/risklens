import axios from 'axios';
import router from '../router';
import { useAuthStore } from '../stores/auth';

export const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('access_token');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.status === 401) {
      const authStore = useAuthStore();

      // Сбрасываем сессию и удаляем токен
      authStore.logout();

      // Мягкий редирект на логин (если пользователь еще не там)
      if (router.currentRoute.value.name !== 'login') {
        await router.push({ name: 'login' });
      }
    }

    return Promise.reject(error);
  },
);
