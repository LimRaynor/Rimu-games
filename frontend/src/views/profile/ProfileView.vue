<template>
  <div class="profile-page">
    <div class="profile-header">
      <h1>마이페이지</h1>
    </div>

    <!-- 탭 네비게이션 -->
    <div class="tab-nav">
      <button
        v-for="tab in tabs"
        :key="tab.key"
        class="tab-btn"
        :class="{ active: activeTab === tab.key }"
        @click="activeTab = tab.key"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- Basic Info 탭 -->
    <div v-if="activeTab === 'basic'" class="tab-content card">
      <h2>Basic Info</h2>
      <form @submit.prevent="saveBasicInfo">
        <div class="form-group">
          <label>이름</label>
          <input v-model="profileForm.name" type="text" placeholder="이름을 입력하세요" />
        </div>
        <div class="form-group">
          <label>역할</label>
          <input v-model="profileForm.role" type="text" placeholder="ex) Game Developer, 2D Artist" />
        </div>
        <div class="form-group">
          <label>자기소개</label>
          <textarea v-model="profileForm.bio" rows="4" placeholder="자신을 소개해주세요"></textarea>
        </div>
        <div class="form-group">
          <label>프로필 이미지 URL</label>
          <input v-model="profileForm.avatarUrl" type="url" placeholder="https://..." />
        </div>
        <p v-if="saveSuccess" class="success-message">저장되었습니다!</p>
        <button type="submit" class="btn btn-primary" :disabled="saving">
          {{ saving ? '저장 중...' : '저장' }}
        </button>
      </form>
    </div>

    <!-- Project 탭 -->
    <div v-if="activeTab === 'project'" class="tab-content">
      <div class="section-header">
        <h2>Project</h2>
        <button class="btn btn-primary" @click="showProjectForm = true">+ 추가</button>
      </div>

      <!-- 프로젝트 추가/수정 폼 -->
      <div v-if="showProjectForm" class="card project-form">
        <h3>{{ editingProject ? '프로젝트 수정' : '새 프로젝트' }}</h3>
        <form @submit.prevent="saveProject">
          <div class="form-group">
            <label>프로젝트 이름 *</label>
            <input v-model="projectForm.title" type="text" placeholder="프로젝트 이름" required />
          </div>
          <div class="form-group">
            <label>설명</label>
            <textarea v-model="projectForm.description" rows="3" placeholder="프로젝트 설명"></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>기술 스택</label>
              <input v-model="projectForm.techStack" type="text" placeholder="ex) Unity, C#, Photoshop" />
            </div>
            <div class="form-group">
              <label>상태</label>
              <input v-model="projectForm.status" type="text" placeholder="ex) 완성, 진행중" />
            </div>
          </div>
          <div class="form-group">
            <label>프로젝트 URL</label>
            <input v-model="projectForm.projectUrl" type="url" placeholder="https://..." />
          </div>
          <div class="form-group">
            <label>깃허브 URL</label>
            <input v-model="projectForm.repoUrl" type="url" placeholder="https://github.com/..." />
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary">저장</button>
            <button type="button" class="btn btn-secondary" @click="cancelProjectForm">취소</button>
          </div>
        </form>
      </div>

      <!-- 프로젝트 목록 -->
      <div v-if="projectStore.loading" class="loading">불러오는 중...</div>
      <div v-else-if="projectStore.projects.length === 0" class="empty">
        <p>등록된 프로젝트가 없습니다.</p>
      </div>
      <div v-else class="project-list">
        <div v-for="project in projectStore.projects" :key="project.id" class="card project-item">
          <div class="project-info">
            <h3>{{ project.title }}</h3>
            <p v-if="project.role" class="project-status">{{ project.status }}</p>
            <p v-if="project.description" class="project-desc">{{ project.description }}</p>
            <div v-if="project.techStack" class="tech-stack">
              <span
                v-for="tech in project.techStack.split(',')"
                :key="tech"
                class="tech-badge"
              >{{ tech.trim() }}</span>
            </div>
          </div>
          <div class="project-actions">
            <button class="btn btn-secondary" @click="editProject(project)">수정</button>
            <button class="btn btn-danger" @click="removeProject(project.id)">삭제</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Contact 탭 -->
    <div v-if="activeTab === 'contact'" class="tab-content card">
      <h2>Contact</h2>
      <form @submit.prevent="saveContact">
        <div class="form-group">
          <label>공개 이메일</label>
          <input v-model="profileForm.email" type="email" placeholder="공개할 이메일 주소" />
        </div>
        <div class="form-group">
          <label>GitHub URL</label>
          <input v-model="profileForm.githubUrl" type="url" placeholder="https://github.com/..." />
        </div>
        <div class="form-group">
          <label>개인 웹사이트</label>
          <input v-model="profileForm.websiteUrl" type="url" placeholder="https://..." />
        </div>
        <div class="form-group">
          <label>Discord 태그</label>
          <input v-model="profileForm.discordTag" type="text" placeholder="username#1234" />
        </div>
        <p v-if="saveSuccess" class="success-message">저장되었습니다!</p>
        <button type="submit" class="btn btn-primary" :disabled="saving">
          {{ saving ? '저장 중...' : '저장' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useProfileStore } from '../../stores/profile.js'
import { useProjectStore } from '../../stores/project.js'

const profileStore = useProfileStore()
const projectStore = useProjectStore()

const activeTab = ref('basic')
const tabs = [
  { key: 'basic', label: 'Basic Info' },
  { key: 'project', label: 'Project' },
  { key: 'contact', label: 'Contact' },
]

const saving = ref(false)
const saveSuccess = ref(false)
const showProjectForm = ref(false)
const editingProject = ref(null)

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

const projectForm = reactive({
  title: '',
  description: '',
  techStack: '',
  status: '',
  projectUrl: '',
  repoUrl: '',
  thumbnailUrl: '',
})

onMounted(async () => {
  try {
    await profileStore.fetchMyProfile()
    if (profileStore.profile) {
      Object.assign(profileForm, profileStore.profile)
    }
  } catch {}

  await projectStore.fetchMyProjects()
})

async function saveBasicInfo() {
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

async function saveContact() {
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

function editProject(project) {
  editingProject.value = project
  Object.assign(projectForm, project)
  showProjectForm.value = true
}

function cancelProjectForm() {
  showProjectForm.value = false
  editingProject.value = null
  Object.keys(projectForm).forEach(k => { projectForm[k] = '' })
}

async function saveProject() {
  try {
    if (editingProject.value) {
      await projectStore.updateProject(editingProject.value.id, { ...projectForm })
    } else {
      await projectStore.createProject({ ...projectForm })
    }
    cancelProjectForm()
  } catch (e) {
    console.error('프로젝트 저장 실패:', e)
  }
}

async function removeProject(projectId) {
  if (confirm('프로젝트를 삭제하시겠습니까?')) {
    await projectStore.deleteProject(projectId)
  }
}
</script>

<style scoped>
.profile-header {
  margin-bottom: 24px;
}

.profile-header h1 {
  font-size: 28px;
  font-weight: 700;
}

.tab-nav {
  display: flex;
  gap: 4px;
  margin-bottom: 24px;
  border-bottom: 1px solid var(--color-border);
}

.tab-btn {
  background: none;
  border: none;
  padding: 12px 20px;
  cursor: pointer;
  color: var(--color-text-muted);
  font-size: 14px;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;
}

.tab-btn:hover {
  color: var(--color-text);
}

.tab-btn.active {
  color: var(--color-primary);
  border-bottom-color: var(--color-primary);
}

.tab-content h2 {
  font-size: 20px;
  margin-bottom: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-header h2 {
  font-size: 20px;
}

.project-form {
  margin-bottom: 20px;
}

.project-form h3 {
  font-size: 16px;
  margin-bottom: 16px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.project-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.project-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.project-info h3 {
  font-size: 16px;
  margin-bottom: 4px;
}

.project-status {
  color: var(--color-primary);
  font-size: 12px;
  margin-bottom: 4px;
}

.project-desc {
  color: var(--color-text-muted);
  font-size: 13px;
  margin-bottom: 8px;
}

.tech-stack {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tech-badge {
  background: var(--color-border);
  padding: 2px 10px;
  border-radius: 12px;
  font-size: 12px;
  color: var(--color-text-muted);
}

.project-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.success-message {
  color: #4caf50;
  font-size: 13px;
  margin-bottom: 8px;
}

.loading, .empty {
  text-align: center;
  padding: 40px 0;
  color: var(--color-text-muted);
}
</style>
