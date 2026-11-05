<template>
  <main class="grid min-h-screen place-items-center bg-slate-50 px-5">
    <div class="text-center">
      <p class="font-mono text-sm font-semibold text-brand-accent">404</p>
      <h1 class="mt-2 text-2xl font-bold text-slate-900">Page not found</h1>
      <p class="mt-2 text-sm text-slate-600"></p>
      <RouterLink
        to="/login"
        class="mt-6 inline-flex min-h-10 items-center rounded-lg bg-brand-accent px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
      >
        To home
      </RouterLink>
    </div>
  </main>
</template>
