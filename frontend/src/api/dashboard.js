import api from './index.js'

export const dashboardApi = {
  getTeamMembers: () => api.get('/dashboard/team'),
}
