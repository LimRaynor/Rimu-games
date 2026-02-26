<template>
  <div id="app">
    <AppHeader v-if="authStore.isLoggedIn" />
    <main class="main-content">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { useAuthStore } from './stores/auth.js'
import AppHeader from './components/layout/AppHeader.vue'

const authStore = useAuthStore()
</script>
