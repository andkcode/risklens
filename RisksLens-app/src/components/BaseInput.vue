<script setup lang="ts">
defineProps<{
  modelValue: string | number;
  label?: string;
  error?: string;
  placeholder?: string;
  type?: string;
}>();

defineEmits(['update:modelValue']);
</script>

<template>
  <div class="flex flex-col gap-1.5">
    <label v-if="label" class="text-xs font-semibold text-brand-secondary uppercase tracking-wider">
      {{ label }}
    </label>
    <div class="relative">
      <input
        :type="type || 'text'"
        :value="modelValue"
        :placeholder="placeholder"
        class="w-full bg-white border border-brand-border rounded-lg px-3.5 py-2 text-sm text-brand-primary placeholder:text-slate-400 focus:outline-none focus:border-brand-accent focus:ring-1 focus:ring-brand-accent transition-all"
        :class="{
          'border-market-danger focus:border-market-danger focus:ring-market-danger': error,
        }"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      />
    </div>
    <span v-if="error" class="text-xs text-market-danger">{{ error }}</span>
  </div>
</template>
