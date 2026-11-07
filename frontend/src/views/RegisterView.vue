<script setup lang="ts">
import { useRegisterForm } from '../composables/useRegisterForm';
import BaseButton from '../components/BaseButton.vue';
import BaseInput from '../components/BaseInput.vue';
import BaseCard from '../components/BaseCard.vue';
import PasswordRequirements from '../components/PasswordRequirements.vue';
import AuthIcon from '../../../../../WebstormProjects/risklens/frontend/src/components/icons/AuthIcons.vue';

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
  generalError,
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
        <AuthIcon name="logo" />
      </div>

      <div class="flex items-center gap-2 text-xs font-medium text-brand-secondary">
        <AuthIcon name="security" class="text-emerald-500 shrink-0" />
        <span>256-bit encrypted session</span>
      </div>
    </header>

    <!-- 2. MAIN CONTENT -->
    <main class="relative flex flex-1 items-center justify-center px-4 py-12 sm:px-6 lg:px-8">
      <BaseCard variant="auth" class="w-full max-w-md">
        <!-- Card header -->
        <div class="mb-6 text-center">
          <div
            class="mx-auto mb-3 grid size-12 place-items-center rounded-xl bg-brand-surface text-brand-accent border border-brand-border"
          >
            <AuthIcon name="user" :size="24" />
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

        <!-- General error -->
        <div
          v-if="generalError"
          class="mb-4 rounded-lg bg-rose-500/10 border border-rose-500/20 p-3 text-xs text-rose-500 text-center font-medium"
        >
          {{ generalError }}
        </div>

        <!-- Registration form -->
        <form class="space-y-4" novalidate @submit.prevent="handleRegister">
          <!-- Full name field -->
          <div class="space-y-1">
            <BaseInput v-model="fullName" label="Full name" type="text" placeholder="John Doe">
              <template #prefix>
                <AuthIcon name="user" :size="16" class="text-brand-secondary mr-2.5 shrink-0" />
              </template>
            </BaseInput>
            <p v-if="fullNameError" class="text-xs text-rose-500 font-medium">
              {{ fullNameError }}
            </p>
          </div>

          <!-- Email field -->
          <div class="space-y-1">
            <BaseInput
              v-model="email"
              label="Email address"
              type="email"
              placeholder="name@company.com"
            >
              <template #prefix>
                <AuthIcon name="mail" :size="16" class="text-brand-secondary mr-2.5 shrink-0" />
              </template>
            </BaseInput>
            <p v-if="emailError" class="text-xs text-rose-500 font-medium">{{ emailError }}</p>
          </div>

          <!-- Password field -->
          <div class="space-y-1">
            <!-- Wrapper to track password field focus -->
            <div @focusin="isPasswordFocused = true" @focusout="isPasswordFocused = false">
              <BaseInput
                v-model="password"
                label="Password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="Minimum 8 characters"
              >
                <template #prefix>
                  <AuthIcon name="lock" :size="16" class="text-brand-secondary mr-2.5 shrink-0" />
                </template>
                <template #suffix>
                  <button
                    type="button"
                    class="text-brand-secondary hover:text-brand-primary transition-colors"
                    :aria-label="showPassword ? 'Hide password' : 'Show password'"
                    @click="showPassword = !showPassword"
                  >
                    <AuthIcon :name="showPassword ? 'eye-off' : 'eye'" :size="16" />
                  </button>
                </template>
              </BaseInput>
            </div>
            <p v-if="passwordError" class="text-xs text-rose-500 font-medium">
              {{ passwordError }}
            </p>
          </div>

          <!-- Password requirements indicator (appears on focus or input) -->
          <PasswordRequirements
            v-if="isPasswordFocused || password.length > 0"
            :has-min-length="hasMinLength"
            :has-upper-case="hasUpperCase"
            :has-number="hasNumber"
            :has-special-char="hasSpecialChar"
          />

          <!-- Confirm password field -->
          <div class="space-y-1">
            <BaseInput
              v-model="confirmPassword"
              label="Confirm password"
              :type="showConfirmPassword ? 'text' : 'password'"
              placeholder="Re-enter your password"
            >
              <template #prefix>
                <AuthIcon name="lock" :size="16" class="text-brand-secondary mr-2.5 shrink-0" />
              </template>
              <template #suffix>
                <button
                  type="button"
                  class="text-brand-secondary hover:text-brand-primary transition-colors"
                  :aria-label="showConfirmPassword ? 'Hide password' : 'Show password'"
                  @click="showConfirmPassword = !showConfirmPassword"
                >
                  <AuthIcon :name="showConfirmPassword ? 'eye-off' : 'eye'" :size="16" />
                </button>
              </template>
            </BaseInput>
            <p v-if="confirmPasswordError" class="text-xs text-rose-500 font-medium">
              {{ confirmPasswordError }}
            </p>
          </div>

          <!-- Terms agreements checkboxes -->
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

          <!-- Submit button -->
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

        <!-- Navigate to login screen -->
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
