import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

export function useLoginForm() {
  const router = useRouter();
  const authStore = useAuthStore();

  const email = ref('');
  const password = ref('');
  const showPassword = ref(false);
  const rememberMe = ref(false);

  const emailError = ref('');
  const passwordError = ref('');
  const generalError = ref('');
  const isLoading = ref(false);

  /**
   * Handle login form submission
   */
  const handleSubmit = async () => {
    emailError.value = '';
    passwordError.value = '';
    generalError.value = '';

    if (!email.value) {
      emailError.value = 'Email address or username is required';
      return;
    }
    if (!password.value) {
      passwordError.value = 'Password is required';
      return;
    }

    isLoading.value = true;
    try {
      await authStore.login({
        email: email.value,
        password: password.value,
      });
      // Redirect to main dashboard after successful authentication
      router.push('/');
    } catch (err: unknown) {
      generalError.value = err.response?.data?.message || 'Invalid email or password.';
    } finally {
      isLoading.value = false;
    }
  };

  /**
   * Navigate to registration view
   */
  const register = () => {
    router.push('/register');
  };

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
