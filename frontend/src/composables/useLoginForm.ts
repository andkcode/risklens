// frontend/src/composables/useLoginForm.ts
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import { useToastStore } from '../stores/toast';
import { parseProblemDetail } from '../utils/errorMapper';

export function useLoginForm() {
  const router = useRouter();
  const auth = useAuthStore();
  const toast = useToastStore();

  const email = ref('');
  const password = ref('');
  const showPassword = ref(false);
  const rememberMe = ref(false);

  const emailError = ref('');
  const passwordError = ref('');
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

  async function handleSubmit(): Promise<void> {
    if (!validate()) {
      toast.warning('Please correct the highlighted fields before continuing.', 'Check your details');
      return;
    }

    isLoading.value = true;
    try {
      // Pass both email and password to the authentication store
      await auth.login(email.value, password.value);
      toast.success('You are now signed in.', 'Welcome back');
      await router.push({ name: 'dashboard' });
    } catch (err: unknown) {
      // Parse backend ProblemDetail response
      const parsedError = parseProblemDetail(err);
      toast.error(parsedError.generalMessage, 'Sign-in failed');

      if (parsedError.fieldErrors.email) {
        emailError.value = parsedError.fieldErrors.email;
      }
      if (parsedError.fieldErrors.password) {
        passwordError.value = parsedError.fieldErrors.password;
      }
    } finally {
      isLoading.value = false;
    }
  }

  function register(): void {
    void router.push({ name: 'register' });
  }

  return {
    email,
    password,
    showPassword,
    rememberMe,
    emailError,
    passwordError,
    isLoading,
    handleSubmit,
    register,
  };
}