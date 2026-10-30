<script setup lang="ts">
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '../stores/auth';

const auth = useAuthStore();
const router = useRouter();
const route = useRoute();

// State for mobile sidebar toggle (< 1024px)
const isMobileMenuOpen = ref(false);

function toggleMobileMenu() {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
}

function closeMobileMenu() {
  isMobileMenuOpen.value = false;
}

function logout() {
  auth.logout();
  void router.push({ name: 'login' });
}
</script>

<template>
  <div class="min-h-screen bg-slate-50 text-slate-900">
    <!-- Top Header -->
    <header
      class="fixed inset-x-0 top-0 z-50 flex h-16 items-center justify-between border-b border-slate-200 bg-white px-4 sm:px-6 lg:px-8"
    >
      <div class="flex items-center gap-3">
        <!-- Mobile Menu Trigger (< 1024px) -->
        <button
          type="button"
          class="rounded-lg p-2 text-slate-600 transition hover:bg-slate-100 hover:text-slate-900 lg:hidden"
          aria-label="Open menu"
          @click="toggleMobileMenu"
        >
          <svg viewBox="0 0 24 24" fill="none" class="size-6" aria-hidden="true">
            <path
              d="M4 6h16M4 12h16M4 18h16"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
            />
          </svg>
        </button>

        <!-- Brand Logo -->
        <RouterLink to="/dashboard" class="flex items-center gap-3" aria-label="RiskLense — Home">
          <span class="grid size-9 place-items-center rounded-xl bg-brand-primary text-white">
            <svg viewBox="0 0 24 24" fill="none" class="size-5" aria-hidden="true">
              <path
                d="M4 17.5 9 12l3.5 3.5L20 7"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path d="M15 7h5v5" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
            </svg>
          </span>
          <span class="text-lg font-bold tracking-tight text-brand-primary">RiskLense</span>
        </RouterLink>
      </div>

      <!-- Right Header Actions -->
      <div class="flex items-center gap-4">
        <span
          class="hidden rounded-full bg-emerald-50 px-3 py-1 text-xs font-semibold text-emerald-700 sm:inline-flex"
        >
          Analysis Active
        </span>
        <span class="hidden text-sm text-slate-600 sm:inline">{{ auth.user?.email }}</span>
        <button
          type="button"
          class="rounded-lg px-3 py-2 text-sm font-medium text-slate-600 transition hover:bg-slate-100 hover:text-slate-900 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-brand-accent"
          @click="logout"
        >
          Log out
        </button>
      </div>
    </header>

    <!-- Mobile Sidebar Backdrop -->
    <div
      v-if="isMobileMenuOpen"
      class="fixed inset-0 z-40 bg-slate-900/20 backdrop-blur-xs lg:hidden"
      @click="closeMobileMenu"
    />

    <!-- Sidebar Navigation (Adaptive from 1024px / lg) -->
    <aside
      class="fixed bottom-0 top-16 z-40 w-64 border-r border-slate-200 bg-white transition-transform duration-300 lg:translate-x-0"
      :class="isMobileMenuOpen ? 'left-0 translate-x-0' : '-left-64 -translate-x-full lg:left-0'"
      aria-label="Main navigation"
    >
      <nav class="space-y-1 p-4" @click="closeMobileMenu">
        <RouterLink
          to="/dashboard"
          class="flex items-center gap-3 rounded-lg px-4 py-3 text-sm font-medium text-slate-600 transition hover:bg-slate-100 hover:text-brand-primary [&.router-link-active]:bg-blue-50 [&.router-link-active]:text-brand-accent"
        >
          Overview
        </RouterLink>
        <RouterLink
          to="/portfolios"
          class="flex items-center gap-3 rounded-lg px-4 py-3 text-sm font-medium text-slate-600 transition hover:bg-slate-100 hover:text-brand-primary [&.router-link-active]:bg-blue-50 [&.router-link-active]:text-brand-accent"
        >
          Portfolios
        </RouterLink>
        <div class="px-4 pb-2 pt-8 text-xs font-semibold uppercase tracking-wider text-slate-400">
          Analytics
        </div>
        <RouterLink
          to="/risk/demo"
          class="flex items-center gap-3 rounded-lg px-4 py-3 text-sm font-medium text-slate-600 transition hover:bg-slate-100 hover:text-brand-primary [&.router-link-active]:bg-blue-50 [&.router-link-active]:text-brand-accent"
        >
          AI Risk Analysis
        </RouterLink>
      </nav>

      <!-- Sidebar Footer Card -->
      <div class="absolute inset-x-4 bottom-4 rounded-xl border border-slate-200 bg-slate-50 p-4">
        <p class="text-xs font-semibold text-slate-700">Demo Mode</p>
        <p class="mt-1 text-xs leading-5 text-slate-500">
          Data and authentication are currently mocked.
        </p>
      </div>
    </aside>

    <!-- Main Workspace Area -->
    <main class="min-h-screen px-4 pb-8 pt-24 sm:px-6 lg:ml-64 lg:px-8 lg:pt-20">
      <div class="mx-auto max-w-7xl">
        <RouterView />
      </div>
    </main>
  </div>
</template>
