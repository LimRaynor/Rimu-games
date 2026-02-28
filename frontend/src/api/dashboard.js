import api from './index.js'

export const dashboardApi = {
  getTeamMembers: () => api.get('/dashboard/team'),
  getProjects:    () => api.get('/dashboard/projects'),
}
