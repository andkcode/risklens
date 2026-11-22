<script setup lang="ts">
import { ref } from 'vue';
import { useRisk } from '../composables/useRisk';
import RiskHeader from '../components/risk/RiskHeader.vue';
import RiskCoreGrid from '../components/risk/RiskCoreGrid.vue';
import RiskSheduleLabel from '../components/risk/RiskSheduleLabel.vue';
import RiskInsightsCard from '../components/risk/RiskInsightsCard.vue';
import RiskAssistantChat from '../components/risk/RiskAssistantChat.vue';

const riskRange = ref('3M');

const {
  metrics,
  timelineAssets,
  insights,
  chatMessages,
  isLoading,
  error,
  handleExportPdf,
  handleExportCsv,
  handlePrint,
  handleSendMessage,
} = useRisk();
</script>

<template>
  <div class="h-full overflow-y-auto p-6 space-y-6">
    <!-- Loading State -->
    <div v-if="isLoading" class="flex items-center justify-center py-24">
      <div
        class="size-8 rounded-full border-2 border-brand-accent border-t-transparent animate-spin"
      ></div>
    </div>

    <!-- Error State -->
    <div
      v-else-if="error"
      class="p-4 text-xs text-rose-400 bg-rose-500/10 border border-rose-500/20 rounded-lg"
    >
      {{ error }}
    </div>

    <!-- Content -->
    <template v-else>
      <RiskHeader
        portfolio-name="Main Equity Fund"
        last-analyzed-time="8 minutes ago"
        @export-pdf="handleExportPdf"
        @export-csv="handleExportCsv"
        @print-report="handlePrint"
      />

      <RiskCoreGrid v-if="metrics" :metrics="metrics" />

      <RiskSheduleLabel v-model="riskRange" :items="timelineAssets" />

      <!-- Нижняя сетка: Инсайты слева, Ассистент чата справа -->
      <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
        <div class="lg:col-span-8">
          <RiskInsightsCard v-if="insights" :insights="insights" />
        </div>
        <div class="lg:col-span-4">
          <RiskAssistantChat :messages="chatMessages" @send="handleSendMessage" />
        </div>
      </div>
    </template>
  </div>
</template>
