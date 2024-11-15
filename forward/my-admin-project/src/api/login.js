import request from '@/utils/request'

// 用户登录
export function login(username, password) {
  const data = {
    username,
    password
  }
  return request({
    url: '/users/login',
    method: 'post',
    data: data,
    withCredentials: true // 使请求携带 cookie
  })
}

// 获取用户信息
export function getInfo() {
  return request({
    url: '/users/getInfo',
    method: 'get',
    withCredentials: true // 使请求携带 cookie
  })
}
// 用户退出
export function logout() {
    return request({
      url: '/users/logout',
      method: 'post'
    });
  }
  