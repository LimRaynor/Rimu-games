<template>
  <div class="profile-page">

    <!-- ── 사이드바 ── -->
    <aside class="sidebar">
      <div class="sidebar-block-top">
        <div class="avatar-wrap">
          <img v-if="profileForm.avatarUrl" :src="profileForm.avatarUrl" alt="avatar" class="avatar" />
          <div v-else class="avatar-placeholder">{{ userInitial }}</div>
        </div>
        <p class="greeting">{{ userName }}님, 환영합니다</p>
      </div>

      <nav class="sidebar-nav">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          class="nav-pill"
          :class="{ active: activeTab === tab.key }"
          @click="activeTab = tab.key"
        >{{ tab.label }}</button>
      </nav>

      <div class="sidebar-block-bottom">
        <button class="icon-sq-btn" @click="saveProfile" :disabled="saving" title="저장">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/>
            <polyline points="17 21 17 13 7 13 7 21"/>
            <polyline points="7 3 7 8 15 8"/>
          </svg>
        </button>
        <button class="icon-sq-btn" @click="handleLogout" title="로그아웃">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
            <polyline points="16 17 21 12 16 7"/>
            <line x1="21" y1="12" x2="9" y2="12"/>
          </svg>
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

        <!-- 이름 -->
        <div class="input-field input-field--narrow">
          <label class="input-label">이름</label>
          <input v-model="profileForm.name" type="text" class="input-box" placeholder="이름을 입력하세요" />
        </div>

        <!-- 소개글 -->
        <div class="input-field input-field--full">
          <label class="input-label">소개글</label>
          <input v-model="profileForm.bio" type="text" class="input-box" placeholder="자신을 소개해주세요" />
        </div>

        <!-- 이미지 업로드 -->
        <div class="image-row">
          <div class="image-section">
            <div class="input-label">배경 이미지</div>
            <div class="image-desc">1920*1080px 의 jpg, png, webp 이미지</div>
            <div class="split-btn-wrap">
              <button class="split-btn-lead" @click="bgImageInput.click()">
                <svg class="split-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                  <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"/>
                </svg>
                파일 선택
              </button>
              <button class="split-btn-trail">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22" height="22">
                  <path d="M7 10l5 5 5-5z"/>
                </svg>
              </button>
            </div>
            <input ref="bgImageInput" type="file" accept=".jpg,.jpeg,.png,.webp" style="display:none" />
          </div>

          <div class="image-section">
            <div class="input-label">로고 이미지</div>
            <div class="image-desc">최대 500*500px 의 jpg, png, webp 이미지</div>
            <div class="split-btn-wrap">
              <button class="split-btn-lead" @click="logoImageInput.click()">
                <svg class="split-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                  <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"/>
                </svg>
                파일 선택
              </button>
              <button class="split-btn-trail">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22" height="22">
                  <path d="M7 10l5 5 5-5z"/>
                </svg>
              </button>
            </div>
            <input ref="logoImageInput" type="file" accept=".jpg,.jpeg,.png,.webp" style="display:none" />
          </div>
        </div>

        <!-- 팀원 이름 / 역할 -->
        <div class="team-section">
          <div class="team-col">
            <h3 class="team-col-title">팀원 이름</h3>
            <div v-for="(member, i) in teamMembers" :key="'name-' + i" class="team-input-wrap">
              <input v-model="member.name" type="text" class="team-input" placeholder="이름" />
            </div>
          </div>
          <div class="team-col">
            <h3 class="team-col-title">팀원 역할</h3>
            <div v-for="(member, i) in teamMembers" :key="'role-' + i" class="team-input-wrap">
              <input v-model="member.role" type="text" class="team-input" placeholder="ex) 기획, 개발, 아트" />
            </div>
          </div>
        </div>

        <!-- 팀원 추가 버튼 -->
        <button class="add-member-bar" @click="addTeamMember">+</button>

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
              <button v-if="!isNewProject" type="button" class="btn-delete-project" @click="deleteProject">삭제</button>
              <button type="button" class="btn-cancel-project" @click="cancelProjectEdit">취소</button>
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

// ── 이미지 업로드 refs ──
const bgImageInput = ref(null)
const logoImageInput = ref(null)

// ── 팀원 목록 (첫 번째 행 = 본인 name/role) ──
const teamMembers = ref([{ name: '', role: '' }])

function addTeamMember() {
  teamMembers.value.push({ name: '', role: '' })
}

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
      teamMembers.value[0].name = profileStore.profile.name || ''
      teamMembers.value[0].role = profileStore.profile.role || ''
    }
  } catch {}
  await projectStore.fetchMyProjects()
})

// ── 프로필 저장 ──
async function saveProfile() {
  saving.value = true
  saveSuccess.value = false
  // 첫 번째 행(본인 정보)을 profileForm에 반영
  profileForm.name = teamMembers.value[0]?.name || ''
  profileForm.role = teamMembers.value[0]?.role || ''
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
  background: #FFFFFF;
  border-right: 1px solid #D9D9D9;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding: 60px 32px;
  position: sticky;
  top: 0;
  height: 100vh;
  overflow-y: auto;
  box-sizing: border-box;
}

/* 사이드바 상단 블록 */
.sidebar-block-top {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
  width: 380px;
}

.avatar-wrap {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
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
  font-family: 'Roboto', sans-serif;
  font-size: 48px;
  font-weight: 600;
}

.greeting {
  font-family: 'Roboto', sans-serif;
  font-size: 24px;
  font-weight: 400;
  line-height: 32px;
  color: #000000;
  text-align: center;
  margin: 0;
}

/* ── 탭 네비게이션 ── */
.sidebar-nav {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  width: 380px;
}

.nav-pill {
  width: 380px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px;
  border-radius: 8px;
  border: none;
  background: transparent;
  font-family: 'Roboto', sans-serif;
  font-size: 24px;
  font-weight: 400;
  line-height: 32px;
  color: #1E1E1E;
  cursor: pointer;
  transition: background 0.15s;
}

.nav-pill:hover {
  background: rgba(0, 0, 0, 0.04);
}

.nav-pill.active {
  background: #F2F2F7;
}

/* 사이드바 하단 블록 */
.sidebar-block-bottom {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 24px;
  width: 380px;
}

.icon-sq-btn {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: 2.5px solid #1E1E1E;
  border-radius: 8px;
  cursor: pointer;
  color: #1E1E1E;
  transition: opacity 0.2s;
}

.icon-sq-btn:hover {
  opacity: 0.6;
}

.icon-sq-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
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

/* ── 기본정보 탭 ── */

.tab-pane {
  width: 100%;
}

.input-field {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
  margin-bottom: 40px;
}

.input-field--narrow {
  width: 354px;
}

.input-field--full {
  width: 100%;
  max-width: 1178px;
}

.input-label {
  font-family: 'Roboto', sans-serif;
  font-size: 32px;
  font-weight: 400;
  line-height: 40px;
  color: #1E1E1E;
}

.input-box {
  width: 100%;
  height: 56px;
  padding: 12px 16px;
  background: #FFFFFF;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 24px;
  font-weight: 400;
  line-height: 32px;
  color: #1E1E1E;
  outline: none;
  box-sizing: border-box;
  transition: border-color 0.2s;
}

.input-box:focus {
  border-color: #6750A4;
}

/* ── 이미지 업로드 ── */
.image-row {
  display: flex;
  gap: 87px;
  margin-bottom: 60px;
  flex-wrap: wrap;
}

.image-section {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
  width: 354px;
}

.image-desc {
  font-family: 'Inter', sans-serif;
  font-size: 16px;
  font-weight: 400;
  line-height: 140%;
  color: #757575;
}

.split-btn-wrap {
  display: flex;
  gap: 2px;
  align-items: center;
}

.split-btn-lead {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 10px 12px 10px 16px;
  gap: 8px;
  width: 151px;
  height: 40px;
  background: #6750A4;
  color: #FFFFFF;
  border: none;
  border-radius: 20px 4px 4px 20px;
  font-family: 'Roboto', sans-serif;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.1px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.split-btn-lead:hover {
  opacity: 0.85;
}

.split-icon {
  flex-shrink: 0;
}

.split-btn-trail {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 9px 14px 9px 12px;
  width: 48px;
  height: 40px;
  background: #6750A4;
  color: #FFFFFF;
  border: none;
  border-radius: 4px 20px 20px 4px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.split-btn-trail:hover {
  opacity: 0.85;
}

/* ── 팀원 섹션 ── */
.team-section {
  display: grid;
  grid-template-columns: 382px 382px;
  gap: 16px;
  margin-bottom: 48px;
}

.team-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.team-col-title {
  width: 382px;
  font-family: 'Roboto', sans-serif;
  font-size: 28px;
  font-weight: 400;
  line-height: 36px;
  color: #1E1E1E;
  margin: 0;
  align-self: stretch;
}

.team-input-wrap {
  width: 100%;
}

.team-input {
  width: 382px;
  height: 56px;
  padding: 12px 16px;
  background: #FFFFFF;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 24px;
  font-weight: 400;
  line-height: 32px;
  color: #1E1E1E;
  outline: none;
  box-sizing: border-box;
  transition: border-color 0.2s;
}

.team-input:focus {
  border-color: #6750A4;
}

/* ── 팀원 추가 버튼 ── */
.add-member-bar {
  width: 807px;
  max-width: 100%;
  height: 33px;
  background: #D9D9D9;
  border-radius: 21px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Roboto', sans-serif;
  font-size: 32px;
  line-height: 40px;
  color: #000000;
  cursor: pointer;
  transition: background 0.2s;
}

.add-member-bar:hover {
  background: #c5c5c5;
}

/* ── 프로젝트/연락처 탭 공통 필드 ── */
.tab-title {
  font-family: 'Roboto', sans-serif;
  font-size: 28px;
  font-weight: 400;
  color: #1E1E1E;
  margin-bottom: 40px;
}

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
  box-sizing: border-box;
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
  font-family: 'Roboto', sans-serif;
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
