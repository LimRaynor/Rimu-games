<template>
  <div class="profile-page">

    <!-- ── 사이드바 ── -->
    <aside class="sidebar">
      <div class="sidebar-top">
        <div class="avatar-wrap">
          <img v-if="profileForm.avatarUrl" :src="profileForm.avatarUrl" alt="avatar" class="avatar" />
          <div v-else class="avatar-placeholder">{{ userInitial }}</div>
        </div>
        <p class="greeting">{{ userName }}님 환영합니다</p>
      </div>

      <nav class="sidebar-nav">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          class="sidebar-tab"
          :class="{ active: activeTab === tab.key }"
          @click="activeTab = tab.key"
        >
          {{ tab.label }}
        </button>
      </nav>

      <div class="sidebar-bottom">
        <button class="icon-btn" @click="saveProfile" :disabled="saving" title="저장">
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/>
            <polyline points="17 21 17 13 7 13 7 21"/>
            <polyline points="7 3 7 8 15 8"/>
          </svg>
          <span>저장</span>
        </button>
        <button class="icon-btn logout-btn" @click="handleLogout" title="로그아웃">
          <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
            <polyline points="16 17 21 12 16 7"/>
            <line x1="21" y1="12" x2="9" y2="12"/>
          </svg>
          <span>로그아웃</span>
        </button>
      </div>
    </aside>

    <!-- ── 컨텐츠 ── -->
    <main class="content">
      <transition name="toast">
        <div v-if="saveSuccess" class="toast">저장되었습니다!</div>
      </transition>

      <!-- Tab: 기본정보 -->
      <div v-if="activeTab === 'basic'" class="tab-pane">
        <h2 class="tab-title">기본정보</h2>
        <div class="field-group">
          <label class="field-label">이름</label>
          <input v-model="profileForm.name" type="text" placeholder="이름을 입력하세요" class="field-input" />
        </div>
        <div class="field-group">
          <label class="field-label">역할</label>
          <input v-model="profileForm.role" type="text" placeholder="ex) 게임 개발자, 2D 아티스트" class="field-input" />
        </div>
        <div class="field-group">
          <label class="field-label">자기소개</label>
          <textarea v-model="profileForm.bio" rows="5" placeholder="자신을 소개해주세요" class="field-input"></textarea>
        </div>
        <div class="field-group">
          <label class="field-label">아바타 URL</label>
          <input v-model="profileForm.avatarUrl" type="url" placeholder="https://..." class="field-input" />
        </div>
      </div>

      <!-- Tab: 프로젝트 -->
      <div v-if="activeTab === 'project'" class="tab-pane">
        <h2 class="tab-title">프로젝트</h2>

        <div class="project-strip">
          <div
            v-for="project in projectStore.projects"
            :key="project.id"
            class="project-card-thumb"
            :class="{ selected: selectedProject?.id === project.id }"
            @click="selectProject(project)"
          >
            <div class="thumb-img">
              <img v-if="project.thumbnailUrl" :src="project.thumbnailUrl" alt="thumb" />
              <div v-else class="thumb-placeholder">🎮</div>
            </div>
            <p class="thumb-title">{{ project.title }}</p>
          </div>
          <button class="add-card-btn" @click="newProject" title="프로젝트 추가">
            <span>+</span>
          </button>
        </div>

        <div v-if="showProjectForm" class="project-edit-form">
          <h3 class="edit-form-title">{{ isNewProject ? '새 프로젝트' : '프로젝트 편집' }}</h3>
          <form @submit.prevent="saveProject">
            <div class="form-row-2">
              <div class="field-group">
                <label class="field-label">이름 *</label>
                <input v-model="projectForm.title" type="text" placeholder="프로젝트 이름" class="field-input" required />
              </div>
              <div class="field-group">
                <label class="field-label">버전 / 상태</label>
                <input v-model="projectForm.status" type="text" placeholder="ex) v1.0, 진행중, 완성" class="field-input" />
              </div>
            </div>
            <div class="field-group">
              <label class="field-label">태그 (기술스택)</label>
              <input v-model="projectForm.techStack" type="text" placeholder="ex) Unity, C#, 2D 플랫포머" class="field-input" />
            </div>
            <div class="field-group">
              <label class="field-label">세부설명</label>
              <textarea v-model="projectForm.description" rows="4" placeholder="프로젝트를 자세히 설명해주세요" class="field-input"></textarea>
            </div>
            <div class="form-row-2">
              <div class="field-group">
                <label class="field-label">썸네일 URL</label>
                <input v-model="projectForm.thumbnailUrl" type="url" placeholder="https://..." class="field-input" />
              </div>
              <div class="field-group">
                <label class="field-label">영상 / 링크</label>
                <input v-model="projectForm.projectUrl" type="url" placeholder="https://..." class="field-input" />
              </div>
            </div>
            <div class="field-group">
              <label class="field-label">GitHub URL</label>
              <input v-model="projectForm.repoUrl" type="url" placeholder="https://github.com/..." class="field-input" />
            </div>
            <div class="project-form-actions">
              <button type="submit" class="btn-save-project" :disabled="projectSaving">
                {{ projectSaving ? '저장 중...' : '저장' }}
              </button>
              <button v-if="!isNewProject" type="button" class="btn-delete-project" @click="deleteProject">
                삭제
              </button>
              <button type="button" class="btn-cancel-project" @click="cancelProjectEdit">
                취소
              </button>
            </div>
          </form>
        </div>

        <div v-else class="project-empty-hint">
          <p>프로젝트를 선택하거나 + 버튼으로 추가하세요</p>
        </div>
      </div>

      <!-- Tab: 연락처 -->
      <div v-if="activeTab === 'contact'" class="tab-pane">
        <h2 class="tab-title">연락처</h2>
        <div class="field-group">
          <label class="field-label">공개 이메일</label>
          <input v-model="profileForm.email" type="email" placeholder="공개할 이메일 주소" class="field-input" />
        </div>
        <div class="field-group">
          <label class="field-label">GitHub URL</label>
          <input v-model="profileForm.githubUrl" type="url" placeholder="https://github.com/..." class="field-input" />
        </div>
        <div class="field-group">
          <label class="field-label">개인 웹사이트</label>
          <input v-model="profileForm.websiteUrl" type="url" placeholder="https://..." class="field-input" />
        </div>
        <div class="field-group">
          <label class="field-label">Discord 태그</label>
          <input v-model="profileForm.discordTag" type="text" placeholder="username#1234" class="field-input" />
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth.js'
import { useProfileStore } from '../../stores/profile.js'
import { useProjectStore } from '../../stores/project.js'
import { authApi } from '../../api/auth.js'

const router = useRouter()
const authStore = useAuthStore()
const profileStore = useProfileStore()
const projectStore = useProjectStore()

// ── 탭 ──
const activeTab = ref('basic')
const tabs = [
  { key: 'basic', label: '기본정보' },
  { key: 'project', label: '프로젝트' },
  { key: 'contact', label: '연락처' },
]

// ── 사용자 정보 ──
const userName = computed(() => authStore.user?.nickname || 'User')
const userInitial = computed(() => userName.value.charAt(0).toUpperCase())

// ── 프로필 저장 상태 ──
const saving = ref(false)
const saveSuccess = ref(false)

// ── 프로필 폼 ──
const profileForm = reactive({
  name: '',
  role: '',
  bio: '',
  avatarUrl: '',
  email: '',
  githubUrl: '',
  websiteUrl: '',
  discordTag: '',
})

// ── 프로젝트 상태 ──
const selectedProject = ref(null)
const isNewProject = ref(false)
const projectSaving = ref(false)
const showProjectForm = computed(() => selectedProject.value !== null || isNewProject.value)

const projectForm = reactive({
  title: '',
  description: '',
  techStack: '',
  status: '',
  projectUrl: '',
  repoUrl: '',
  thumbnailUrl: '',
})

// ── 초기 로드 ──
onMounted(async () => {
  try {
    await profileStore.fetchMyProfile()
    if (profileStore.profile) {
      Object.assign(profileForm, profileStore.profile)
    }
  } catch {}
  await projectStore.fetchMyProjects()
})

// ── 프로필 저장 ──
async function saveProfile() {
  saving.value = true
  saveSuccess.value = false
  try {
    await profileStore.updateProfile({ ...profileForm })
    saveSuccess.value = true
    setTimeout(() => { saveSuccess.value = false }, 3000)
  } finally {
    saving.value = false
  }
}

// ── 로그아웃 ──
async function handleLogout() {
  try {
    await authApi.logout()
  } catch {}
  authStore.logout()
  router.push('/login')
}

// ── 프로젝트: 선택 ──
function selectProject(project) {
  isNewProject.value = false
  selectedProject.value = project
  Object.assign(projectForm, {
    title: project.title || '',
    description: project.description || '',
    techStack: project.techStack || '',
    status: project.status || '',
    projectUrl: project.projectUrl || '',
    repoUrl: project.repoUrl || '',
    thumbnailUrl: project.thumbnailUrl || '',
  })
}

// ── 프로젝트: 새로 만들기 ──
function newProject() {
  selectedProject.value = null
  isNewProject.value = true
  Object.keys(projectForm).forEach(k => { projectForm[k] = '' })
}

// ── 프로젝트: 취소 ──
function cancelProjectEdit() {
  selectedProject.value = null
  isNewProject.value = false
}

// ── 프로젝트: 저장 ──
async function saveProject() {
  projectSaving.value = true
  try {
    if (isNewProject.value) {
      const created = await projectStore.createProject({ ...projectForm })
      selectedProject.value = created
      isNewProject.value = false
    } else {
      const updated = await projectStore.updateProject(selectedProject.value.id, { ...projectForm })
      selectedProject.value = updated
    }
  } catch (e) {
    console.error('프로젝트 저장 실패:', e)
  } finally {
    projectSaving.value = false
  }
}

// ── 프로젝트: 삭제 ──
async function deleteProject() {
  if (!confirm('프로젝트를 삭제하시겠습니까?')) return
  try {
    await projectStore.deleteProject(selectedProject.value.id)
    cancelProjectEdit()
  } catch (e) {
    console.error('프로젝트 삭제 실패:', e)
  }
}
</script>

<style scoped>
/* ── 페이지 레이아웃 ── */
.profile-page {
  display: flex;
  width: 100%;
  min-height: 100vh;
}

/* ── 사이드바 ── */
.sidebar {
  width: 444px;
  min-width: 444px;
  background: #1C1C1E;
  display: flex;
  flex-direction: column;
  padding: 48px 32px;
  position: sticky;
  top: 0;
  height: 100vh;
  overflow-y: auto;
}

.sidebar-top {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 48px;
}

.avatar-wrap {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  margin-bottom: 20px;
  border: 3px solid #6750A4;
  flex-shrink: 0;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #6750A4;
  color: #FFFFFF;
  font-family: 'Space Grotesk', sans-serif;
  font-size: 48px;
  font-weight: 600;
}

.greeting {
  font-family: 'Roboto', sans-serif;
  font-size: 18px;
  color: #FFFFFF;
  text-align: center;
}

/* ── 탭 메뉴 ── */
.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 6px;
  flex: 1;
}

.sidebar-tab {
  width: 100%;
  padding: 16px 20px;
  border-radius: 8px;
  border: none;
  background: transparent;
  color: #9E9E9E;
  font-family: 'Roboto', sans-serif;
  font-size: 17px;
  font-weight: 400;
  text-align: left;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}

.sidebar-tab:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #FFFFFF;
}

.sidebar-tab.active {
  background: #F2F2F7;
  color: #1E1E1E;
  font-weight: 500;
}

/* ── 사이드바 하단 ── */
.sidebar-bottom {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.icon-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  padding: 14px 20px;
  border-radius: 8px;
  border: none;
  background: transparent;
  color: #9E9E9E;
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}

.icon-btn:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #FFFFFF;
}

.icon-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.logout-btn:hover {
  background: rgba(255, 101, 132, 0.12);
  color: #ff6584;
}

/* ── 컨텐츠 ── */
.content {
  flex: 1;
  background: #FFFFFF;
  padding: 60px 80px;
  overflow-y: auto;
  position: relative;
  color: #1E1E1E;
}

/* ── 토스트 ── */
.toast {
  position: fixed;
  top: 32px;
  right: 40px;
  background: #6750A4;
  color: #FFFFFF;
  padding: 14px 28px;
  border-radius: 10px;
  font-size: 15px;
  font-family: 'Roboto', sans-serif;
  z-index: 9999;
  box-shadow: 0 4px 20px rgba(103, 80, 164, 0.4);
}

.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-12px);
}

/* ── 탭 공통 ── */
.tab-pane {
  max-width: 800px;
}

.tab-title {
  font-family: 'Space Grotesk', sans-serif;
  font-size: 28px;
  font-weight: 600;
  color: #1E1E1E;
  margin-bottom: 40px;
}

/* ── 필드 ── */
.field-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 28px;
}

.field-label {
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  font-weight: 500;
  color: #4A4A4A;
}

.field-input {
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
  font-weight: 400;
  color: #1E1E1E;
  background: #FFFFFF;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  padding: 14px 18px;
  width: 100%;
  outline: none;
  transition: border-color 0.2s;
}

.field-input:focus {
  border-color: #6750A4;
}

textarea.field-input {
  resize: vertical;
  min-height: 120px;
}

/* ── 프로젝트 카드 스트립 ── */
.project-strip {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding-bottom: 16px;
  margin-bottom: 40px;
  scrollbar-width: thin;
  max-width: 100%;
}

.project-card-thumb {
  flex-shrink: 0;
  width: 140px;
  cursor: pointer;
  border-radius: 12px;
  overflow: hidden;
  border: 3px solid transparent;
  transition: border-color 0.2s;
}

.project-card-thumb.selected {
  border-color: #6750A4;
}

.thumb-img {
  width: 100%;
  height: 100px;
  background: #1a1a1a;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.thumb-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb-placeholder {
  font-size: 32px;
}

.thumb-title {
  background: #F5F5F5;
  padding: 8px 10px;
  font-size: 13px;
  color: #1E1E1E;
  font-family: 'Roboto', sans-serif;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.add-card-btn {
  flex-shrink: 0;
  width: 140px;
  height: 140px;
  border-radius: 12px;
  border: 2px dashed #D9D9D9;
  background: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #9E9E9E;
  font-size: 36px;
  transition: border-color 0.2s, color 0.2s;
}

.add-card-btn:hover {
  border-color: #6750A4;
  color: #6750A4;
}

/* ── 프로젝트 편집 폼 ── */
.project-edit-form {
  background: #F9F9F9;
  border-radius: 16px;
  padding: 40px;
  max-width: 900px;
}

.edit-form-title {
  font-family: 'Space Grotesk', sans-serif;
  font-size: 20px;
  font-weight: 600;
  color: #1E1E1E;
  margin-bottom: 28px;
}

.form-row-2 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.project-form-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.btn-save-project {
  padding: 12px 32px;
  background: #6750A4;
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-family: 'Roboto', sans-serif;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-save-project:hover { opacity: 0.85; }
.btn-save-project:disabled { opacity: 0.5; cursor: not-allowed; }

.btn-delete-project {
  padding: 12px 24px;
  background: transparent;
  color: #ff6584;
  border: 1px solid #ff6584;
  border-radius: 8px;
  font-size: 15px;
  font-family: 'Roboto', sans-serif;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-delete-project:hover { opacity: 0.75; }

.btn-cancel-project {
  padding: 12px 24px;
  background: transparent;
  color: #9E9E9E;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  font-size: 15px;
  font-family: 'Roboto', sans-serif;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-cancel-project:hover { opacity: 0.75; }

.project-empty-hint {
  padding: 60px;
  text-align: center;
  color: #9E9E9E;
  font-family: 'Roboto', sans-serif;
  font-size: 16px;
}
</style>
