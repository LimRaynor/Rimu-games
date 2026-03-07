import api from './index.js'

export const teamMemberApi = {
  getAll:          ()       => api.get('/team-members'),
  create:          (data)   => api.post('/team-members', data),
  update:          (id, data) => api.put(`/team-members/${id}`, data),
  delete:          (id)     => api.delete(`/team-members/${id}`),
}
