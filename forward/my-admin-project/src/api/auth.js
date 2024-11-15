import request from '@/utils/request'

// 用户登录
export function login(username, password) {
  return request({
    url: '/auth/login',
    method: 'post',
    data: { username, password },
  });
}

// 获取用户信息和权限
export function getInfo() {
  return request({
    url: '/auth/getInfo',
    method: 'get',
  });
}
