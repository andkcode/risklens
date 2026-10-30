<script setup lang="ts">
defineProps<{
  columns: { key: string; label: string; align?: 'left' | 'right' | 'center' }[]
  data: Record<string, any>[]
}>()
</script>

<template>
  <div class="w-full overflow-x-auto border border-brand-border rounded-xl bg-white shadow-card">
    <table class="w-full text-left border-collapse">
      <thead>
      <tr class="border-b border-brand-border bg-brand-surface text-xs font-semibold text-brand-secondary uppercase tracking-wider">
        <th v-for="col in columns" :key="col.key" class="px-4 py-3" :class="[col.align === 'right' ? 'text-right' : col.align === 'center' ? 'text-center' : 'text-left']">
          {{ col.label }}
        </th>
      </tr>
      </thead>
      <tbody class="divide-y divide-brand-border text-sm text-brand-primary">
      <tr v-for="(row, index) in data" :key="index" class="hover:bg-brand-surface/60 transition-colors">
        <td v-for="col in columns" :key="col.key" class="px-4 py-3.5" :class="[col.align === 'right' ? 'text-right' : col.align === 'center' ? 'text-center' : 'text-left']">
          <slot :name="col.key" :row="row">
            {{ row[col.key] }}
          </slot>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>