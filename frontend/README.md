# RiskLense Frontend

Vue 3, TypeScript, Vite, Vue Router, Pinia, and Tailwind CSS 4 starter for the RiskLense risk-analysis platform.

## Development

```sh
npm ci
npm run dev
```

The backend's `demo` profile seeds sample users, portfolios, positions, price history, and an initial risk assessment. Start the backend and sign in as `user@risklens.com` with password `12345678` to view the seeded Growth Portfolio dashboard. Other demo accounts are `analyst@risklens.com` and `admin@risklens.com` with the same password. Existing databases are seeded idempotently at application startup.

The frontend connects to the backend by default. To use the mock login and sample dashboard responses instead, set `VITE_ENABLE_MOCKS=true` before starting Vite.

## Quality checks

```sh
npm run lint
npm run format:check
npm run typecheck
npm run build
```

The GitHub Actions workflow runs these checks for pushes and pull requests targeting `main` and `develop`.

## Structure

- `src/api` — API integration modules
- `src/assets` — global styles and static assets
- `src/components` — reusable design-system components
- `src/composables` — reusable Composition API logic
- `src/layouts` — authenticated application shell
- `src/router` — routes and authentication guard
- `src/stores` — Pinia state
- `src/views` — route-level screens
