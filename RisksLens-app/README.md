# RiskLense Frontend

Vue 3, TypeScript, Vite, Vue Router, Pinia, and Tailwind CSS 4 starter for the RiskLense risk-analysis platform.

## Development

```sh
npm ci
npm run dev
```

The demo login accepts any non-empty email address and stores a mock access token in browser local storage. All portfolio metrics and risk findings are sample data; authentication, market data, and AI analysis are not connected to a backend.

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
