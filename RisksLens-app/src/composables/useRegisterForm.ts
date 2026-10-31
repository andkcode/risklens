import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';

export function useRegisterForm() {
  const router = useRouter();
  const auth = useAuthStore();

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

  const hasMinLength = computed(() => password.value.length >= 8);
  const hasUpperCase = computed(() => /[A-Z]/.test(password.value));
  const hasNumber = computed(() => /[0-9]/.test(password.value));
  const hasSpecialChar = computed(() =>
    /[!@#$%^&*(),.?":{}|<>\-_+=[\]{};:'",.<>/?\\|`~]/.test(password.value),
  );

  function validate(): boolean {
    let isValid = true;

    if (!fullName.value.trim()) {
      fullNameError.value = 'Full name is required';
      isValid = false;
    } else {
      fullNameError.value = '';
    }

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
    } else if (
      !hasMinLength.value ||
      !hasUpperCase.value ||
      !hasNumber.value ||
      !hasSpecialChar.value
    ) {
      passwordError.value = 'Password must meet all security requirements';
      isValid = false;
    } else {
      passwordError.value = '';
    }

    if (!confirmPassword.value) {
      confirmPasswordError.value = 'Please confirm your password';
      isValid = false;
    } else if (confirmPassword.value !== password.value) {
      confirmPasswordError.value = 'Passwords do not match';
      isValid = false;
    } else {
      confirmPasswordError.value = '';
    }

    if (!agreeTerms.value || !agreePrivacy.value) {
      termsError.value = 'You must agree to the Terms and Privacy Policy';
      isValid = false;
    } else {
      termsError.value = '';
    }

    return isValid;
  }

  async function handleRegister() {
    generalError.value = '';
    if (!validate()) return;

    isLoading.value = true;
    try {
      // Добавлен await
      await auth.register(fullName.value, email.value, password.value);
      await router.push({ name: 'dashboard' });
    } catch {
      generalError.value = 'Registration failed. Please try again.';
    } finally {
      isLoading.value = false;
    }
  }

  function goToLogin() {
    void router.push({ name: 'login' });
  }

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
