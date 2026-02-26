<template>
  <div class="dashboard">
    <div class="dashboard-header">
      <h1>🎮 팀 소개</h1>
      <p class="subtitle">Rimu Games 팀원들을 만나보세요</p>
    </div>

    <div v-if="loading" class="loading">불러오는 중...</div>

    <div v-else-if="teamMembers.length === 0" class="empty">
      <p>아직 등록된 팀원이 없습니다.</p>
    </div>

    <div v-else class="team-grid">
      <div
        v-for="member in teamMembers"
        :key="member.userId"
        class="member-card card"
      >
        <div class="member-avatar">
          <img
            v-if="member.avatarUrl"
            :src="member.avatarUrl"
            :alt="member.nickname"
          />
          <div v-else class="avatar-placeholder">
            {{ (member.name || member.nickname || '?').charAt(0).toUpperCase() }}
          </div>
        </div>
        <div class="member-info">
          <h3 class="member-name">{{ member.name || member.nickname }}</h3>
          <p v-if="member.role" class="member-role">{{ member.role }}</p>
          <p v-if="member.bio" class="member-bio">{{ member.bio }}</p>
          <div class="member-stats">
            <span class="stat">프로젝트 {{ member.projectCount }}개</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { dashboardApi } from '../../api/dashboard.js'

const teamMembers = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    teamMembers.value = await dashboardApi.getTeamMembers()
  } catch (e) {
    console.error('팀원 불러오기 실패:', e)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.dashboard-header {
  margin-bottom: 32px;
}

.dashboard-header h1 {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 8px;
}

.subtitle {
  color: var(--color-text-muted);
  font-size: 16px;
}

.team-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.member-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 16px;
  transition: transform 0.2s;
}

.member-card:hover {
  transform: translateY(-4px);
}

.member-avatar img,
.avatar-placeholder {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-placeholder {
  background: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  font-weight: 700;
  color: #fff;
}

.member-name {
  font-size: 18px;
  font-weight: 600;
}

.member-role {
  color: var(--color-primary);
  font-size: 13px;
  margin-top: 2px;
}

.member-bio {
  color: var(--color-text-muted);
  font-size: 13px;
  margin-top: 6px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.member-stats {
  margin-top: 8px;
}

.stat {
  background: var(--color-border);
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  color: var(--color-text-muted);
}

.loading, .empty {
  text-align: center;
  padding: 60px 0;
  color: var(--color-text-muted);
}
</style>
