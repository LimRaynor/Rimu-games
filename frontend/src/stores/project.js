import { defineStore } from 'pinia'
import { ref } from 'vue'
import { projectApi } from '../api/project.js'

export const useProjectStore = defineStore('project', () => {
  const projects = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchMyProjects() {
    loading.value = true
    error.value = null
    try {
      projects.value = await projectApi.getMyProjects()
    } catch (e) {
      error.value = e
    } finally {
      loading.value = false
    }
  }

  async function createProject(data) {
    const project = await projectApi.createProject(data)
    projects.value.push(project)
    return project
  }

  async function updateProject(projectId, data) {
    const updated = await projectApi.updateProject(projectId, data)
    const index = projects.value.findIndex(p => p.id === projectId)
    if (index !== -1) projects.value[index] = updated
    return updated
  }

  async function deleteProject(projectId) {
    await projectApi.deleteProject(projectId)
    projects.value = projects.value.filter(p => p.id !== projectId)
  }

  return { projects, loading, error, fetchMyProjects, createProject, updateProject, deleteProject }
})
