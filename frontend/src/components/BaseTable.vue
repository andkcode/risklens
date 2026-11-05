<template>
  <div class="overflow-x-auto border border-[#222830] rounded-[9px] bg-[#101419]">
    <table class="w-full min-w-[920px] border-collapse text-left">
      <thead>
        <tr
          class="h-[37px] bg-[#0d1115] border-b border-[#222830] text-[#606b78] text-[8px] font-semibold uppercase tracking-wider"
        >
          <slot name="header" />
        </tr>
      </thead>
      <tbody class="divide-y divide-[#1c2229]">
        <slot name="body" />
      </tbody>
    </table>
  </div>
</template>
