import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

export function useLoginForm() {
  const router = useRouter();
  const auth = useAuthStore();

  const email = ref('');
  const password = ref('');
  const showPassword = ref(false);
  const rememberMe = ref(false);

  const emailError = ref('');
  const passwordError = ref('');
  const generalError = ref('');
  const isLoading = ref(false);

  function validate(): boolean {
    let isValid = true;
    if (!email.value.trim()) {
      emailError.value = 'Email address is required';
      isValid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
      emailError.value = 'Please enter a valid email address';
      isValid = false;
    } else {
      emailError.value = '';
    }

    if (!password.value) {
      passwordError.value = 'Password is required';
      isValid = false;
    } else if (password.value.length < 8) {
      passwordError.value = 'Password must be at least 8 characters';
      isValid = false;
    } else {
      passwordError.value = '';
    }

    return isValid;
  }

  async function handleSubmit() {
    generalError.value = '';
    if (!validate()) return;

    isLoading.value = true;
    try {
      // Передаем и email, и password в стор авторизации
      await auth.login(email.value, password.value);
      await router.push({ name: 'dashboard' });
    } catch {
      generalError.value = 'Invalid credentials or network error.';
    } finally {
      isLoading.value = false;
    }
  }

  function register() {
    void router.push({ name: 'register' });
  }

  return {
    email,
    password,
    showPassword,
    rememberMe,
    emailError,
    passwordError,
    generalError,
    isLoading,
    handleSubmit,
    register,
  };
}
