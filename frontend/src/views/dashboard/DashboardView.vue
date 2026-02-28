<template>
  <div class="dashboard-wrap" ref="wrapRef" @scroll="onScroll">

    <!-- ── Fixed Header ── -->
    <header class="dash-header">
      <template v-if="!authStore.isLoggedIn">
        <div class="header-auth">
          <button class="btn-dash btn-login" @click="router.push('/login')">로그인</button>
          <button class="btn-dash btn-signup" @click="router.push('/signup')">회원가입</button>
        </div>
      </template>
      <template v-else>
        <div class="header-avatar" @click="router.push('/profile')">
          {{ avatarInitial }}
        </div>
      </template>
    </header>

    <!-- ── Page 1: 팀 소개 ── -->
    <section class="page page-1">
      <div class="team-logo">
        <div class="logo-placeholder">RIMU<br>GAMES</div>
      </div>
      <!-- STEP 3에서 채움 -->
    </section>

    <!-- ── Page 2: 프로젝트 카드 ── -->
    <section class="page page-2">
      <div class="team-logo">
        <div class="logo-placeholder">RIMU<br>GAMES</div>
      </div>
      <!-- STEP 4에서 채움 -->
    </section>

    <!-- ── Page 3: 연락처 ── -->
    <section class="page page-3">
      <div class="team-logo">
        <div class="logo-placeholder">RIMU<br>GAMES</div>
      </div>
      <!-- STEP 5에서 채움 -->
    </section>

    <!-- ── Fixed Bottom Nav ── -->
    <nav class="page-nav">
      <button class="nav-arrow" @click="prevPage" aria-label="이전">
        <span class="arrow arrow-left"></span>
      </button>
      <div class="nav-dots">
        <span
          v-for="i in 3"
          :key="i"
          class="dot"
          :class="{ active: currentPage === i - 1 }"
          @click="goToPage(i - 1)"
        />
      </div>
      <button class="nav-arrow" @click="nextPage" aria-label="다음">
        <span class="arrow arrow-right"></span>
      </button>
    </nav>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth.js'

const router = useRouter()
const authStore = useAuthStore()
const wrapRef = ref(null)
const currentPage = ref(0)

const avatarInitial = computed(() => {
  const name = authStore.user?.nickname || authStore.user?.email || '?'
  return name.charAt(0).toUpperCase()
})

function goToPage(index) {
  currentPage.value = index
  wrapRef.value?.scrollTo({ top: index * window.innerHeight, behavior: 'smooth' })
}

function prevPage() {
  if (currentPage.value > 0) goToPage(currentPage.value - 1)
}

function nextPage() {
  if (currentPage.value < 2) goToPage(currentPage.value + 1)
}

let scrollTimeout = null
function onScroll() {
  clearTimeout(scrollTimeout)
  scrollTimeout = setTimeout(() => {
    const scrollTop = wrapRef.value?.scrollTop ?? 0
    currentPage.value = Math.round(scrollTop / window.innerHeight)
  }, 50)
}

onBeforeUnmount(() => clearTimeout(scrollTimeout))
</script>

<style scoped>
/* ── 전체 컨테이너 ── */
.dashboard-wrap {
  width: 100%;
  height: 100vh;
  overflow-y: scroll;
  scroll-snap-type: y mandatory;
  scrollbar-width: none;
  position: relative;
}
.dashboard-wrap::-webkit-scrollbar { display: none; }

/* ── 페이지 공통 ── */
.page {
  width: 100%;
  height: 100vh;
  scroll-snap-align: start;
  position: relative;
  background-color: #1a1a1a; /* 배경.png 교체 예정 */
  background-size: cover;
  background-position: center;
  overflow: hidden;
}

/* ── 팀 로고 (placeholder) ── */
.team-logo {
  position: absolute;
  left: 50%;
  top: 178px;
  transform: translateX(-50%);
  width: 622px;
  height: 278px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-placeholder {
  font-family: 'ABeeZee', sans-serif;
  font-size: 72px;
  font-weight: 400;
  color: #FFFFFF;
  text-align: center;
  line-height: 1.1;
  letter-spacing: 8px;
  /* 배경 이미지 생기면 이 div 제거 */
  border: 2px dashed rgba(255,255,255,0.2);
  padding: 24px 48px;
  border-radius: 8px;
}

/* ── 고정 헤더 ── */
.dash-header {
  position: fixed;
  top: 26px;
  right: 18px;
  z-index: 100;
}

.header-auth {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 12px;
}

.btn-dash {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 83px;
  height: 32px;
  border-radius: 8px;
  font-family: 'Inter', sans-serif;
  font-size: 16px;
  font-weight: 400;
  cursor: pointer;
  border: none;
  transition: opacity 0.2s;
}
.btn-dash:hover { opacity: 0.85; }

.btn-login {
  background: #E3E3E3;
  border: 1px solid #767676 !important;
  color: #1E1E1E;
}

.btn-signup {
  background: #2C2C2C;
  border: 1px solid #2C2C2C !important;
  color: #F5F5F5;
}

.header-avatar {
  width: 76px;
  height: 76px;
  border-radius: 50%;
  background: #2C2C2C;
  border: 2px solid #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Roboto', sans-serif;
  font-size: 28px;
  font-weight: 500;
  color: #FFFFFF;
  cursor: pointer;
  transition: opacity 0.2s;
  position: relative;
  top: -10px; /* Figma: top 16px */
}
.header-avatar:hover { opacity: 0.85; }

/* ── 고정 하단 네비게이션 ── */
.page-nav {
  position: fixed;
  bottom: 68px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 40px;
  z-index: 100;
}

.nav-dots {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: transparent;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50px;
  background: #FFFFFF;
  opacity: 0.3;
  cursor: pointer;
  transition: opacity 0.3s;
}
.dot.active { opacity: 1; }

.nav-arrow {
  width: 32px;
  height: 32px;
  background: transparent;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.arrow {
  display: block;
  width: 14px;
  height: 14px;
  border-top: 3px solid #FFFFFF;
  border-right: 3px solid #FFFFFF;
}
.arrow-left  { transform: rotate(-135deg); margin-left: 4px; }
.arrow-right { transform: rotate(45deg);  margin-right: 4px; }
</style>
