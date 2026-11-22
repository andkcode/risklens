<script setup lang="ts">
import { ref } from 'vue';
import type { ChatMessage } from '../../types/risk';
import BaseCard from '../BaseCard.vue';
import BaseButton from '../BaseButton.vue';
import RiskIcons from '../icons/RiskIcons.vue';

defineProps<{
  messages: ChatMessage[];
}>();

const emit = defineEmits<{
  (e: 'send', text: string): void;
  (e: 'suggest-click', suggestion: string): void;
}>();

const inputMessage = ref('');

const handleSend = () => {
  if (!inputMessage.value.trim()) return;
  emit('send', inputMessage.value);
  inputMessage.value = '';
};
</script>

<template>
  <BaseCard variant="standard" class="risk-assistant-card flex flex-col justify-between h-full">
    <!-- Header -->
    <div>
      <div class="flex items-center justify-between mb-3">
        <div>
          <h2 class="text-sm font-bold text-white tracking-tight">Ask AI Risk Assistant</h2>
          <p class="text-[11px] text-brand-secondary mt-0.5">Context-aware portfolio guidance</p>
        </div>
        <div class="flex items-center gap-1.5 text-xs text-brand-secondary">
          <span class="size-2 rounded-full bg-emerald-400 inline-block"></span>
          <span class="text-[10px] uppercase font-semibold">Online</span>
        </div>
      </div>

      <!-- Messages History Container -->
      <div class="space-y-3 my-4 max-h-70 overflow-y-auto pr-1">
        <div
          v-for="msg in messages"
          :key="msg.id"
          class="flex flex-col"
          :class="msg.sender === 'user' ? 'items-end' : 'items-start'"
        >
          <div
            class="max-w-[85%] rounded-xl px-3.5 py-2.5 text-xs leading-relaxed"
            :class="
              msg.sender === 'user'
                ? 'bg-brand-accent text-white rounded-br-none'
                : 'bg-brand-sidebar border border-brand-border text-brand-secondary rounded-bl-none'
            "
          >
            <div
              v-if="msg.sender === 'ai'"
              class="flex items-center gap-1 text-[10px] font-bold text-brand-accent uppercase mb-1"
            >
              <span>AI</span>
            </div>
            <p :class="msg.sender === 'ai' ? 'text-white font-medium' : ''">{{ msg.text }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Suggested & Input Area -->
    <div class="pt-3 border-t border-brand-border">
      <div class="flex items-center gap-1.5 mb-3 flex-wrap">
        <span class="text-[10px] text-brand-secondary uppercase font-semibold mr-1">Suggested</span>
        <button
          v-for="suggestion in ['How can I lower my VaR?', 'Model a 20% crash']"
          :key="suggestion"
          class="text-[11px] px-2.5 py-1 bg-brand-sidebar border border-brand-border rounded-lg text-brand-secondary hover:text-white hover:border-brand-accent transition-colors"
          @click="$emit('suggest-click', suggestion)"
        >
          {{ suggestion }}
        </button>
      </div>

      <div class="flex items-center gap-2">
        <input
          v-model="inputMessage"
          type="text"
          placeholder="Ask about risk, VaR, or allocation..."
          class="flex-1 bg-brand-sidebar border border-brand-border rounded-lg px-3 py-2 text-xs placeholder-brand-secondary focus:outline-none transition-colors"
          @keyup.enter="handleSend"
        />
        <BaseButton variant="primary" size="sm" class="h-9 px-3" @click="handleSend">
          <RiskIcons name="send" :size="14" />
        </BaseButton>
      </div>
    </div>
  </BaseCard>
</template>
