<template>
  <div class="login-page">
    <div class="login-card" style="display: flex;justify-content: center;align-items: center;">
      <div>
        <div class="login-title" style="margin-bottom: 100px">Pic-Sharing</div>
        <div style="display: flex;justify-content: center;align-items: center; margin-bottom: 15px">
          <el-button type="primary" style="width: 180px;" @click="toRegister" round>注册账号</el-button>
        </div>
        <div style="display: flex;justify-content: center;align-items: center; margin-bottom: 100px">
          <el-button style="width: 180px;" @click="toLogin" round>
            登录
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Index',
  methods: {
    toRegister(){
      this.$router.push({
        name:'register'
      })
    },
    toLogin(){
      this.$router.push({
        name: 'login'
      })
    }
  }
}
</script>

<style>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 98vh;
  background-color: #f7d9e9;
  background-image: url(@/assets/bg.png);
  background-repeat: no-repeat;
  background-size: cover;
}

.login-card {
  width: 300px;
  height: 400px;
  padding: 30px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
}

.login-form {
  margin-top: 20px;
}
</style>
