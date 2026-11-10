import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '../stores/auth';

export function useAppLayout() {
  const auth = useAuthStore();
  const router = useRouter();
  const route = useRoute();

  const isMobileMenuOpen = ref(false);
  const searchQuery = ref('');

  // Check if current route requires full-width layout (e.g., portfolios master-detail view)
  const isFullWidth = computed(() => {
    return route.path.startsWith('/portfolios') || Boolean(route.meta.fullWidth);
  });

  const toggleMobileMenu = () => {
    isMobileMenuOpen.value = !isMobileMenuOpen.value;
  };

  const closeMobileMenu = () => {
    isMobileMenuOpen.value = false;
  };

  const handleLogout = () => {
    auth.logout();
    void router.push({ name: 'login' });
  };

  return {
    auth,
    isFullWidth,
    isMobileMenuOpen,
    searchQuery,
    toggleMobileMenu,
    closeMobileMenu,
    handleLogout,
  };
}
