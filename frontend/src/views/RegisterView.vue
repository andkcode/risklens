<script setup lang="ts">
import { useRegisterForm } from '../composables/useRegisterForm';
import BaseButton from '../components/BaseButton.vue';
import BaseInput from '../components/BaseInput.vue';
import BaseCard from '../components/BaseCard.vue';
import PasswordRequirements from '../components/PasswordRequirements.vue';
import IconEye from '../components/icons/IconEye.vue';
import IconEyeOff from '../components/icons/IconEyeOff.vue';
import MainLogo from '../components/icons/MainLogo.vue';
import IconSecurity from '../components/icons/IconSecurity.vue';
import IconUser from '../components/icons/IconUser.vue';

const {
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
  isLoading,
  hasMinLength,
  hasUpperCase,
  hasNumber,
  hasSpecialChar,
  handleRegister,
  goToLogin,
} = useRegisterForm();
</script>

<template>
  <div class="flex min-h-screen flex-col bg-brand-bg text-brand-primary">
    <!-- 1. TOP HEADER -->
    <header
      class="flex h-16 items-center justify-between border-b border-brand-border bg-brand-card px-4 sm:px-6 lg:px-8"
    >
      <div class="flex items-center gap-3">
        <MainLogo class="size-8" />
      </div>

      <div class="flex items-center gap-2 text-xs font-medium text-brand-secondary">
        <IconSecurity class="size-4 text-emerald-500 shrink-0" />
        <span>256-bit encrypted session</span>
      </div>
    </header>

    <!-- 2. MAIN CONTENT -->
    <main class="relative flex flex-1 items-center justify-center px-4 py-12 sm:px-6 lg:px-8">
      <BaseCard variant="auth" class="w-full max-w-md">
        <!-- Шапка карточки -->
        <div class="mb-6 text-center">
          <div
            class="mx-auto mb-3 grid size-12 place-items-center rounded-xl bg-brand-surface text-brand-accent border border-brand-border"
          >
            <IconUser class="size-6" />
          </div>
          <span class="text-xs font-semibold uppercase tracking-wider text-brand-accent">
            Portfolio intelligence
          </span>
          <h1 class="mt-1 text-2xl font-bold tracking-tight text-brand-primary">
            Create your account
          </h1>
          <p class="mt-2 text-sm text-brand-secondary">
            Start monitoring portfolio exposure and downside risk with institutional-grade
            analytics.
          </p>
        </div>

        <!-- Форма регистрации -->
        <form class="space-y-4" novalidate @submit.prevent="handleRegister">
          <!-- Полное имя -->
          <div class="space-y-1">
            <BaseInput v-model="fullName" label="Full name" type="text" placeholder="John Doe" />
            <p v-if="fullNameError" class="text-xs text-rose-500 font-medium">
              {{ fullNameError }}
            </p>
          </div>

          <!-- Email -->
          <div class="space-y-1">
            <BaseInput
              v-model="email"
              label="Email address"
              type="email"
              placeholder="name@company.com"
            />
            <p v-if="emailError" class="text-xs text-rose-500 font-medium">{{ emailError }}</p>
          </div>

          <!-- Пароль -->
          <div class="space-y-1">
            <!-- Обертка для отслеживания фокуса в поле ввода пароля -->
            <div @focusin="isPasswordFocused = true" @focusout="isPasswordFocused = false">
              <BaseInput
                v-model="password"
                label="Password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="Create a password"
              >
                <template #suffix>
                  <button
                    type="button"
                    class="text-brand-secondary hover:text-brand-primary transition-colors"
                    :aria-label="showPassword ? 'Hide password' : 'Show password'"
                    @click="showPassword = !showPassword"
                  >
                    <IconEyeOff v-if="showPassword" class="size-4" />
                    <IconEye v-else class="size-4" />
                  </button>
                </template>
              </BaseInput>
            </div>
            <p v-if="passwordError" class="text-xs text-rose-500 font-medium">
              {{ passwordError }}
            </p>
          </div>

          <!-- Индикатор требований к паролю (появляется при фокусе или наборе) -->
          <PasswordRequirements
            v-if="isPasswordFocused || password.length > 0"
            :has-min-length="hasMinLength"
            :has-upper-case="hasUpperCase"
            :has-number="hasNumber"
            :has-special-char="hasSpecialChar"
          />

          <!-- Подтверждение пароля -->
          <div class="space-y-1">
            <BaseInput
              v-model="confirmPassword"
              label="Confirm password"
              :type="showConfirmPassword ? 'text' : 'password'"
              placeholder="Confirm your password"
            >
              <template #suffix>
                <button
                  type="button"
                  class="text-brand-secondary hover:text-brand-primary transition-colors"
                  :aria-label="showConfirmPassword ? 'Hide password' : 'Show password'"
                  @click="showConfirmPassword = !showConfirmPassword"
                >
                  <IconEyeOff v-if="showConfirmPassword" class="size-4" />
                  <IconEye v-else class="size-4" />
                </button>
              </template>
            </BaseInput>
            <p v-if="confirmPasswordError" class="text-xs text-rose-500 font-medium">
              {{ confirmPasswordError }}
            </p>
          </div>

          <!-- Чекбоксы соглашений -->
          <div class="space-y-2 pt-1">
            <BaseInput
              v-model="agreeTerms"
              label="I agree to the Terms of Service"
              type="checkbox"
            />
            <BaseInput
              v-model="agreePrivacy"
              label="I acknowledge the Privacy Policy"
              type="checkbox"
            />
            <p v-if="termsError" class="text-xs text-rose-500 font-medium">{{ termsError }}</p>
          </div>

          <!-- Кнопка отправки формы -->
          <BaseButton
            variant="primary"
            class="w-full mt-2"
            size="md"
            type="submit"
            :disabled="isLoading"
          >
            <span>{{ isLoading ? 'Creating account...' : 'Create account' }}</span>
          </BaseButton>
        </form>

        <!-- Переход на экран входа -->
        <div class="mt-6 text-center text-sm text-brand-secondary">
          <span>Already have an account?</span>
          <button
            type="button"
            class="ml-1 font-semibold text-brand-accent hover:underline"
            @click="goToLogin"
          >
            Login here
          </button>
        </div>
      </BaseCard>
    </main>

    <!-- 3. FOOTER -->
    <footer class="py-4 text-center text-xs text-brand-secondary">
      <span>SOC 2 Type II · FINRA-grade security · Privacy-first analytics</span>
    </footer>
  </div>
</template>
