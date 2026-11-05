// frontend/src/api/client.ts
import axios, { AxiosError } from 'axios'
import { Configuration } from './generated/configuration'

// Base axios instance
export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  withCredentials: true,
})

interface QueueItem {
  resolve: (token: string) => void
  reject: (error: unknown) => void
}

// Flag to prevent infinite loops during token refresh
let isRefreshing = false
let failedQueue: QueueItem[] = []

const processQueue = (error: unknown, token: string | null = null): void => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error)
    } else if (token) {
      prom.resolve(token)
    }
  })
  failedQueue = []
}

// 1. Request Interceptor: attach Bearer token
axiosInstance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('access_token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 2. Response Interceptor: catch 401 errors and perform a single refresh attempt
axiosInstance.interceptors.response.use(
  response => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as typeof error.config & { _retry?: boolean }

    if (error.response?.status === 401 && originalRequest && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise<string>((resolve, reject) => {
          failedQueue.push({ resolve, reject })
        })
          .then(token => {
            if (originalRequest.headers) {
              originalRequest.headers.Authorization = `Bearer ${token}`
            }
            return axiosInstance(originalRequest)
          })
          .catch(err => Promise.reject(err))
      }

      originalRequest._retry = true
      isRefreshing = true

      try {
        const refreshToken = localStorage.getItem('refresh_token')
        // Request to refresh the access token
        const response = await axiosInstance.post<{ accessToken: string; refreshToken?: string }>('/api/v1/auth/refresh', { refreshToken })
        const { accessToken, refreshToken: newRefresh } = response.data

        localStorage.setItem('access_token', accessToken)
        if (newRefresh) {
          localStorage.setItem('refresh_token', newRefresh)
        }

        axiosInstance.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
        processQueue(null, accessToken)

        isRefreshing = false
        if (originalRequest.headers) {
          originalRequest.headers.Authorization = `Bearer ${accessToken}`
        }
        return axiosInstance(originalRequest)
      } catch (refreshError: unknown) {
        processQueue(refreshError, null)
        isRefreshing = false

        // Logout on refresh failure: clear tokens and redirect to login
        localStorage.removeItem('access_token')
        localStorage.removeItem('refresh_token')
        window.location.href = '/login'
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  }
)

// Configuration for OpenAPI-generated code
export const apiConfig = new Configuration({
  basePath: 'http://localhost:8080',
})