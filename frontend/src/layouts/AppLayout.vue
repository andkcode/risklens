<script setup lang="ts">
import { useAppLayout } from '../composables/useAppLayout';
import Icon from '../../../../../WebstormProjects/risklens/frontend/src/components/icons/LayoutIcons.vue';
import BaseButton from '../components/BaseButton.vue';
import BaseInput from '../components/BaseInput.vue';
import BaseCard from '../components/BaseCard.vue';

const {
  auth,
  isFullWidth,
  isMobileMenuOpen,
  searchQuery,
  toggleMobileMenu,
  closeMobileMenu,
  handleLogout,
} = useAppLayout();
</script>

<template>
  <div
    class="bg-brand-bg text-brand-primary font-sans antialiased selection:bg-brand-accent selection:text-white"
    :class="isFullWidth ? 'h-screen overflow-hidden' : 'min-h-screen'"
  >
    <!-- TOP HEADER BAR -->
    <header
      class="fixed inset-x-0 top-0 z-50 flex h-16 items-center justify-between border-b border-brand-border bg-brand-sidebar px-4 lg:px-6"
    >
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
            class="grid size-8 place-items-center rounded-lg bg-linear-to-br from-brand-accent to-[#2f70df] text-white shadow-md shadow-brand-accent/20"
          >
            <Icon name="logo" :size="16" />
          </div>
          <span class="text-sm font-bold tracking-tight text-brand-primary">Risk Lens</span>
        </RouterLink>
      </div>

      <div class="hidden md:flex items-center flex-1 max-w-md mx-8">
        <BaseInput
          v-model="searchQuery"
          placeholder="Search ticker, asset, or metric..."
          class="w-full"
        >
          <template #prefix>
            <Icon name="search" :size="15" class="text-brand-secondary mr-2 shrink-0" />
          </template>
        </BaseInput>
      </div>

      <div class="flex items-center gap-4">
        <BaseButton variant="ghost" size="icon" class="relative" aria-label="Notifications">
          <span class="absolute top-2 right-2 size-2 bg-brand-accent rounded-full"></span>
          <Icon name="bell" :size="16" />
        </BaseButton>

        <div class="flex items-center gap-3 pl-2 border-l border-brand-border">
          <div class="flex items-center gap-2.5">
            <div
              class="grid size-8 place-items-center rounded-lg bg-brand-surface-hover border border-brand-border-light text-[11px] font-bold text-brand-primary"
            >
              {{ auth.user?.fullName ? auth.user.fullName.slice(0, 2).toUpperCase() : 'Us' }}
            </div>
            <div class="hidden sm:flex flex-col text-left">
              <span class="text-[11px] font-semibold text-brand-primary leading-tight">
                {{ auth.user?.fullName || 'User' }}
              </span>
              <span class="text-[9px] font-semibold text-brand-primary">{{
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
      class="fixed bottom-0 top-16 z-40 w-64 border-r border-brand-border bg-brand-sidebar flex flex-col justify-between transition-transform duration-300 lg:translate-x-0"
      :class="isMobileMenuOpen ? 'left-0 translate-x-0' : '-left-64 -translate-x-full lg:left-0'"
    >
      <nav class="p-3 space-y-1 overflow-y-auto" @click="closeMobileMenu">
        <div
          class="px-3 pt-2 pb-2 text-[9px] font-semibold uppercase tracking-wider text-brand-muted-2"
        >
          Workspace
        </div>

        <RouterLink
          to="/dashboard"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-brand-secondary transition-all hover:bg-brand-surface-raised hover:text-brand-primary"
          active-class="bg-brand-surface-raised text-brand-accent border border-brand-border"
        >
          <Icon name="dashboard" :size="16" />
          <span>Dashboard</span>
        </RouterLink>

        <RouterLink
          to="/portfolios"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-brand-secondary transition-all hover:bg-brand-surface-raised hover:text-brand-primary"
          active-class="bg-brand-surface-raised text-brand-accent border border-brand-border"
        >
          <Icon name="portfolio" :size="16" />
          <span>Portfolio</span>
        </RouterLink>

        <RouterLink
          to="/risk/demo"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-brand-secondary transition-all hover:bg-brand-surface-raised hover:text-brand-primary"
          active-class="bg-brand-surface-raised text-brand-accent border border-brand-border"
        >
          <Icon name="risks" :size="16" />
          <span>Risks</span>
        </RouterLink>

        <RouterLink
          to="/reports"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-brand-secondary transition-all hover:bg-brand-surface-raised hover:text-brand-primary"
          active-class="bg-brand-surface-raised text-brand-accent border border-brand-border"
        >
          <Icon name="reports" :size="16" />
          <span>Reports</span>
        </RouterLink>

        <RouterLink
          to="/news"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-brand-secondary transition-all hover:bg-brand-surface-raised hover:text-brand-primary"
          active-class="bg-brand-surface-raised text-brand-accent border border-brand-border"
        >
          <Icon name="news" :size="16" />
          <span>News</span>
        </RouterLink>
      </nav>

      <div class="p-3 border-t border-brand-border space-y-3 bg-brand-sidebar">
        <RouterLink
          to="/settings"
          class="flex items-center gap-3 px-3 py-2.5 rounded-lg text-xs font-medium text-brand-secondary transition-all hover:bg-brand-surface-raised hover:text-brand-primary"
        >
          <Icon name="settings" :size="16" />
          <span>Account Settings</span>
        </RouterLink>

        <BaseCard variant="standard" class="p-3">
          <div class="flex items-center justify-between mb-1">
            <span class="text-[9px] font-semibold text-brand-secondary uppercase tracking-wider"
              >Data Refresh</span
            >
            <span
              class="inline-flex items-center gap-1.5 text-[9px] font-semibold text-market-success"
            >
              <span class="size-1.5 rounded-full bg-market-success animate-pulse"></span>
              Demo mode
            </span>
          </div>
          <div class="text-xs font-semibold text-brand-primary mb-0.5">
            Data and authentication are currently mocked.
          </div>
        </BaseCard>
      </div>
    </aside>

    <!-- MAIN WORKSPACE CONTAINER -->
    <main
      class="transition-all"
      :class="
        isFullWidth
          ? 'fixed inset-x-0 top-16 bottom-0 overflow-hidden p-0 lg:left-64'
          : 'relative min-h-screen px-4 pb-12 pt-20 sm:px-6 lg:ml-64 lg:px-8 lg:pt-20'
      "
    >
      <div :class="isFullWidth ? 'h-full w-full' : 'mx-auto max-w-7xl'">
        <RouterView />
      </div>
    </main>
  </div>
</template>
