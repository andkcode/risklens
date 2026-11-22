<script setup lang="ts">
import BaseCard from '../BaseCard.vue';
import BaseButton from '../BaseButton.vue';
import BaseInput from '../BaseInput.vue';
import BaseIcon from '../icons/PortfolioIcons.vue';

// Принимаем стейт форм через пропсы
defineProps<{
  stockForm: {
    ticker: string;
    quantity: string | number;
    price: string | number;
    date: string;
  };
  cryptoForm: {
    search: string;
    quantity: string | number;
    price: string | number;
    network: string;
  };
}>();

// Пробрасываем события клика наверх родителям
defineEmits<{
  (e: 'add-stock'): void;
  (e: 'add-crypto'): void;
}>();
</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 my-6">
    <!-- Add New Stock Card -->
    <BaseCard variant="standard" class="p-5 flex flex-col justify-between">
      <div>
        <div class="flex items-center justify-between mb-4">
          <div>
            <h3 class="text-sm font-bold text-white tracking-tight">Add new stock</h3>
            <p class="text-[11px] text-brand-secondary mt-0.5">
              Add an equity position to portfolio
            </p>
          </div>
          <div
            class="size-8 rounded-lg bg-brand-sidebar border border-brand-border flex items-center justify-center text-brand-accent"
          >
            <BaseIcon name="stock" />
          </div>
        </div>

        <div class="space-y-4">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <BaseInput v-model="stockForm.ticker" label="Ticker symbol" placeholder="AAPL" mono />
            <BaseInput
              v-model="stockForm.quantity"
              type="number"
              label="Quantity"
              placeholder="0"
              mono
            />
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <BaseInput v-model="stockForm.price" label="Purchase price" placeholder="$ 0.00" mono />
            <BaseInput v-model="stockForm.date" type="date" label="Date purchased" mono />
          </div>
        </div>
      </div>

      <div class="mt-6 pt-4 border-t border-brand-border">
        <BaseButton
          variant="primary"
          class="w-full py-2 bg-brand-accent hover:bg-blue-600 text-white text-xs font-semibold rounded-lg flex items-center justify-center gap-2 shadow-[0_4px_16px_rgba(47,112,223,0.3)] transition-colors"
          @click="$emit('add-stock')"
        >
          <BaseIcon name="plus" />
          Add stock
        </BaseButton>
      </div>
    </BaseCard>

    <!-- Add New Cryptocurrency Card -->
    <BaseCard variant="standard" class="p-5 flex flex-col justify-between">
      <div>
        <div class="flex items-center justify-between mb-4">
          <div>
            <h3 class="text-sm font-bold text-white tracking-tight">Add new cryptocurrency</h3>
            <p class="text-[11px] text-brand-secondary mt-0.5">
              Add an on-chain asset to portfolio
            </p>
          </div>
          <div
            class="size-8 rounded-lg bg-brand-sidebar border border-brand-border flex items-center justify-center text-purple-400"
          >
            <BaseIcon name="crypto" />
          </div>
        </div>

        <div class="space-y-4">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <BaseInput
              v-model="cryptoForm.search"
              label="Coin / token search"
              placeholder="Search BTC, ETH..."
            />
            <BaseInput
              v-model="cryptoForm.quantity"
              type="number"
              label="Quantity"
              placeholder="0.00"
              mono
            />
          </div>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <BaseInput
              v-model="cryptoForm.price"
              label="Price per coin"
              placeholder="$ 0.00"
              mono
            />
            <!-- Network Select -->
            <div class="flex flex-col gap-1.5">
              <label class="text-[9px] font-medium text-[#aeb6c0] uppercase tracking-wider">
                Select network
              </label>
              <div
                class="relative flex items-center h-10 px-3 bg-brand-surface border border-brand-border rounded-md transition-all focus-within:border-brand-accent"
              >
                <select
                  v-model="cryptoForm.network"
                  class="w-full bg-transparent border-0 outline-0 text-[11px] text-white appearance-none cursor-pointer"
                >
                  <option value="" disabled selected class="bg-brand-surface text-brand-muted">
                    Choose network
                  </option>
                  <option value="ethereum" class="bg-brand-surface text-white">
                    Ethereum (ERC-20)
                  </option>
                  <option value="solana" class="bg-brand-surface text-white">Solana (SPL)</option>
                  <option value="bitcoin" class="bg-brand-surface text-white">
                    Bitcoin Network
                  </option>
                  <option value="bsc" class="bg-brand-surface text-white">
                    Binance Smart Chain
                  </option>
                </select>
                <div
                  class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-3 text-brand-muted"
                >
                  <svg class="size-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M19 9l-7 7-7-7"
                    />
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-6 pt-4 border-t border-brand-border">
        <BaseButton
          variant="primary"
          class="w-full py-2 bg-purple-600 hover:bg-purple-500 text-white text-xs font-semibold rounded-lg flex items-center justify-center gap-2 shadow-[0_4px_16px_rgba(124,58,237,0.3)] transition-colors"
          @click="$emit('add-crypto')"
        >
          <BaseIcon name="plus" />
          Add crypto
        </BaseButton>
      </div>
    </BaseCard>
  </div>
</template>
