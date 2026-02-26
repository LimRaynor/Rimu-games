import api from './index.js'

export const projectApi = {
  getMyProjects: () => api.get('/projects/my'),
  createProject: (data) => api.post('/projects', data),
  updateProject: (projectId, data) => api.put(`/projects/${projectId}`, data),
  deleteProject: (projectId) => api.delete(`/projects/${projectId}`),
  getProjectsByUserId: (userId) => api.get(`/projects/user/${userId}`),
}
