<script setup lang="ts">
defineProps<{
  name: 'export' | 'analysis' | 'search' | 'stock' | 'crypto' | 'plus';
  class?: string;
}>();
</script>

<template>
  <svg
    class = "shrink-0 w-4 h-4"
    fill="none"
    viewBox="0 0 24 24"
    stroke="currentColor"
    stroke-width="2"
  >
    <!-- Export Icon -->
    <path
      v-if="name === 'export'"
      stroke-linecap="round"
      stroke-linejoin="round"
      d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"
    />

    <!-- Run Analysis Icon -->
    <template v-else-if="name === 'analysis'">
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        d="M14.752 11.168l-3.197-2.132A1 1 0 0010 9.87v4.263a1 1 0 001.555.832l3.197-2.132a1 1 0 000-1.664z"
      />
      <path stroke-linecap="round" stroke-linejoin="round" d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
    </template>

    <!-- Search Icon -->
    <path
      v-else-if="name === 'search'"
      stroke-linecap="round"
      stroke-linejoin="round"
      d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
    />

    <!-- Stock / Equity Icon -->
    <path
      v-else-if="name === 'stock'"
      stroke-linecap="round"
      stroke-linejoin="round"
      d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z"
    />

    <!-- Cryptocurrency Icon -->
    <path
      v-else-if="name === 'crypto'"
      stroke-linecap="round"
      stroke-linejoin="round"
      d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9"
    />

    <!-- Plus / Add Icon -->
    <path
      v-else-if="name === 'plus'"
      stroke-linecap="round"
      stroke-linejoin="round"
      stroke-width="2.5"
      d="M12 4v16m8-8H4"
    />
  </svg>
</template>
