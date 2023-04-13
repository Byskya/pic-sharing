<template>
  <div class="register-page">
    <div class="register-card">
      <div class="register-title">注册账号</div>
      <el-form :model="registerForm" ref="registerForm" :rules="rules" class="register-form" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" autocomplete="off" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password" autocomplete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="registerForm.confirmPassword" autocomplete="off" placeholder="请确认密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RegisterView',
  data() {
    return {
      registerForm: {
        username: '',
        email:'',
        password: '',
        confirmPassword: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/, message: '请输入正确的邮箱格式', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        confirmPassword:[
          { required: true, message: '请确认密码', trigger: 'blur' },
            //两次输入密码是否一致验证
          { validator: (rule, value, callback) => {
              if (value === '') {
                callback(new Error('请再次输入密码'))
              } else if (value !== this.registerForm.password) {
                callback(new Error('两次输入密码不一致'))
              } else {
                callback()
              }
            }, trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    register() {
      // TODO: 注册逻辑
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          const jsonUserData = JSON.stringify(this.registerForm)
          // 处理登录逻辑，比如向后端API发送登录请求等
          this.axios.post('http://localhost:9090/user/register',jsonUserData,{
            headers:{
              'Content-Type':'application/json'
            }
          }).then(response=>{
            if (response.data.state===200){
              console.log('注册成功')
              // 跳转到后台管理页面
              this.$message.success("注册成功")
              this.$router.push('/login')
            }
            else {
              this.$message.success("注册失败")
              console.log('注册失败')
            }
          }).catch(error=>{
            console.error(error);
          });
        } else {
          this.$message.success("注册失败")
          console.log('注册失败')
        }
      })
    }
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 98vh;
  background-color: #f7d9e9;
  /*background-image: url(https://cdn.pixabay.com/photo/2016/12/23/03/23/anime-1920599_960_720.png);*/
  background-repeat: no-repeat;
  background-size: cover;
}

.register-card {
  width: 400px;
  padding: 30px;
  background-color: rgba(255, 255, 255, 0.8);
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.register-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
}

.register-form {
  margin-top: 20px;
}
</style>
