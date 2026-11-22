<script setup lang="ts">
import { computed } from 'vue';
import type { RiskMetricsData } from '../../types/risk';
import BaseCard from '../BaseCard.vue';
import { getRiskBadgeClass, getRiskDotClass, getTrendColorClass } from '../../utils/riskStyle';

const props = defineProps<{
  metrics: RiskMetricsData;
}>();

const dialDashOffset = computed(() => {
  const maxScore = 100;
  const totalLength = 120;
  const clampedScore = Math.max(0, Math.min(props.metrics.overallScore, maxScore));
  return totalLength - (clampedScore / maxScore) * totalLength;
});
</script>

<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
    <!-- Overall Risk Score -->
    <BaseCard
      variant="standard"
      class="core-risk-card overall-risk-card flex flex-col justify-between"
    >
      <div class="core-risk-label flex items-center justify-between">
        <span class="text-xs text-brand-secondary uppercase tracking-wider"
          >Overall risk score</span
        >
        <small class="font-semibold" :class="getTrendColorClass('+live')">LIVE</small>
      </div>

      <!-- Увеличен отступ и область для предотвращения наложения -->
      <div class="risk-dial flex items-center justify-center pt-4 pb-2 relative">
        <svg viewBox="0 0 100 60" class="w-40">
          <path d="M12 52 A38 38 0 0 1 88 52" stroke="#2228" stroke-width="8" fill="none" />
          <path
            class="dial-value transition-all duration-500"
            :class="getRiskBadgeClass(metrics.overallLevel)"
            d="M12 52 A38 38 0 0 1 88 52"
            stroke="currentColor"
            stroke-width="8"
            fill="none"
            stroke-dasharray="120"
            :stroke-dashoffset="dialDashOffset"
          />
        </svg>
        <div class="absolute inset-0 flex flex-col items-center justify-center text-center pt-3">
          <strong class="text-2xl font-bold text-white">{{ metrics.overallScore }}</strong>
          <span class="text-xs text-brand-secondary">/100</span>
        </div>
      </div>

      <div
        class="core-risk-footer flex items-center justify-between text-xs pt-2 border-t border-brand-border"
      >
        <span
          class="px-2 py-0.5 rounded text-[10px] font-semibold uppercase"
          :class="getRiskBadgeClass(metrics.overallLevel)"
        >
          {{ metrics.overallLevel }}
        </span>
        <small
          :class="
            getTrendColorClass(
              metrics.scoreChange > 0 ? `+${metrics.scoreChange}` : `${metrics.scoreChange}`,
            )
          "
        >
          {{ metrics.scoreChange > 0 ? `+${metrics.scoreChange}` : metrics.scoreChange }} pts this
          week
        </small>
      </div>
    </BaseCard>

    <!-- VaR Card -->
    <BaseCard variant="standard" class="core-risk-card var-card flex flex-col justify-between">
      <div class="core-risk-label flex items-center justify-between">
        <span class="text-xs text-brand-secondary uppercase tracking-wider"
          >Value at Risk (VaR)</span
        >
        <small class="text-[10px] text-brand-secondary">95% CONF. · 1 DAY</small>
      </div>
      <div class="my-2">
        <strong class="text-2xl font-bold text-white font-mono">{{ metrics.varAmount }}</strong>
        <div class="var-rate flex items-center gap-2 mt-1 flex-wrap">
          <span class="text-xs font-semibold" :class="getTrendColorClass('-loss')">
            {{ metrics.varPercentage }}
          </span>
          <small class="text-[10px] text-brand-secondary">potential maximum loss</small>
        </div>
      </div>
      <div
        class="core-risk-footer text-[10px] text-brand-secondary pt-2 border-t border-brand-border truncate"
      >
        Historical simulation · 500 observations
      </div>
    </BaseCard>

    <!-- Stress Test Simulation -->
    <BaseCard
      variant="standard"
      class="core-risk-card stress-simulation-card flex flex-col justify-between"
    >
      <div class="core-risk-label flex items-center justify-between">
        <span class="text-xs text-brand-secondary uppercase tracking-wider"
          >Stress test simulation</span
        >
        <small
          class="font-semibold uppercase px-1.5 py-0.5 rounded text-[10px]"
          :class="getRiskBadgeClass('High')"
          >SEVERE</small
        >
      </div>
      <div class="my-2">
        <div class="scenario-name flex items-center gap-1.5 text-xs text-brand-secondary mb-1">
          <span class="size-2 rounded-full shrink-0" :class="getRiskDotClass('Medium')"></span>
          <span>{{ metrics.stressCrashPercent }} Market Crash</span>
        </div>
        <strong class="text-2xl font-bold text-white font-mono">{{ metrics.stressAmount }}</strong>
        <div
          class="scenario-stats flex items-center justify-between text-[11px] text-brand-secondary mt-1"
        >
          <span>Drawdown {{ metrics.stressDrawdown }}</span>
          <span class="text-white font-medium"
            >Recovery {{ metrics.stressRecoveryMonths }} mo.</span
          >
        </div>
      </div>
      <div
        class="core-risk-footer text-[10px] text-brand-secondary pt-2 border-t border-brand-border truncate"
      >
        Based on 2008 / 2020 stress conditions
      </div>
    </BaseCard>

    <!-- Heatmap Card -->
    <BaseCard variant="standard" class="core-risk-card heatmap-card flex flex-col justify-between">
      <div class="core-risk-label flex items-center justify-between">
        <span class="text-xs text-brand-secondary uppercase tracking-wider"
          >Asset risk heatmap</span
        >
        <small class="text-[10px] text-brand-secondary uppercase"
          >{{ metrics.heatmapAssetsCount }} ASSETS</small
        >
      </div>

      <div class="heatmap-blocks grid grid-cols-6 gap-1.5 my-3">
        <template v-for="stat in metrics.heatmapStats" :key="stat.level">
          <span
            v-for="n in stat.count"
            :key="`${stat.level}-${n}`"
            class="h-4 rounded opacity-60 transition-all"
            :class="getRiskDotClass(stat.level)"
          ></span>
        </template>
      </div>

      <div
        class="heatmap-legend flex items-center justify-between text-[10px] text-brand-secondary pt-2 border-t border-brand-border gap-1"
      >
        <span
          v-for="stat in metrics.heatmapStats"
          :key="stat.level"
          class="flex items-center gap-1"
        >
          <b :class="getTrendColorClass(stat.level === 'High' ? '-high' : '+medium')">{{
            stat.level
          }}</b>
          <span>{{ stat.percentage }}%</span>
        </span>
      </div>
    </BaseCard>
  </div>
</template>
