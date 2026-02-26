<template>
  <div class="auth-container">
    <div class="auth-card card">
      <h1 class="auth-title">🎮 Rimu Games</h1>
      <p class="auth-subtitle">팀원으로 합류하세요</p>

      <form @submit.prevent="handleSignup">
        <div class="form-group">
          <label>이메일</label>
          <input v-model="form.email" type="email" placeholder="이메일을 입력하세요" required />
        </div>
        <div class="form-group">
          <label>닉네임</label>
          <input v-model="form.nickname" type="text" placeholder="닉네임을 입력하세요" required />
        </div>
        <div class="form-group">
          <label>비밀번호</label>
          <input v-model="form.password" type="password" placeholder="6자 이상" required />
        </div>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <button type="submit" class="btn btn-primary full-width" :disabled="loading">
          {{ loading ? '가입 중...' : '회원가입' }}
        </button>
      </form>

      <p class="auth-footer">
        이미 계정이 있으신가요? <RouterLink to="/login">로그인</RouterLink>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../../api/auth.js'

const router = useRouter()

const form = ref({ email: '', nickname: '', password: '' })
const errorMessage = ref('')
const loading = ref(false)

async function handleSignup() {
  errorMessage.value = ''
  loading.value = true
  try {
    await authApi.signup(form.value)
    router.push('/login')
  } catch (e) {
    errorMessage.value = e.response?.data?.message || '회원가입에 실패했습니다.'
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
