<script setup lang="ts">
</script>

<template>
  <div class="overflow-x-auto border border-brand-border rounded-[9px] bg-brand-surface">
    <table class="w-full min-w-230 border-collapse text-left">
      <thead>
        <tr
          class="h-9.25 bg-brand-sidebar border-b border-brand-border text-brand-muted-2 text-[8px] font-semibold uppercase tracking-wider"
        >
          <slot name="header" />
        </tr>
      </thead>
      <tbody class="divide-y divide-brand-border">
        <slot name="body" />
      </tbody>
    </table>
  </div>
</template>
