<script setup lang="ts">
defineProps<{
  name: 'eye' | 'eye-off' | 'lock' | 'security' | 'user' | 'mail' | 'logo';
  size?: number;
}>();
</script>

<template>
  <!-- Icon Eye -->
  <svg
    v-if="name === 'eye'"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.7"
    stroke-linecap="round"
    stroke-linejoin="round"
    :width="size || 16"
    :height="size || 16"
  >
    <path d="M2.5 12s3.5-6 9.5-6 9.5 6 9.5 6-3.5 6-9.5 6-9.5-6-9.5-6Z" />
    <circle cx="12" cy="12" r="2.5" />
  </svg>

  <!--  Eye-Off -->
  <svg
    v-else-if="name === 'eye-off'"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.7"
    stroke-linecap="round"
    stroke-linejoin="round"
    :width="size || 16"
    :height="size || 16"
  >
    <path
      d="M10.733 5.076a10.444 10.444 0 0 1 1.267-.076c7 0 10 7 10 7a13.16 13.16 0 0 1-1.67 2.68"
    />
    <path d="M6.61 6.61A13.526 13.526 0 0 0 2 12s3 7 10 7a9.74 9.74 0 0 0 5.39-1.61" />
    <line x1="2" y1="2" x2="22" y2="22" />
    <path d="M9.88 9.88a3 3 0 1 0 4.24 4.24" />
  </svg>

  <!-- Icon Lock -->
  <svg
    v-else-if="name === 'lock'"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.7"
    stroke-linecap="round"
    stroke-linejoin="round"
    :width="size || 18"
    :height="size || 18"
  >
    <rect x="5" y="10" width="14" height="11" rx="2" />
    <path d="M8 10V7a4 4 0 0 1 8 0v3M12 14v3" />
  </svg>

  <!--Icon security -->
  <svg
    v-else-if="name === 'security'"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.7"
    stroke-linecap="round"
    stroke-linejoin="round"
    :width="size || 16"
    :height="size || 16"
  >
    <path d="M12 3 5 6v5c0 4.8 2.9 8.2 7 10 4.1-1.8 7-5.2 7-10V6Z" />
    <path d="m9 12 2 2 4-4" />
  </svg>

  <!-- icon User -->
  <svg
    v-else-if="name === 'user'"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.7"
    stroke-linecap="round"
    stroke-linejoin="round"
    :width="size || 18"
    :height="size || 18"
  >
    <circle cx="12" cy="8" r="4" />
    <path d="M4 21a8 8 0 0 1 16 0" />
  </svg>

  <!--  Mail -->
  <svg
    v-else-if="name === 'mail'"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    stroke-width="1.7"
    stroke-linecap="round"
    stroke-linejoin="round"
    :width="size || 18"
    :height="size || 18"
  >
    <rect width="20" height="16" x="2" y="4" rx="2" />
    <path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7" />
  </svg>

  <!-- logo RiskLens -->
  <div v-else-if="name === 'logo'" class="flex items-center gap-2.5">
    <span class="grid size-9 place-items-center rounded-xl bg-brand-accent text-white shrink-0">
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
    <span class="text-lg font-bold tracking-tight text-white">RiskLens</span>
  </div>
</template>
