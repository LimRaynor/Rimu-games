<template>
  <div class="auth-container">
    <div class="auth-card card">
      <h1 class="auth-title">🎮 Rimu Games</h1>
      <p class="auth-subtitle">인디게임팀 포트폴리오</p>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>이메일</label>
          <input v-model="form.email" type="email" placeholder="이메일을 입력하세요" required />
        </div>
        <div class="form-group">
          <label>비밀번호</label>
          <input v-model="form.password" type="password" placeholder="비밀번호를 입력하세요" required />
        </div>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <button type="submit" class="btn btn-primary full-width" :disabled="loading">
          {{ loading ? '로그인 중...' : '로그인' }}
        </button>
      </form>

      <p class="auth-footer">
        계정이 없으신가요? <RouterLink to="/signup">회원가입</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../../api/auth.js'
import { useAuthStore } from '../../stores/auth.js'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({ email: '', password: '' })
const errorMessage = ref('')
const loading = ref(false)

async function handleLogin() {
  errorMessage.value = ''
  loading.value = true
  try {
    const data = await authApi.login(form.value)
    authStore.setAuth(data.accessToken, { email: form.value.email })
    router.push('/')
  } catch (e) {
    errorMessage.value = e.response?.data?.message || '로그인에 실패했습니다.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
}

.auth-card {
  width: 100%;
  max-width: 400px;
}

.auth-title {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-primary);
  text-align: center;
  margin-bottom: 4px;
}

.auth-subtitle {
  text-align: center;
  color: var(--color-text-muted);
  font-size: 14px;
  margin-bottom: 32px;
}

.full-width {
  width: 100%;
  justify-content: center;
  padding: 12px;
  font-size: 15px;
}

.auth-footer {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: var(--color-text-muted);
}
</style>
