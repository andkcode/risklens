<script setup lang="ts">
import type { RiskInsightsData } from '../../types/risk';
import BaseCard from '../BaseCard.vue';
import BaseButton from '../BaseButton.vue';
import RiskIcons from '../icons/RiskIcons.vue';
import { getRiskBadgeClass } from '../../utils/riskStyle';

defineProps<{
  insights: RiskInsightsData;
}>();

defineEmits<{
  (e: 'action-click', action: string): void;
}>();
</script>

<template>
  <BaseCard variant="standard" class="risk-insights-card">
    <!-- Header -->
    <div class="flex items-center justify-between mb-2">
      <div>
        <h2 class="text-sm font-bold text-white tracking-tight">
          AI Risk Insights &amp; Reasoning
        </h2>
        <p class="text-[11px] text-brand-secondary mt-0.5">
          Plain-language interpretation of quantitative risk drivers
        </p>
      </div>
      <div class="flex items-center gap-1.5 text-xs text-brand-secondary">
        <span class="size-2 rounded-full bg-emerald-400 inline-block animate-pulse"></span>
        <span class="text-[10px] uppercase font-semibold">AI analysis complete</span>
      </div>
    </div>

    <!-- Primary Finding Box -->
    <div class="bg-brand-sidebar border border-brand-border rounded-xl p-4 my-4">
      <div
        class="flex items-center gap-1.5 text-[10px] font-bold uppercase text-brand-accent tracking-wider mb-1.5"
      >
        <RiskIcons name="analytics" :size="12" />
        <span>Primary Finding</span>
      </div>
      <h3 class="text-base font-bold text-white mb-2">{{ insights.primaryTitle }}</h3>
      <p class="text-xs text-brand-secondary leading-relaxed">
        {{ insights.primaryDescription }}
      </p>
    </div>

    <!-- Insights List -->
    <div class="space-y-2.5 mb-4">
      <div
        v-for="item in insights.items"
        :key="item.id"
        class="flex items-center justify-between p-3 rounded-lg bg-brand-sidebar/60 border border-brand-border/60 hover:bg-brand-sidebar transition-colors"
      >
        <div class="flex items-center gap-3 min-w-0">
          <span class="font-mono text-xs font-bold text-brand-secondary shrink-0">{{
            item.number
          }}</span>
          <div class="min-w-0">
            <h4 class="text-xs font-bold text-white truncate">{{ item.title }}</h4>
            <p class="text-[11px] text-brand-secondary truncate mt-0.5">{{ item.description }}</p>
          </div>
        </div>
        <!-- Динамический вызов утилиты без хардкода прямо в шаблоне -->
        <span
          class="px-2 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wider shrink-0 ml-3"
          :class="getRiskBadgeClass(item.impact)"
        >
          {{ item.impact }}
        </span>
      </div>
    </div>

    <!-- Recommended Actions Footer -->
    <div
      class="flex items-center justify-between pt-3 border-t border-brand-border flex-wrap gap-2"
    >
      <span class="text-[10px] text-brand-secondary uppercase font-semibold tracking-wider"
        >Recommended actions</span
      >
      <div class="flex items-center gap-2 flex-wrap">
        <BaseButton
          v-for="action in insights.recommendedActions"
          :key="action"
          variant="secondary"
          size="sm"
          class="text-[11px] h-7"
          @click="$emit('action-click', action)"
        >
          {{ action }}
        </BaseButton>
      </div>
    </div>
  </BaseCard>
</template>
