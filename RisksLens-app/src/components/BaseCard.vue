<script setup lang="ts">
withDefaults(
  defineProps<{
    title?: string;
    subtitle?: string;
    variant?: 'standard' | 'auth';
  }>(),
  {
    variant: 'standard',
    title: undefined,
    subtitle: undefined,
  },
);
</script>

<template>
  <div
    class="transition-all"
    :class="[
      /* Стандартный вариант для дашбордов и таблиц */
      variant === 'standard' &&
        'bg-[#101419] border border-[#222830] rounded-[9px] shadow-[0_10px_30px_rgba(0,0,0,.08)]',

      /* Вариант для экранов авторизации/регистрации со свечением сверху и отступами */
      variant === 'auth' &&
        'relative bg-[rgba(16,20,25,.96)] border border-[#252d36] rounded-[10px] p-8 shadow-[0_28px_80px_rgba(0,0,0,.38),0_0_0_1px_rgba(255,255,255,.012)] before:absolute before:top-[-1px] before:left-[20%] before:right-[20%] before:h-[1px] before:bg-gradient-to-r before:from-transparent before:via-[rgba(76,141,255,.72)] before:to-transparent',
    ]"
  >
    <!-- Шапка карточки (только для standard при наличии заголовка или слотов) -->
    <div
      v-if="variant === 'standard' && (title || $slots.header || $slots['header-actions'])"
      class="h-[66px] px-[19px] flex items-center justify-between border-b border-[#222830]"
    >
      <div>
        <h3 v-if="title" class="text-xs font-semibold text-[#d9dde3] tracking-tight">
          {{ title }}
        </h3>
        <p v-if="subtitle" class="mt-1 text-[9px] text-[#8994a2]">{{ subtitle }}</p>
      </div>
      <slot name="header-actions" />
    </div>

    <!-- Основной контент (для standard с отступами, для auth — контент уже с палдингом p-8) -->
    <div :class="variant === 'standard' ? 'p-[19px]' : ''">
      <slot />
    </div>
  </div>
</template>
