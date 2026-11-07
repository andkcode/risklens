import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

export function useRegisterForm() {
  const router = useRouter();
  const authStore = useAuthStore();

  const fullName = ref('');
  const email = ref('');
  const password = ref('');
  const confirmPassword = ref('');
  const showPassword = ref(false);
  const showConfirmPassword = ref(false);
  const agreeTerms = ref(false);
  const agreePrivacy = ref(false);
  const isPasswordFocused = ref(false);

  const fullNameError = ref('');
  const emailError = ref('');
  const passwordError = ref('');
  const confirmPasswordError = ref('');
  const termsError = ref('');
  const generalError = ref('');
  const isLoading = ref(false);

  // Password strength validation computed properties
  const hasMinLength = computed(() => password.value.length >= 8);
  const hasUpperCase = computed(() => /[A-Z]/.test(password.value));
  const hasNumber = computed(() => /\d/.test(password.value));
  const hasSpecialChar = computed(() => /[@$!%*?&]/.test(password.value));

  /**
   * Handle registration form submission
   */
  const handleRegister = async () => {
    fullNameError.value = '';
    emailError.value = '';
    passwordError.value = '';
    confirmPasswordError.value = '';
    termsError.value = '';
    generalError.value = '';

    if (!fullName.value) {
      fullNameError.value = 'Full name is required';
      return;
    }
    if (!email.value) {
      emailError.value = 'Email address is required';
      return;
    }
    if (!hasMinLength.value || !hasUpperCase.value || !hasNumber.value) {
      passwordError.value = 'Password does not meet security requirements';
      return;
    }
    if (password.value !== confirmPassword.value) {
      confirmPasswordError.value = 'Passwords do not match';
      return;
    }
    if (!agreeTerms.value || !agreePrivacy.value) {
      termsError.value = 'You must agree to the terms and privacy policy';
      return;
    }

    isLoading.value = true;
    try {
      // Register user via store -> authService -> MSW mock database
      await authStore.register({
        fullName: fullName.value,
        email: email.value,
        password: password.value,
      });

      // Redirect to login view so the user can sign in with their newly created credentials
      router.push('/login');
    } catch (err: unknown) {
      generalError.value = err.response?.data?.message || 'Registration failed. Try another email.';
    } finally {
      isLoading.value = false;
    }
  };

  /**
   * Navigate back to login view
   */
  const goToLogin = () => {
    router.push('/login');
  };

  return {
    fullName,
    email,
    password,
    confirmPassword,
    showPassword,
    showConfirmPassword,
    agreeTerms,
    agreePrivacy,
    isPasswordFocused,
    fullNameError,
    emailError,
    passwordError,
    confirmPasswordError,
    termsError,
    generalError,
    isLoading,
    hasMinLength,
    hasUpperCase,
    hasNumber,
    hasSpecialChar,
    handleRegister,
    goToLogin,
  };
}
