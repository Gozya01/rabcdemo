<template>
  <div class="home-page">
    <el-main>
      <div class="content">
        <h1>欢迎，{{ username }}！</h1>
      </div>
    </el-main>
  </div>
</template>

<script>
import { getInfo } from "@/api/auth.js"
import { getAccessToken, removeToken } from "@/utils/auth.js"

export default {
  name: "HomeView",
  data() {
    return {
      username: ""
    }
  },
  created() {
    const token = getAccessToken()
    if (!token) {
      // 如果没有 token，跳转到登录页面
      this.$router.push({ path: '/login' })
    } else {
      // 请求用户信息
      getInfo().then((res) => {
        if (res.username) {
          this.username = res.username
        } else {
          this.$router.push({ path: '/login' })
        }
      }).catch(() => {
        this.$router.push({ path: '/login' })
      })
    }
  },
  methods: {
    handleLogout() {
      removeToken() // 清除token
      this.$router.push({ path: '/login' }) // 跳转到登录页面
    }
  }
}
</script>

<style scoped>
/* 样式部分 */
</style>
