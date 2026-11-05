<script setup lang="ts">
defineProps<{
  hasMinLength: boolean;
  hasUpperCase: boolean;
  hasNumber: boolean;
  hasSpecialChar: boolean;
}>();
</script>

<template>
  <div class="rounded-lg bg-brand-surface border border-brand-border p-3 space-y-1.5 text-xs">
    <p class="font-medium text-brand-secondary">Password requirements:</p>
    <ul class="space-y-1">
      <li
        class="flex items-center gap-2"
        :class="hasMinLength ? 'text-emerald-500' : 'text-brand-secondary'"
      >
        <span
          class="size-1.5 rounded-full"
          :class="hasMinLength ? 'bg-emerald-500' : 'bg-brand-border'"
        ></span>
        <span>At least 8 characters</span>
      </li>
      <li
        class="flex items-center gap-2"
        :class="hasUpperCase ? 'text-emerald-500' : 'text-brand-secondary'"
      >
        <span
          class="size-1.5 rounded-full"
          :class="hasUpperCase ? 'bg-emerald-500' : 'bg-brand-border'"
        ></span>
        <span>At least one uppercase letter</span>
      </li>
      <li
        class="flex items-center gap-2"
        :class="hasNumber ? 'text-emerald-500' : 'text-brand-secondary'"
      >
        <span
          class="size-1.5 rounded-full"
          :class="hasNumber ? 'bg-emerald-500' : 'bg-brand-border'"
        ></span>
        <span>At least one number</span>
      </li>
      <li
        class="flex items-center gap-2"
        :class="hasSpecialChar ? 'text-emerald-500' : 'text-brand-secondary'"
      >
        <span
          class="size-1.5 rounded-full"
          :class="hasSpecialChar ? 'bg-emerald-500' : 'bg-brand-border'"
        ></span>
        <span>At least one special character (!@#$%^&*)</span>
      </li>
    </ul>
  </div>
</template>
