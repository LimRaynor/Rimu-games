<template>
  <header class="app-header">
    <div class="header-inner">
      <RouterLink to="/" class="logo">🎮 Rimu Games</RouterLink>
      <nav class="nav">
        <RouterLink to="/" class="nav-link">대시보드</RouterLink>
        <RouterLink to="/profile" class="nav-link">마이페이지</RouterLink>
        <button class="btn btn-secondary logout-btn" @click="handleLogout">로그아웃</button>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { useAuthStore } from '../../stores/auth.js'
import { authApi } from '../../api/auth.js'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

async function handleLogout() {
  try {
    await authApi.logout()
  } finally {
    authStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.app-header {
  background: var(--color-bg-card);
  border-bottom: 1px solid var(--color-border);
  padding: 0 16px;
}

.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
}

.logo {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-primary);
  text-decoration: none;
}

.nav {
  display: flex;
  align-items: center;
  gap: 16px;
}

.nav-link {
  color: var(--color-text-muted);
  font-size: 14px;
  text-decoration: none;
  transition: color 0.2s;
}

.nav-link:hover,
.nav-link.router-link-active {
  color: var(--color-primary);
  text-decoration: none;
}

.logout-btn {
  padding: 6px 14px;
  font-size: 13px;
}
</style>
