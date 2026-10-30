import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router';
import AppLayout from '../layouts/AppLayout.vue';
import { useAuthStore } from '../stores/auth';
import { pinia } from '../stores/pinia';

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterView.vue'),
    meta: { requiresAuth: false },
  },
  {
    path: '/',
    component: AppLayout,
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        redirect: { name: 'dashboard' },
      },
      {
        path: 'dashboard',
        name: 'dashboard',
        component: () => import('../views/DashboardView.vue'),
        meta: { requiresAuth: true },
      },
      {
        path: 'portfolios',
        name: 'portfolios',
        component: () => import('../views/PortfoliosView.vue'),
        meta: { requiresAuth: true },
      },
      {
        path: 'portfolios/:id',
        name: 'portfolio-details',
        component: () => import('../views/PortfolioDetailsView.vue'),
        meta: { requiresAuth: true },
        props: true,
      },
      {
        path: 'risk/:id',
        name: 'risk-analysis',
        component: () => import('../views/RiskAnalysisView.vue'),
        meta: { requiresAuth: true },
        props: true,
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('../views/NotFoundView.vue'),
    meta: { requiresAuth: false },
  },
];

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.beforeEach((to) => {
  const auth = useAuthStore(pinia);

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } };
  }

  if ((to.name === 'login' || to.name === 'register') && auth.isAuthenticated) {
    return { name: 'dashboard' };
  }
});
