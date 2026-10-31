<script setup lang="ts">
import { useLoginForm } from '../composables/useLoginForm';
import BaseButton from '../components/BaseButton.vue';
import BaseInput from '../components/BaseInput.vue';
import BaseCard from '../components/BaseCard.vue';
import IconLock from '../components/icons/IconLock.vue';
import IconEye from '../components/icons/IconEye.vue';
import IconEyeOff from '../components/icons/IconEyeOff.vue';
import MainLogo from '../components/icons/MainLogo.vue';
import IconSecurity from '../components/icons/IconSecurity.vue';

const {
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
} = useLoginForm();
</script>

<template>
  <div class="flex min-h-screen flex-col bg-brand-bg text-brand-primary">
    <!-- 1. TOP HEADER -->
    <header
      class="flex h-16 items-center justify-between border-b border-brand-border bg-brand-card px-4 sm:px-6 lg:px-8"
    >
      <div class="flex items-center gap-3">
        <MainLogo class="size-6" />
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
            <IconLock class="size-6" />
          </div>
          <span class="text-xs font-semibold uppercase tracking-wider text-brand-accent">
            Secure client access
          </span>
          <h1 class="mt-1 text-2xl font-bold tracking-tight text-brand-primary">Welcome back</h1>
          <p class="mt-2 text-sm text-brand-secondary">
            Sign in to monitor exposure, stress-test positions, and manage portfolio risk.
          </p>
        </div>

        <!-- Общая ошибка авторизации -->
        <div
          v-if="generalError"
          class="mb-4 rounded-lg bg-rose-500/10 border border-rose-500/20 p-3 text-xs text-rose-500 text-center font-medium"
        >
          {{ generalError }}
        </div>

        <!-- Форма входа -->
        <form class="space-y-4" novalidate @submit.prevent="handleSubmit">
          <!-- Поле Email -->
          <div class="space-y-1">
            <BaseInput
              v-model="email"
              label="Email address or username"
              type="text"
              placeholder="name@company.com"
            />
            <p v-if="emailError" class="text-xs text-rose-500 font-medium">{{ emailError }}</p>
          </div>

          <!-- Поле Пароля -->
          <div class="space-y-1">
            <BaseInput
              v-model="password"
              label="Password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="Enter your password"
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
            <p v-if="passwordError" class="text-xs text-rose-500 font-medium">
              {{ passwordError }}
            </p>
          </div>

          <div class="flex items-center justify-between text-sm">
            <BaseInput v-model="rememberMe" label="Remember me" type="checkbox" />
            <button type="button" class="font-medium text-brand-accent hover:underline">
              Forgot password?
            </button>
          </div>

          <BaseButton
            variant="primary"
            class="w-full mt-2"
            size="md"
            type="submit"
            :disabled="isLoading"
          >
            <span>{{ isLoading ? 'Authenticating...' : 'Sign in securely' }}</span>
          </BaseButton>
        </form>

        <div class="mt-6 text-center text-sm text-brand-secondary">
          <span>Don't have an account?</span>
          <button
            type="button"
            class="ml-1 font-semibold text-brand-accent hover:underline"
            @click="register"
          >
            Register now
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
