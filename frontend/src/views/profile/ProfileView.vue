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
          @click="tryChangeTab(tab.key)"
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
        <button class="icon-sq-btn" @click="router.push('/')" title="나가기">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
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
      <transition name="toast">
        <div v-if="autoSaveMsg" class="toast toast--auto">{{ autoSaveMsg }}</div>
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
            <div class="split-btn-wrap" style="position:relative">
              <button class="split-btn-lead" @click="triggerFileInput(bgImageInput)">
                <svg class="split-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                  <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"/>
                </svg>
                파일 선택
              </button>
              <button class="split-btn-trail" @click.stop="toggleDropdown('bg')">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22" height="22">
                  <path d="M7 10l5 5 5-5z"/>
                </svg>
              </button>
              <div v-if="activeDropdown === 'bg'" class="img-dropdown">
                <button @click="triggerFileInput(bgImageInput); activeDropdown = null">내 드라이브에서 불러오기</button>
                <button @click="openUrlModal('bg', '구글 드라이브 URL')">구글 드라이브에서 불러오기</button>
                <button @click="openUrlModal('bg', '임베드 URL')">임베드로 넣기</button>
              </div>
            </div>
            <input ref="bgImageInput" type="file" accept=".jpg,.jpeg,.png,.webp" style="display:none" @change="onFileChange($event, 'bg')" />
            <div v-if="profileForm.bgImageUrl" class="file-preview-wrap">
              <img :src="profileForm.bgImageUrl" class="file-preview-img" alt="배경 이미지 미리보기" />
              <button type="button" class="file-clear-btn" @click="profileForm.bgImageUrl = ''">선택 삭제</button>
            </div>
          </div>

          <div class="image-section">
            <div class="input-label">로고 이미지</div>
            <div class="image-desc">최대 500*500px 의 jpg, png, webp 이미지</div>
            <div class="split-btn-wrap" style="position:relative">
              <button class="split-btn-lead" @click="triggerFileInput(logoImageInput)">
                <svg class="split-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                  <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"/>
                </svg>
                파일 선택
              </button>
              <button class="split-btn-trail" @click.stop="toggleDropdown('logo')">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22" height="22">
                  <path d="M7 10l5 5 5-5z"/>
                </svg>
              </button>
              <div v-if="activeDropdown === 'logo'" class="img-dropdown">
                <button @click="triggerFileInput(logoImageInput); activeDropdown = null">내 드라이브에서 불러오기</button>
                <button @click="openUrlModal('logo', '구글 드라이브 URL')">구글 드라이브에서 불러오기</button>
                <button @click="openUrlModal('logo', '임베드 URL')">임베드로 넣기</button>
              </div>
            </div>
            <input ref="logoImageInput" type="file" accept=".jpg,.jpeg,.png,.webp" style="display:none" @change="onFileChange($event, 'logo')" />
            <div v-if="profileForm.logoImageUrl" class="file-preview-wrap">
              <img :src="profileForm.logoImageUrl" class="file-preview-img" alt="로고 이미지 미리보기" />
              <button type="button" class="file-clear-btn" @click="profileForm.logoImageUrl = ''">선택 삭제</button>
            </div>
          </div>
        </div>

        <!-- URL 입력 모달 -->
        <div v-if="urlModal.show" class="url-modal-backdrop" @click.self="urlModal.show = false">
          <div class="url-modal">
            <h3 class="url-modal-title">{{ urlModal.label }}</h3>
            <input
              v-model="urlModal.value"
              type="url"
              class="url-modal-input"
              :placeholder="urlModal.label === '구글 드라이브 URL' ? 'https://drive.google.com/file/d/...' : 'https://...'"
              @keydown.enter="confirmUrl"
            />
            <div class="url-modal-actions">
              <button class="url-modal-confirm" @click="confirmUrl">확인</button>
              <button class="url-modal-cancel" @click="urlModal.show = false">취소</button>
            </div>
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
      <div v-if="activeTab === 'project'" class="tab-pane tab-pane--project">

        <!-- 프로젝트 수 -->
        <p v-if="selectedProject && projectStore.projects.length > 0" class="proj-count-label">
          {{ selectedProjectIndex }}/{{ projectStore.projects.length }}
        </p>

        <!-- 카드 스트립 -->
        <div class="proj-strip">
          <div
            v-for="project in projectStore.projects"
            :key="project.id"
            class="proj-card"
            :class="{ selected: selectedProject?.id === project.id }"
            @click="trySelectProject(project)"
          >
            <img v-if="project.thumbnailUrl" :src="project.thumbnailUrl" class="proj-card-bg-img" />
            <div class="proj-card-gradient"></div>
            <span class="proj-card-name">{{ project.title }}</span>
            <span class="proj-card-status">{{ project.status }}</span>
            <div class="proj-card-icons">
              <button type="button" class="proj-icon-btn" @click.stop="deleteProjectFromCard(project)" title="삭제">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                  <polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6"/><path d="M14 11v6"/><path d="M9 6V4h6v2"/>
                </svg>
              </button>
            </div>
          </div>
          <div class="proj-card proj-card-add" @click="tryNewProject">
            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/>
            </svg>
            <span class="proj-card-add-label">추가</span>
          </div>
        </div>

        <!-- 편집 폼 -->
        <div v-if="showProjectForm" class="proj-form">

          <div class="proj-form-row2">
            <div class="input-field">
              <label class="input-label">이름</label>
              <input v-model="projectForm.title" type="text" class="input-box" placeholder="프로젝트 이름" />
            </div>
            <div class="input-field">
              <label class="input-label">버전 / 상태</label>
              <input v-model="projectForm.status" type="text" class="input-box" placeholder="ex) v1.0, 진행중, 완성" />
            </div>
          </div>

          <div class="input-field input-field--wide">
            <label class="input-label">태그 (기술스택)</label>
            <div class="image-desc">예) Unity, C#, 2D 플랫포머</div>
            <input v-model="projectForm.techStack" type="text" class="input-box" placeholder="Unity, C#..." />
          </div>

          <div class="image-row">
            <div class="image-section">
              <div class="input-label">카드 썸네일</div>
              <div class="image-desc">348*212px 의 jpg, png, webp 이미지</div>
              <div class="split-btn-wrap" style="position:relative">
                <button class="split-btn-lead" type="button" @click="triggerFileInput(projThumbInput)">
                  <svg class="split-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                    <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"/>
                  </svg>
                  파일 선택
                </button>
                <button class="split-btn-trail" type="button" @click.stop="toggleDropdown('projThumb')">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22" height="22"><path d="M7 10l5 5 5-5z"/></svg>
                </button>
                <div v-if="activeDropdown === 'projThumb'" class="img-dropdown">
                  <button @click="triggerFileInput(projThumbInput); activeDropdown = null">내 드라이브에서 불러오기</button>
                  <button @click="openUrlModal('projThumb', '구글 드라이브 URL')">구글 드라이브에서 불러오기</button>
                  <button @click="openUrlModal('projThumb', '임베드 URL')">임베드로 넣기</button>
                </div>
              </div>
              <input ref="projThumbInput" type="file" accept=".jpg,.jpeg,.png,.webp" style="display:none" @change="onFileChange($event, 'projThumb')" />
              <div v-if="projectForm.thumbnailUrl" class="file-preview-wrap">
                <img :src="projectForm.thumbnailUrl" class="file-preview-img" alt="썸네일 미리보기" />
                <button type="button" class="file-clear-btn" @click="projectForm.thumbnailUrl = ''">선택 삭제</button>
              </div>
            </div>
            <div class="image-section">
              <div class="input-label">설명용 영상</div>
              <div class="image-desc">mp4, mov, webm</div>
              <div class="split-btn-wrap" style="position:relative">
                <button class="split-btn-lead" type="button" @click="triggerFileInput(projVideoInput)">
                  <svg class="split-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="20" height="20">
                    <path d="M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96zM14 13v4h-4v-4H7l5-5 5 5h-3z"/>
                  </svg>
                  파일 선택
                </button>
                <button class="split-btn-trail" type="button" @click.stop="toggleDropdown('projVideo')">
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" width="22" height="22"><path d="M7 10l5 5 5-5z"/></svg>
                </button>
                <div v-if="activeDropdown === 'projVideo'" class="img-dropdown">
                  <button @click="triggerFileInput(projVideoInput); activeDropdown = null">내 드라이브에서 불러오기</button>
                  <button @click="openUrlModal('projVideo', '구글 드라이브 URL')">구글 드라이브에서 불러오기</button>
                  <button @click="openUrlModal('projVideo', '임베드 URL')">임베드로 넣기</button>
                </div>
              </div>
              <input ref="projVideoInput" type="file" accept=".mp4,.mov,.webm" style="display:none" @change="onFileChange($event, 'projVideo')" />
              <div v-if="projectForm.projectUrl" class="file-preview-wrap file-preview-wrap--video">
                <video v-if="isVideoUrl(projectForm.projectUrl)" :src="projectForm.projectUrl" controls class="file-preview-video"></video>
                <iframe v-else :src="projectForm.projectUrl" class="file-preview-video" allowfullscreen></iframe>
                <button type="button" class="file-clear-btn" @click="projectForm.projectUrl = ''">선택 삭제</button>
              </div>
            </div>
          </div>

          <div class="field-group">
            <div class="md-editor-header">
              <label class="input-label">세부설명</label>
              <div class="md-tab-group">
                <button type="button" class="md-tab" :class="{ active: !descPreview }" @click="descPreview = false">편집</button>
                <button type="button" class="md-tab" :class="{ active: descPreview }" @click="descPreview = true">미리보기</button>
              </div>
            </div>
            <textarea
              v-if="!descPreview"
              v-model="projectForm.description"
              rows="12"
              placeholder="내용 작성"
              class="field-input md-textarea"
            ></textarea>
            <div v-else class="md-preview" v-html="descHtml"></div>
          </div>

          <div class="field-group">
            <label class="input-label">GitHub URL</label>
            <input v-model="projectForm.repoUrl" type="url" placeholder=" " class="input-box" />
          </div>

          <div class="project-form-actions">
            <button type="button" class="btn-save-project" :disabled="projectSaving" @click="saveProject">
              {{ projectSaving ? '저장 중...' : '저장' }}
            </button>
            <button v-if="!isNewProject" type="button" class="btn-delete-project" @click="deleteProject">삭제</button>
            <button type="button" class="btn-cancel-project" @click="cancelProjectEdit">취소</button>
          </div>
        </div>

        <div v-else class="project-empty-hint">
          <p>프로젝트를 선택하거나 추가 카드를 클릭하세요</p>
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

    <!-- ── 미저장 경고 모달 ── -->
    <div v-if="showUnsavedModal" class="unsaved-backdrop" @click.self="cancelDiscard">
      <div class="unsaved-modal">
        <h3 class="unsaved-modal-title">저장하지 않은 변경사항이 있습니다</h3>
        <p class="unsaved-modal-desc">이동하면 변경사항이 사라집니다.</p>
        <div class="unsaved-modal-actions">
          <button class="unsaved-btn-discard" @click="confirmDiscard">버리기</button>
          <button class="unsaved-btn-keep" @click="cancelDiscard">계속 편집</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, reactive, computed } from 'vue'
import { marked } from 'marked'
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
const autoSaveMsg = ref('')  // '자동 저장됨' 표시용

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
const projThumbInput = ref(null)
const projVideoInput = ref(null)

// ── 이미지 드롭다운 / URL 모달 ──
const activeDropdown = ref(null)
const urlModal = reactive({ show: false, field: null, label: '', value: '' })

function toggleDropdown(key) {
  activeDropdown.value = activeDropdown.value === key ? null : key
}

function triggerFileInput(inputRef) {
  activeDropdown.value = null
  inputRef?.click()
}

function onFileChange(event, field) {
  const file = event.target.files[0]
  if (!file) return
  const url = URL.createObjectURL(file)
  if (field === 'bg') profileForm.bgImageUrl = url
  else if (field === 'logo') profileForm.logoImageUrl = url
  else if (field === 'avatar') profileForm.avatarUrl = url
  else if (field === 'projThumb') projectForm.thumbnailUrl = url
  else if (field === 'projVideo') projectForm.projectUrl = url
}

function openUrlModal(field, label) {
  activeDropdown.value = null
  urlModal.field = field
  urlModal.label = label
  urlModal.value = ''
  urlModal.show = true
}

function convertGDriveUrl(url) {
  const match = url.match(/\/file\/d\/([^/]+)/)
  if (match) return `https://drive.google.com/uc?id=${match[1]}`
  return url
}

function confirmUrl() {
  let url = urlModal.value.trim()
  if (!url) return
  if (urlModal.label === '구글 드라이브 URL') url = convertGDriveUrl(url)
  if (urlModal.field === 'bg') profileForm.bgImageUrl = url
  else if (urlModal.field === 'logo') profileForm.logoImageUrl = url
  else if (urlModal.field === 'avatar') profileForm.avatarUrl = url
  else if (urlModal.field === 'projThumb') projectForm.thumbnailUrl = url
  else if (urlModal.field === 'projVideo') projectForm.projectUrl = url
  urlModal.show = false
}

// 외부 클릭 시 드롭다운 닫기
function onDocumentClick() { activeDropdown.value = null }

// 영상 URL 판별 (blob 또는 확장자 기반)
function isVideoUrl(url) {
  if (!url) return false
  return url.startsWith('blob:') || /\.(mp4|mov|webm)$/i.test(url)
}

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
const selectedProjectIndex = computed(() => {
  if (isNewProject.value) return projectStore.projects.length + 1
  if (!selectedProject.value) return '-'
  const idx = projectStore.projects.findIndex(p => p.id === selectedProject.value.id)
  return idx === -1 ? '-' : idx + 1
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

// ── 세부설명 마크다운 ──
const descPreview = ref(false)
const descHtml = computed(() => marked.parse(projectForm.description || ''))

// ── 미저장 경고 모달 ──
const savedProjectSnapshot = reactive({
  title: '', description: '', techStack: '', status: '',
  projectUrl: '', repoUrl: '', thumbnailUrl: '',
})
const isDirty = computed(() => {
  if (!showProjectForm.value) return false
  return Object.keys(projectForm).some(k => projectForm[k] !== savedProjectSnapshot[k])
})
const showUnsavedModal = ref(false)
let pendingAction = null

function checkUnsaved(action) {
  if (isDirty.value) {
    pendingAction = action
    showUnsavedModal.value = true
  } else {
    action()
  }
}
function confirmDiscard() {
  showUnsavedModal.value = false
  pendingAction?.()
  pendingAction = null
}
function cancelDiscard() {
  showUnsavedModal.value = false
  pendingAction = null
}
function tryChangeTab(key) {
  if (activeTab.value === 'project') {
    checkUnsaved(() => { activeTab.value = key })
  } else {
    activeTab.value = key
  }
}
function trySelectProject(project) {
  checkUnsaved(() => selectProject(project))
}
function tryNewProject() {
  checkUnsaved(() => newProject())
}

// ── 자동 저장 ──
let autoSaveTimer = null

async function autoSave() {
  if (saving.value) return
  profileForm.name = teamMembers.value[0]?.name || ''
  profileForm.role = teamMembers.value[0]?.role || ''
  try {
    await profileStore.updateProfile({ ...profileForm })
    autoSaveMsg.value = '자동 저장됨'
    setTimeout(() => { autoSaveMsg.value = '' }, 2000)
  } catch {}
}

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
  autoSaveTimer = setInterval(autoSave, 60000)
  document.addEventListener('click', onDocumentClick)
})

onBeforeUnmount(() => {
  clearInterval(autoSaveTimer)
  document.removeEventListener('click', onDocumentClick)
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
  const data = {
    title: project.title || '',
    description: project.description || '',
    techStack: project.techStack || '',
    status: project.status || '',
    projectUrl: project.projectUrl || '',
    repoUrl: project.repoUrl || '',
    thumbnailUrl: project.thumbnailUrl || '',
  }
  Object.assign(projectForm, data)
  Object.assign(savedProjectSnapshot, data)
}

// ── 프로젝트: 새로 만들기 ──
function newProject() {
  selectedProject.value = null
  isNewProject.value = true
  Object.keys(projectForm).forEach(k => { projectForm[k] = '' })
  Object.keys(savedProjectSnapshot).forEach(k => { savedProjectSnapshot[k] = '' })
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
    Object.assign(savedProjectSnapshot, { ...projectForm })
  } catch (e) {
    console.error('프로젝트 저장 실패:', e)
  } finally {
    projectSaving.value = false
  }
}

// ── 프로젝트: 카드에서 직접 삭제 ──
async function deleteProjectFromCard(project) {
  if (!confirm('프로젝트를 삭제하시겠습니까?')) return
  try {
    await projectStore.deleteProject(project.id)
    if (selectedProject.value?.id === project.id) cancelProjectEdit()
  } catch (e) {
    console.error('프로젝트 삭제 실패:', e)
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

.toast--auto {
  top: 80px;
  background: #444;
  box-shadow: 0 4px 12px rgba(0,0,0,0.2);
  font-size: 13px;
  padding: 10px 20px;
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

/* ── 이미지 드롭다운 ── */
.img-dropdown {
  position: absolute;
  top: calc(100% + 4px);
  left: 0;
  background: #FFFFFF;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
  z-index: 100;
  min-width: 220px;
  overflow: hidden;
}

.img-dropdown button {
  display: block;
  width: 100%;
  padding: 12px 16px;
  background: none;
  border: none;
  text-align: left;
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  color: #1E1E1E;
  cursor: pointer;
  transition: background 0.15s;
}

.img-dropdown button:hover {
  background: #F2F2F7;
}

/* ── URL 입력 모달 ── */
.url-modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.url-modal {
  background: #FFFFFF;
  border-radius: 16px;
  padding: 32px;
  width: 480px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.2);
}

.url-modal-title {
  font-family: 'Roboto', sans-serif;
  font-size: 20px;
  font-weight: 500;
  color: #1E1E1E;
  margin: 0;
}

.url-modal-input {
  width: 100%;
  height: 48px;
  padding: 12px 16px;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  color: #1E1E1E;
  outline: none;
  box-sizing: border-box;
  transition: border-color 0.2s;
}

.url-modal-input:focus {
  border-color: #6750A4;
}

.url-modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.url-modal-confirm {
  padding: 10px 28px;
  background: #6750A4;
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  cursor: pointer;
  transition: opacity 0.2s;
}
.url-modal-confirm:hover { opacity: 0.85; }

.url-modal-cancel {
  padding: 10px 20px;
  background: transparent;
  color: #9E9E9E;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  cursor: pointer;
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

/* ── 연락처 탭 필드 ── */
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

/* ── 프로젝트 탭 ── */
.tab-pane--project {
  padding-top: 0;
}

.proj-count-label {
  font-family: 'ABeeZee', sans-serif;
  font-size: 32px;
  line-height: 38px;
  letter-spacing: -0.04em;
  color: #000000;
  margin: 0 0 24px 0;
}

/* 카드 스트립 */
.proj-strip {
  display: flex;
  flex-direction: row;
  overflow-x: auto;
  margin-bottom: 60px;
  scrollbar-width: thin;
}

.proj-card {
  position: relative;
  flex-shrink: 0;
  width: 348px;
  height: 212px;
  background: #000000;
  overflow: hidden;
  cursor: pointer;
  border: 5px solid transparent;
  transition: border-color 0.2s;
}

.proj-card:first-child { border-radius: 16px 0 0 16px; }
.proj-card:last-child  { border-radius: 0 16px 16px 0; }
.proj-card:only-child  { border-radius: 16px; }

.proj-card.selected {
  border-color: #8800FF;
  z-index: 1;
}

.proj-card-bg-img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.proj-card-gradient {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(78,87,95,0) 0%, rgba(78,87,95,0.5) 60%, #C3DEEF 100%);
}

.proj-card-name {
  position: absolute;
  top: 14px;
  left: 16px;
  font-family: 'ABeeZee', sans-serif;
  font-size: 19px;
  line-height: 22px;
  letter-spacing: -0.04em;
  color: #FFFFFF;
  z-index: 2;
}

.proj-card-status {
  position: absolute;
  bottom: 14px;
  right: 16px;
  font-family: 'Space Grotesk', sans-serif;
  font-size: 17px;
  line-height: 22px;
  letter-spacing: -0.04em;
  color: #FFFFFF;
  text-shadow: 0px 2px 1px rgba(0,0,0,0.25);
  z-index: 2;
}

.proj-card-icons {
  position: absolute;
  top: 10px;
  right: 10px;
  display: flex;
  gap: 4px;
  z-index: 3;
}

.proj-icon-btn {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0,0,0,0.45);
  border: none;
  border-radius: 6px;
  color: #FFFFFF;
  cursor: pointer;
  transition: background 0.2s;
}

.proj-icon-btn:hover { background: rgba(255,101,132,0.75); }

/* 추가 카드 */
.proj-card-add {
  background: #989898 !important;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #FFFFFF;
  gap: 8px;
  border-radius: 0 16px 16px 0 !important;
  transition: background 0.2s !important;
}

.proj-card-add:hover { background: #7a7a7a !important; }

.proj-card-add-label {
  font-family: 'ABeeZee', sans-serif;
  font-size: 19px;
  letter-spacing: -0.04em;
  color: #FFFFFF;
}

/* 편집 폼 */
.proj-form {
  max-width: 1369px;
}

.proj-form-row2 {
  display: grid;
  grid-template-columns: 354px 354px;
  gap: 24px;
  margin-bottom: 40px;
}

.input-field--wide {
  max-width: 783px;
}

.project-form-actions {
  display: flex;
  gap: 12px;
  margin-top: 32px;
  margin-bottom: 60px;
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

/* ── 파일 미리보기 ── */
.file-preview-wrap {
  margin-top: 10px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #D9D9D9;
  width: 100%;
  max-width: 354px;
  background: #000;
}

.file-preview-wrap--video {
  max-width: 480px;
  aspect-ratio: 16 / 9;
}

.file-preview-img {
  display: block;
  width: 100%;
  height: auto;
  max-height: 200px;
  object-fit: contain;
}

.file-preview-video {
  display: block;
  width: 100%;
  height: 100%;
  border: none;
}

.file-clear-btn {
  display: block;
  width: 100%;
  padding: 8px;
  background: #F9F9F9;
  border: none;
  border-top: 1px solid #D9D9D9;
  font-family: 'Roboto', sans-serif;
  font-size: 13px;
  color: #ff6584;
  cursor: pointer;
  transition: background 0.15s;
}

.file-clear-btn:hover {
  background: #fff0f3;
}

/* ── 마크다운 에디터 ── */
.md-editor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.md-tab-group {
  display: flex;
  gap: 2px;
  background: #F2F2F7;
  border-radius: 8px;
  padding: 3px;
}

.md-tab {
  padding: 4px 14px;
  border: none;
  border-radius: 6px;
  background: transparent;
  font-family: 'Roboto', sans-serif;
  font-size: 13px;
  color: #757575;
  cursor: pointer;
  transition: background 0.15s, color 0.15s;
}

.md-tab.active {
  background: #FFFFFF;
  color: #1E1E1E;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
}

.md-textarea {
  resize: vertical;
  min-height: 160px;
  font-family: 'Roboto Mono', 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.6;
}

.md-preview {
  min-height: 160px;
  padding: 14px 18px;
  border: 1px solid #D9D9D9;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  line-height: 1.7;
  color: #1E1E1E;
  background: #FAFAFA;
}

.md-preview :deep(h1),
.md-preview :deep(h2),
.md-preview :deep(h3) {
  font-weight: 600;
  margin: 12px 0 6px;
}

.md-preview :deep(p) { margin: 6px 0; }

.md-preview :deep(ul),
.md-preview :deep(ol) {
  padding-left: 20px;
  margin: 6px 0;
}

.md-preview :deep(code) {
  background: #F2F2F7;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 13px;
}

.md-preview :deep(strong) { font-weight: 600; }

/* ── 미저장 경고 모달 ── */
.unsaved-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
}

.unsaved-modal {
  background: #FFFFFF;
  border-radius: 16px;
  padding: 36px 40px;
  width: 440px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.unsaved-modal-title {
  font-family: 'Space Grotesk', sans-serif;
  font-size: 18px;
  font-weight: 600;
  color: #1E1E1E;
  margin: 0;
}

.unsaved-modal-desc {
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  color: #757575;
  margin: 0;
}

.unsaved-modal-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 8px;
}

.unsaved-btn-discard {
  padding: 10px 24px;
  background: transparent;
  color: #ff6584;
  border: 1px solid #ff6584;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  cursor: pointer;
  transition: opacity 0.2s;
}
.unsaved-btn-discard:hover { opacity: 0.75; }

.unsaved-btn-keep {
  padding: 10px 24px;
  background: #6750A4;
  color: #FFFFFF;
  border: none;
  border-radius: 8px;
  font-family: 'Roboto', sans-serif;
  font-size: 15px;
  cursor: pointer;
  transition: opacity 0.2s;
}
.unsaved-btn-keep:hover { opacity: 0.85; }
</style>
