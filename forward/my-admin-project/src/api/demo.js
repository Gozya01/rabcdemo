import request from '@/utils/request'

// 用户登录
export function getMysqlDemo() {
  return request({
    url: '/users/1',
    method: 'get'
  })
}
