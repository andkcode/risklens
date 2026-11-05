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
  <!-- 1. Вариант для чекбокса -->
  <label
    v-if="type === 'checkbox'"
    class="relative flex items-center gap-2 text-[9px] text-[#8994a2] cursor-pointer select-none"
  >
    <input
      type="checkbox"
      class="absolute w-px h-px opacity-0 peer"
      :checked="Boolean(modelValue)"
      @change="$emit('update:modelValue', ($event.target as HTMLInputElement).checked)"
    />
    <!-- Кастомный квадратик чекбокса в стиле вайрфрейма -->
    <span
      class="w-3.5 h-3.5 flex items-center justify-center bg-[#0c1014] border border-[#343d48] rounded-[3px] peer-checked:bg-[#3278ee] peer-checked:border-[#4c8dff] peer-focus-visible:ring-3 peer-focus-visible:ring-[#4c8dff]/13 transition-all"
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

  <!-- 2. Вариант для обычных полей ввода (текст, пароль и т.д.) -->
  <div v-else class="flex flex-col gap-1.5">
    <label v-if="label" class="text-[9px] font-medium text-[#aeb6c0] uppercase tracking-wider">
      {{ label }}
    </label>
    <div
      class="relative flex items-center h-10 px-3 bg-[#0c1014] border border-[#29313a] rounded-md transition-all focus-within:border-[#3969ab] focus-within:ring-3 focus-within:ring-[#4c8dff]/10"
      :class="{
        'border-[#ff6978] focus-within:border-[#ff6978] focus-within:ring-[#ff6978]/10': error,
      }"
    >
      <slot name="prefix" />
      <input
        :type="type || 'text'"
        :value="modelValue"
        :placeholder="placeholder"
        class="w-full bg-transparent border-0 outline-0 text-[11px] text-[#e2e6eb] placeholder:text-[#48515c]"
        :class="{ 'font-mono': mono }"
        @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
      />
      <slot name="suffix" />
    </div>
    <span v-if="error" class="text-[9px] text-[#ff6978]">{{ error }}</span>
  </div>
</template>
