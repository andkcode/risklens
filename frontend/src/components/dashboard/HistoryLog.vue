<script setup lang="ts">
import type { HistoryLogItem } from '../../types/dashboard';
import { getRiskBadgeClass, getActionStatusClass } from '../../utils/riskStyle';
import BaseCard from '../BaseCard.vue';
import BaseButton from '../BaseButton.vue';
import BaseTable from '../BaseTable.vue';
import DashboardIcon from '../icons/DashboardIcons.vue';

defineProps<{
  history: HistoryLogItem[];
}>();
</script>

<template>
  <BaseCard
    variant="standard"
    title="Analysis history log"
    subtitle="Audit trail of completed portfolio and asset risk analyses"
  >
    <template #header-actions>
      <div class="flex items-center gap-2">
        <BaseButton variant="secondary" size="sm">
          <DashboardIcon name="filter" :size="12" class="mr-1.5" />
          <span>Filter</span>
        </BaseButton>
        <BaseButton variant="secondary" size="sm">
          <DashboardIcon name="download" :size="12" class="mr-1.5" />
          <span>Export CSV</span>
        </BaseButton>
      </div>
    </template>

    <BaseTable>
      <template #header>
        <th class="p-3">Date of Analysis</th>
        <th class="p-3">Type</th>
        <th class="p-3">Assets Analysed</th>
        <th class="p-3">Primary Risk Factor</th>
        <th class="p-3">Risk Rating / Score</th>
        <th class="p-3">Action Recommended / Taken</th>
      </template>
      <template #body>
        <tr v-for="item in history" :key="item.id" class="hover:bg-[#141a21] text-xs">
          <!-- Date and Time -->
          <td class="p-3 font-medium text-white">
            {{ item.date }}
            <div class="text-[9px] text-[#606b78]">{{ item.utcTime }}</div>
          </td>

          <!-- Type -->
          <td class="p-3 text-[#8994a2]">{{ item.type }}</td>

          <!-- Assets Analysed -->
          <td class="p-3 text-[#8994a2]">{{ item.assetsAnalysed }}</td>

          <!-- Primary Risk Factor -->
          <td class="p-3 text-[#e2e6eb]">{{ item.primaryRiskFactor }}</td>

          <td class="p-3">
            <span
              class="px-2 py-0.5 rounded text-[10px] font-semibold"
              :class="getRiskBadgeClass(item.riskRating)"
            >
              {{ item.riskRating }} · {{ item.score }}
            </span>
          </td>

          <td class="p-3 flex items-center justify-between">
            <span class="text-[#8994a2]">{{ item.actionRecommended }}</span>
            <span
              class="px-2 py-0.5 rounded text-[9px] font-semibold"
              :class="getActionStatusClass(item.actionStatus)"
            >
              {{ item.actionStatus }}
            </span>
          </td>
        </tr>
      </template>
    </BaseTable>
  </BaseCard>
</template>
