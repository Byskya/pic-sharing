<template>
  <div class="login-page">
    <div class="login-card" style="height: 300px">
      <div class="login-title">登录</div>
      <el-form :model="loginForm" ref="loginForm" :rules="rules" class="login-form" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" autocomplete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    login() {
      // TODO: 登录逻辑
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const jsonUserData = JSON.stringify(this.loginForm)
          // 处理登录逻辑，比如向后端API发送登录请求等
          this.axios.post('http://localhost:9090/user/login',jsonUserData,{
            headers:{
              'Content-Type':'application/json'
            }
          }).then(response=>{
            if (response.data.state===200){
              console.log('登录成功')
              // 跳转到后台管理页面
              this.$message.success("登录成功")
              //设置路由守卫的的登录判断变量
              localStorage.setItem('isLoggedIn','true')
              this.$store.dispatch('login',response.data.data);
              this.$router.push('/')
            }
            else {
              this.$message.success("登录失败")
              console.log('登录失败')
            }
          }).catch(error=>{
            this.$message.success("登录失败")
            console.error(error);
          });
        } else {
          this.$message.success("登录失败")
          console.log('登录失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 98vh;
  background-color: #f7d9e9;
  /*壁纸随机获取用户的插画图片*/
  background-image: url(https://cdn.pixabay.com/photo/2016/12/23/03/23/anime-1920599_960_720.png);
  background-repeat: no-repeat;
  background-size: cover;
}

.login-card {
  width: 400px;
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
