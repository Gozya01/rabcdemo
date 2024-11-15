import axios from 'axios'
import { getAccessToken, removeToken } from '@/utils/auth'
import router from '@/router'
import { Message } from 'element-ui'

const service = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = getAccessToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token // 在请求头中添加 Token
    }
    return config
  },
  error => {
    Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response) {
      if (error.response.status === 401) {
        // 未授权，跳转到登录页面
        removeToken()
        router.push('/login')
        Message.error('未授权，请重新登录')
      } else if (error.response.status === 403) {
        // 无权限，显示错误信息
        Message.error('无权限访问')
      } else {
        // 其他错误
        Message.error(error.response.data.message || '请求错误')
      }
    }
    return Promise.reject(error)
  }
)

export default service
