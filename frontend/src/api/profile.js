import api from './index.js'

export const profileApi = {
  getMyProfile: () => api.get('/profile/me'),
  updateMyProfile: (data) => api.put('/profile/me', data),
  getProfileByUserId: (userId) => api.get(`/profile/user/${userId}`),
}
