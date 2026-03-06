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
    <section class="page page-1" :style="{ backgroundImage: `url(${dashBg1})` }">
      <div class="team-logo">
        <img :src="logoImg" alt="RIMU GAMES" class="team-logo-img" />
      </div>

      <div class="team-intro">
        <p class="intro-text">
          RIMU GAMES는 원하는 게임 개발에<br>함께하는 인디 게임 팀입니다.
        </p>
      </div>

      <div class="team-members">
        <div v-for="group in memberGroups" :key="group.label" class="member-col">
          <span class="col-label">{{ group.label }}</span>
          <ul class="member-list">
            <li
              v-for="member in group.members"
              :key="member.userId"
              class="member-name"
            >{{ member.name || member.nickname }}</li>
          </ul>
        </div>
      </div>
    </section>

    <!-- ── Page 2: 프로젝트 카드 ── -->
    <section class="page page-2" :style="{ backgroundImage: `url(${dashBg2})` }">
      <div class="projects-grid">
        <div
          v-for="project in projects"
          :key="project.id"
          class="project-card"
          :class="{ selected: selectedProject?.id === project.id }"
          @click="selectProject(project)"
        >
          <div
            class="card-thumb"
            :style="project.thumbnailUrl ? { backgroundImage: `url(${project.thumbnailUrl})` } : {}"
          >
            <div class="card-overlay"></div>
            <div class="card-info">
              <span v-if="project.status" class="card-status">{{ project.status }}</span>
              <h3 class="card-title">{{ project.title }}</h3>
              <p class="card-desc">{{ truncate(project.description, 55) }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 상세 패널 (오른쪽에서 슬라이드인) -->
      <transition name="slide-panel">
        <aside v-if="selectedProject" class="detail-panel">
          <button class="panel-close" @click="selectedProject = null">✕</button>
          <div
            class="panel-thumb"
            :style="selectedProject.thumbnailUrl ? { backgroundImage: `url(${selectedProject.thumbnailUrl})` } : {}"
          ></div>
          <div class="panel-body">
            <span v-if="selectedProject.status" class="panel-status">{{ selectedProject.status }}</span>
            <h2 class="panel-title">{{ selectedProject.title }}</h2>
            <p class="panel-desc">{{ selectedProject.description }}</p>
            <div v-if="selectedProject.techStack" class="panel-tech">
              <span class="tech-label">TECH</span>
              <span class="tech-value">{{ selectedProject.techStack }}</span>
            </div>
          </div>
        </aside>
      </transition>
    </section>

    <!-- ── Page 3: 연락처 ── -->
    <section class="page page-3" :style="{ backgroundImage: `url(${dashBg1})` }">
      <div class="contact-wrap">

        <!-- 좌측: 헤딩 -->
        <div class="contact-left">
          <p class="contact-eyebrow">GET IN TOUCH</p>
          <h2 class="contact-heading">CONTACT</h2>
          <p class="contact-sub">함께하고 싶으신가요?<br>팀원들에게 연락해 보세요.</p>
        </div>

        <!-- 수직 구분선 -->
        <div class="contact-divider"></div>

        <!-- 우측: 팀원 연락처 목록 -->
        <div class="contact-right">
          <div
            v-for="member in contactMembers"
            :key="member.userId"
            class="contact-card"
          >
            <div class="contact-name-row">
              <span class="contact-name">{{ member.name || member.nickname }}</span>
              <span class="contact-role">{{ member.role }}</span>
            </div>
            <div class="contact-links">
              <a
                v-if="member.email"
                :href="`mailto:${member.email}`"
                class="contact-link"
                title="Email"
              >✉ {{ member.email }}</a>
              <a
                v-if="member.githubUrl"
                :href="member.githubUrl"
                target="_blank"
                rel="noopener"
                class="contact-link"
                title="GitHub"
              >⌥ {{ member.githubUrl }}</a>
              <a
                v-if="member.websiteUrl"
                :href="member.websiteUrl"
                target="_blank"
                rel="noopener"
                class="contact-link"
                title="Website"
              >⊕ {{ member.websiteUrl }}</a>
              <span
                v-if="member.discordTag"
                class="contact-link"
                title="Discord"
              >◈ {{ member.discordTag }}</span>
            </div>
          </div>
          <p v-if="contactMembers.length === 0" class="contact-empty">
            아직 연락처 정보가 없습니다.
          </p>
        </div>

      </div>
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
import { dashboardApi } from '../../api/dashboard.js'
import dashBg1 from '../../pubic/image/dashdoard-1.png'
import dashBg2 from '../../pubic/image/dashdoard-2.png'
import logoImg from '../../pubic/image/logo-1.png'

const router = useRouter()
const authStore = useAuthStore()
const wrapRef = ref(null)
const currentPage = ref(0)
const teamMembers = ref([])
const projects = ref([])
const selectedProject = ref(null)

function selectProject(project) {
  selectedProject.value = selectedProject.value?.id === project.id ? null : project
}

function truncate(str, max) {
  if (!str) return ''
  return str.length > max ? str.slice(0, max) + '…' : str
}

const ROLE_KEYWORDS = {
  기획: ['기획', 'pd', '플래너', 'planner', 'producer'],
  개발: ['개발', 'developer', '프로그래머', 'programmer', 'engineer'],
  아트: ['아트', 'artist', '디자이너', 'designer', 'graphic', '아티스트'],
}

function classifyRole(role) {
  if (!role) return null
  const lower = role.toLowerCase()
  for (const [cat, keywords] of Object.entries(ROLE_KEYWORDS)) {
    if (keywords.some(k => lower.includes(k))) return cat
  }
  return null
}

const memberGroups = computed(() => {
  const groups = { 기획: [], 개발: [], 아트: [] }
  for (const m of teamMembers.value) {
    const cat = classifyRole(m.role) ?? '개발'
    groups[cat].push(m)
  }
  return Object.entries(groups).map(([label, members]) => ({ label, members }))
})

const contactMembers = computed(() =>
  teamMembers.value.filter(m => m.email || m.githubUrl || m.websiteUrl || m.discordTag)
)

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

onMounted(async () => {
  try {
    const [members, projs] = await Promise.all([
      dashboardApi.getTeamMembers(),
      dashboardApi.getProjects(),
    ])
    teamMembers.value = members
    projects.value = projs
  } catch (e) {
    console.error('대시보드 데이터 불러오기 실패', e)
  }
})

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

/* ── 팀 로고 ── */
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

.team-logo-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
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

/* ── Page 1: 소개 문구 ── */
.team-intro {
  position: absolute;
  top: 496px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  white-space: nowrap;
}

.intro-text {
  font-family: 'Roboto', sans-serif;
  font-size: 36px;
  font-weight: 400;
  color: #FFFFFF;
  line-height: 1.6;
  margin: 0;
}

/* ── Page 1: 팀원 3열 ── */
.team-members {
  position: absolute;
  bottom: 160px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 120px;
  align-items: flex-start;
}

.member-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  min-width: 140px;
}

.col-label {
  font-family: 'Inter', sans-serif;
  font-size: 12px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.45);
  text-transform: uppercase;
  letter-spacing: 2.5px;
}

.member-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.member-name {
  font-family: 'ABeeZee', sans-serif;
  font-size: 19px;
  font-weight: 400;
  color: #FFFFFF;
}

/* ══════════════════════════════
   Page 2: 프로젝트 카드
══════════════════════════════ */
.page-2 {
  display: flex;
  align-items: center;
  justify-content: center;
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 24px;
  width: calc(100% - 80px);
  max-width: 1600px;
  padding: 0 40px;
  max-height: calc(100vh - 160px);
  overflow-y: auto;
  scrollbar-width: none;
}
.projects-grid::-webkit-scrollbar { display: none; }

/* ── 카드 ── */
.project-card {
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border-color 0.25s, transform 0.2s;
}
.project-card:hover       { transform: translateY(-4px); }
.project-card.selected    { border-color: #6750A4; }

.card-thumb {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
  background-color: #000;
  background-size: cover;
  background-position: center;
}

.card-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to bottom, transparent 30%, rgba(0, 0, 0, 0.85) 100%);
}

.card-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px 20px 20px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.card-status {
  font-family: 'Inter', sans-serif;
  font-size: 11px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.55);
  text-transform: uppercase;
  letter-spacing: 1.5px;
}

.card-title {
  font-family: 'ABeeZee', sans-serif;
  font-size: 19px;
  font-weight: 400;
  color: #FFFFFF;
  margin: 0;
}

.card-desc {
  font-family: 'Space Grotesk', sans-serif;
  font-size: 13px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
  line-height: 1.4;
}

/* ── 상세 패널 ── */
.detail-panel {
  position: absolute;
  top: 0;
  right: 0;
  width: 440px;
  height: 100%;
  background: #111111;
  border-left: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  scrollbar-width: none;
  z-index: 10;
}
.detail-panel::-webkit-scrollbar { display: none; }

.panel-close {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.08);
  border: none;
  border-radius: 50%;
  color: #FFFFFF;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  z-index: 1;
}
.panel-close:hover { background: rgba(255, 255, 255, 0.18); }

.panel-thumb {
  width: 100%;
  aspect-ratio: 16 / 9;
  background-color: #1a1a1a;
  background-size: cover;
  background-position: center;
  flex-shrink: 0;
}

.panel-body {
  padding: 28px 32px 40px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.panel-status {
  font-family: 'Inter', sans-serif;
  font-size: 11px;
  font-weight: 600;
  color: #6750A4;
  text-transform: uppercase;
  letter-spacing: 1.5px;
}

.panel-title {
  font-family: 'ABeeZee', sans-serif;
  font-size: 26px;
  font-weight: 400;
  color: #FFFFFF;
  margin: 0;
  line-height: 1.3;
}

.panel-desc {
  font-family: 'Space Grotesk', sans-serif;
  font-size: 15px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.75);
  line-height: 1.7;
  margin: 0;
}

.panel-tech {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-top: 8px;
}

.tech-label {
  font-family: 'Inter', sans-serif;
  font-size: 11px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.4);
  letter-spacing: 1.5px;
}

.tech-value {
  font-family: 'Space Grotesk', sans-serif;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
}

/* ══════════════════════════════
   Page 3: 연락처
══════════════════════════════ */
.page-3 {
  display: flex;
  align-items: center;
  justify-content: center;
}

.contact-wrap {
  display: flex;
  align-items: center;
  gap: 80px;
  max-width: 1200px;
  width: 100%;
  padding: 0 80px;
}

/* 좌측 헤딩 */
.contact-left {
  flex: 0 0 auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.contact-eyebrow {
  font-family: 'Inter', sans-serif;
  font-size: 11px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.4);
  letter-spacing: 3px;
  margin: 0;
}

.contact-heading {
  font-family: 'ABeeZee', sans-serif;
  font-size: 72px;
  font-weight: 400;
  color: #FFFFFF;
  letter-spacing: 6px;
  margin: 0;
  line-height: 1;
}

.contact-sub {
  font-family: 'Roboto', sans-serif;
  font-size: 18px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.55);
  line-height: 1.7;
  margin: 0;
}

/* 수직 구분선 */
.contact-divider {
  width: 1px;
  height: 280px;
  background: rgba(255, 255, 255, 0.18);
  flex-shrink: 0;
}

/* 우측 연락처 목록 */
.contact-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 32px;
  max-height: calc(100vh - 200px);
  overflow-y: auto;
  scrollbar-width: none;
}
.contact-right::-webkit-scrollbar { display: none; }

.contact-card {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.contact-name-row {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.contact-name {
  font-family: 'ABeeZee', sans-serif;
  font-size: 20px;
  font-weight: 400;
  color: #FFFFFF;
}

.contact-role {
  font-family: 'Inter', sans-serif;
  font-size: 12px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.4);
  letter-spacing: 1px;
}

.contact-links {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.contact-link {
  font-family: 'Space Grotesk', sans-serif;
  font-size: 14px;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.65);
  text-decoration: none;
  transition: color 0.2s;
}
a.contact-link:hover { color: #FFFFFF; }

.contact-empty {
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.3);
  margin: 0;
}

/* ── 슬라이드 트랜지션 ── */
.slide-panel-enter-active,
.slide-panel-leave-active {
  transition: transform 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}
.slide-panel-enter-from,
.slide-panel-leave-to  { transform: translateX(100%); }
.slide-panel-enter-to,
.slide-panel-leave-from { transform: translateX(0); }
</style>
