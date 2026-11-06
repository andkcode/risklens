<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../stores/auth';
import Icon from '../../../../../WebstormProjects/risklens/frontend/src/components/icons/LayoutIcons.vue';
import BaseButton from '../components/BaseButton.vue';
import BaseInput from '../components/BaseInput.vue';
import BaseCard from '../components/BaseCard.vue';
const auth = useAuthStore();
const router = useRouter();

const isMobileMenuOpen = ref(false);
const searchQuery = ref('');

function toggleMobileMenu() {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
}

function closeMobileMenu() {
  isMobileMenuOpen.value = false;
}

function handleLogout() {
  auth.logout();
  void router.push({ name: 'login' });
}
</script>

<template>
  <div
    class="min-h-screen bg-[#09000c] text-[#ebedf0] font-sans antialiased selection:bg-[#4c8dff] selection:text-white"
  >
    <!-- TOP HEADER BAR -->
    <header
      class="fixed inset-x-0 top-0 z-50 flex h-16 items-center justify-between border-b border-[#222830] bg-[#0b0e12] px-4 lg:px-6"
    >
      <!-- Left: Logo & Mobile Trigger -->

      <div class="flex items-center gap-4">
        <BaseButton
          variant="ghost"
          size="icon"
          class="lg:hidden"
          aria-label="Toggle Menu"
          @click="toggleMobileMenu"
        >
          <svg
            viewBox="0 0 24 24"
            fill="none"
            class="size-5"
            stroke="currentColor"
            stroke-width="2"
          >
            <path d="M4 6h16M4 12h16M4 18h16" stroke-linecap="round" />
          </svg>
        </BaseButton>

        <RouterLink to="/dashboard" class="flex items-center gap-2.5 group">
          <div
            class="grid size-8 place-items-center rounded-lg bg-gradient-to-br from-[#4c8dff] to-[#2f70df] text-white shadow-md shadow-[#4c8dff]/20"
          >
            <Icon name="logo" :size="16" />
          </div>
          <span class="text-sm font-bold tracking-tight text-[#ebedf0]">Risk Lens</span>
        </RouterLink>
      </div>

      <!-- Center: Global Search Bar (BaseInput) -->
      <div class="hidden md:flex items-center flex-1 max-w-md mx-8">
        <BaseInput
          v-model="searchQuery"
          placeholder="Search ticker, asset, or metric..."
          class="w-full"
        >
          <template #prefix>
            <Icon name="search" :size="15" class="text-[#8994a2] mr-2 shrink-0" />
          </template>
        </BaseInput>
      </div>

      <!-- Right: Market Tickers & User Profile -->
      <div class="flex items-center gap-4">
        <!-- Notification Button -->
        <BaseButton variant="ghost" size="icon" class="relative" aria-label="Notifications">
          <span class="absolute top-2 right-2 size-2 bg-[#4c8dff] rounded-full"></span>
          <Icon name="bell" :size="16" />
        </BaseButton>

        <!-- User Profile & Logout -->
        <div class="flex items-center gap-3 pl-2 border-l border-[#222830]">
          <div class="flex items-center gap-2.5">
            <div
              class="grid size-8 place-items-center rounded-lg bg-[#171d24] border border-[#2b333d] text-[11px] font-bold text-[#ebedf0]"
            >
              {{ auth.user?.fullName ? auth.user.fullName.slice(0, 2).toUpperCase() : 'Us' }}
            </div>
            <div class="hidden sm:flex flex-col text-left">
              <span class="text-[11px] font-semibold text-[#ebedf0] leading-tight">
                {{ auth.user?.fullName || 'User' }}
              </span>
              <span class="text-[9px] font-semibold text-[#ebedf0]">{{
                auth.user?.email || 'no data'
              }}</span>
            </div>
          </div>

          <BaseButton variant="danger" size="sm" @click="handleLogout"> Log out </BaseButton>
        </div>
      </div>
    </header>

    <!-- Mobile Sidebar Backdrop -->
    <div
      v-if="isMobileMenuOpen"
      class="fixed inset-0 z-40 bg-black/60 backdrop-blur-xs lg:hidden"
      @click="closeMobileMenu"
    />

    <!-- LEFT SIDEBAR NAVIGATION -->
    <aside
      class="fixed bottom-0 top-16 z-40 w-64 border-r border-[#222830] bg-[#0b0e12] flex flex-col justify-between transition-transform duration-300 lg:translate-x-0"
      :class="isMobileMenuOpen ? 'left-0 translate-x-0' : '-left-64 -translate-x-full lg:left-0'"
    >
      <nav class="p-3 space-y-1 overflow-y-auto" @click="closeMobileMenu">
        <div
          class="px-3 pt-2 pb-2 text-[9px] font-semibold uppercase tracking-wider text-[#606b78]"
        >
          Workspace
        </div>

        <RouterLink
          to="/dashboard"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-[#8994a2] transition-all hover:bg-[#14191f] hover:text-[#ebedf0] [&.router-link-active]:bg-[#14191f] [&.router-link-active]:text-[#4c8dff] [&.router-link-active]:border [&.router-link-active]:border-[#222830]"
        >
          <Icon name="dashboard" :size="16" />
          <span>Dashboard</span>
        </RouterLink>

        <RouterLink
          to="/portfolios"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-[#8994a2] transition-all hover:bg-[#14191f] hover:text-[#ebedf0] [&.router-link-active]:bg-[#14191f] [&.router-link-active]:text-[#4c8dff] [&.router-link-active]:border [&.router-link-active]:border-[#222830]"
        >
          <Icon name="portfolio" :size="16" />
          <span>Portfolio</span>
        </RouterLink>

        <RouterLink
          to="/risk/demo"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-[#8994a2] transition-all hover:bg-[#14191f] hover:text-[#ebedf0] [&.router-link-active]:bg-[#14191f] [&.router-link-active]:text-[#4c8dff] [&.router-link-active]:border [&.router-link-active]:border-[#222830]"
        >
          <Icon name="risks" :size="16" />
          <span>Risks</span>
        </RouterLink>

        <RouterLink
          to="/reports"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-[#8994a2] transition-all hover:bg-[#14191f] hover:text-[#ebedf0] [&.router-link-active]:bg-[#14191f] [&.router-link-active]:text-[#4c8dff] [&.router-link-active]:border [&.router-link-active]:border-[#222830]"
        >
          <Icon name="reports" :size="16" />
          <span>Reports</span>
        </RouterLink>

        <RouterLink
          to="/news"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-[#8994a2] transition-all hover:bg-[#14191f] hover:text-[#ebedf0] [&.router-link-active]:bg-[#14191f] [&.router-link-active]:text-[#4c8dff] [&.router-link-active]:border [&.router-link-active]:border-[#222830]"
        >
          <Icon name="news" :size="16" />
          <span>News</span>
        </RouterLink>
      </nav>

      <!-- Sidebar Footer (BaseCard) -->
      <div class="p-3 border-t border-[#222830] space-y-3 bg-[#0b0e12]">
        <RouterLink
          to="/settings"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-[#8994a2] transition-all hover:bg-[#14191f] hover:text-[#ebedf0]"
        >
          <Icon name="settings" :size="16" />
          <span>Account Settings</span>
        </RouterLink>

        <!-- Real-time markets card (BaseCard) -->
        <BaseCard variant="standard" class="p-3">
          <div class="flex items-center justify-between mb-1">
            <span class="text-[9px] font-semibold text-[#8994a2] uppercase tracking-wider"
              >Data Refresh</span
            >
            <span class="inline-flex items-center gap-1.5 text-[9px] font-semibold text-[#43d6a0]">
              <span class="size-1.5 rounded-full bg-[#43d6a0] animate-pulse"></span>
              Demo mode
            </span>
          </div>
          <div class="text-xs font-semibold text-[#ebedf0] mb-0.5">
            Data and authentication are currently mocked.
          </div>
        </BaseCard>
      </div>
    </aside>

    <!-- MAIN WORKSPACE CONTAINER -->
    <main class="min-h-screen px-4 pb-12 pt-20 sm:px-6 lg:ml-64 lg:px-8 lg:pt-20">
      <div class="mx-auto max-w-7xl">
        <RouterView />
      </div>
    </main>
  </div>
</template>
