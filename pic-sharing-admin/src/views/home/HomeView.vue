<template>
  <div class="login-wrapper">
    <el-card class="login-card">
      <h2 class="title">PIXIV</h2>
      <el-form :model="loginForm" ref="loginForm" label-width="0px" class="login-form">
        <el-form-item>
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="loginForm.password" placeholder="密码" prefix-icon="el-icon-lock" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    login() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // 在这里处理登录请求，例如使用 axios 发送登录请求
          // 然后在登录成功后跳转到其他页面
          this.$router.push('/home')
        }
      })
    }
  }
}
</script>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 360px;
  padding: 30px;
  text-align: center;
}

.title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 30px;
  color: #333;
}

.login-form {
  text-align: left;
}

.login-btn {
  width: 100%;
}
</style>
