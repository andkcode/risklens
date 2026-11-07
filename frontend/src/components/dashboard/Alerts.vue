<!-- eslint-disable vue/multi-word-component-names -->
<script setup lang="ts">
import type { RiskAlert } from '../../types/dashboard';
import { getRiskDotClass } from '../../utils/riskStyle';
import BaseCard from '../BaseCard.vue';

defineProps<{
  alerts: RiskAlert[];
}>();
</script>

<template>
  <BaseCard
    variant="standard"
    title="Recent risk alerts"
    subtitle="Last 24 hours"
    class="lg:col-span-3 flex flex-col"
  >
    <template #header-actions>
      <span
        class="size-5 rounded-full bg-rose-500/20 text-rose-400 text-[10px] font-bold grid place-items-center"
      >
        {{ alerts.length }}
      </span>
    </template>

    <div class="space-y-3">
      <div
        v-for="alert in alerts"
        :key="alert.id"
        class="p-3 bg-[#0c1014] border border-brand-border rounded-lg space-y-1"
      >
        <div class="flex items-center">
          <span class="text-xs font-semibold text-white flex items-center gap-2">
            <span class="size-2 rounded-full" :class="getRiskDotClass(alert.severity)"></span>
            {{ alert.title }}
          </span>
          <span class="text-[9px] text-brand-muted-2">{{ alert.timeAgo }}</span>
        </div>
        <p class="text-[11px] text-brand-secondary pl-4">{{ alert.description }}</p>
      </div>
    </div>

    <div class="mt-4 text-center">
      <button class="text-xs font-medium text-brand-accent hover:underline">View all alerts →</button>
    </div>
  </BaseCard>
</template>
