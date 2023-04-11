<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-title">登录</div>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" autocomplete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: {
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
    //登录验证
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const jsonUserData = JSON.stringify(this.form)
          // 处理登录逻辑，比如向后端API发送登录请求等
          this.axios.post('http://localhost:9090/admin/login',jsonUserData,{
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
              this.$router.push('/')
            }
            else {
              this.$message.success("登录失败")
              console.log('登录失败')
            }
          }).catch(error=>{
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
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 200px;
  height: 100%;
}
.login-card {
  width: 400px;
}
.login-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
}
.login-form {
  margin-top: 20px;
}

</style>