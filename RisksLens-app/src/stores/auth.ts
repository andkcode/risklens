import { computed, ref } from 'vue';
import { defineStore } from 'pinia';

const TOKEN_KEY = 'access_token';

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem(TOKEN_KEY));
  const user = ref<{ email: string } | null>(null);
  const isAuthenticated = computed(() => Boolean(token.value));

  function setToken(newToken: string) {
    token.value = newToken;
    localStorage.setItem(TOKEN_KEY, newToken);
  }

  function logout() {
    token.value = null;
    user.value = null;
    localStorage.removeItem(TOKEN_KEY);
  }

  async function login(email: string): Promise<void> {
    setToken('mock-jwt-token-15min');
    user.value = { email };
  }

  return { token, user, isAuthenticated, setToken, login, logout };
});
