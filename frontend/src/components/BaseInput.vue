<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
defineProps<{
  modelValue: string | number | boolean;
  label?: string;
  error?: string;
  placeholder?: string;
  type?: string;
  mono?: boolean;
}>();

defineEmits(['update:modelValue']);
</script>

<template>
  <!-- 1. Checkbox variant -->
  <label
    v-if="type === 'checkbox'"
    class="relative flex items-center gap-2 text-[9px] text-brand-secondary cursor-pointer select-none"
  >
    <input
      type="checkbox"
      class="absolute w-px h-px opacity-0 peer"
      :checked="Boolean(modelValue)"
      @change="$emit('update:modelValue', ($event.target as HTMLInputElement).checked)"
    />
    <!-- Dynamic classes based on modelValue condition instead of peer-checked -->
    <span
      class="w-3.5 h-3.5 flex items-center justify-center rounded-[3px] transition-all border"
      :class="
        modelValue ? 'bg-brand-accent border-brand-accent' : 'bg-brand-surface border-brand-border'
      "
    >
      <svg v-if="modelValue" class="w-2 h-1.5 text-white" viewBox="0 0 6 3" fill="none">
        <path
          d="M0.5 1.5L2 3L5.5 0.5"
          stroke="currentColor"
          stroke-width="1.5"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </span>
    <span>{{ label }}</span>
  </label>

  <!-- 2. Standard text/number input variant -->
  <div v-else class="flex flex-col gap-1.5">
    <label v-if="label" class="text-[9px] font-medium text-[#aeb6c0] uppercase tracking-wider">
      {{ label }}
    </label>
    <div
      class="relative flex items-center h-10 px-3 bg-brand-surface border rounded-md transition-all"
      :class="[
        error ? 'border-market-danger' : 'border-brand-border focus-within:border-brand-accent',
      ]"
    >
      <slot name="prefix" />
      <input
        :type="type || 'text'"
        :value="modelValue"
        :placeholder="placeholder"
        class="w-full bg-transparent border-0 outline-0 text-[11px] text-white placeholder:text-brand-muted-2"
        :class="{ 'font-mono': mono }"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      />
      <slot name="suffix" />
    </div>
    <span v-if="error" class="text-[9px] text-market-danger">{{ error }}</span>
  </div>
</template>
