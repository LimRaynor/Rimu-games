import { defineStore } from 'pinia'
import { ref } from 'vue'
import { profileApi } from '../api/profile.js'

export const useProfileStore = defineStore('profile', () => {
  const profile = ref(null)
  const loading = ref(false)
  const error = ref(null)

  async function fetchMyProfile() {
    loading.value = true
    error.value = null
    try {
      profile.value = await profileApi.getMyProfile()
    } catch (e) {
      error.value = e
    } finally {
      loading.value = false
    }
  }

  async function updateProfile(data) {
    loading.value = true
    error.value = null
    try {
      profile.value = await profileApi.updateMyProfile(data)
    } catch (e) {
      error.value = e
      throw e
    } finally {
      loading.value = false
    }
  }

  return { profile, loading, error, fetchMyProfile, updateProfile }
})
