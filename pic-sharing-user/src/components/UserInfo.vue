<template>
  <div class="popup" style="display: flex;">
    <div class="popup-content">
<!--      用户信息显示页面-->
      <div v-if="showContent">
        <div class="profile">
          <el-card class="profile-card">
            <div slot="header" class="profile-header">
              <el-avatar v-if="user.avatar!=null" :src="'data:image/*;base64,'+user.avatar" :size="100"></el-avatar>
              <el-avatar v-else :src="headerBg" :size="100"></el-avatar>
              <h2>{{ user.username }}</h2>
            </div>
            <div class="profile-content">
              <h3>个人信息</h3>
              <div class="profile-item">
                <span class="profile-label">性别：</span>
                <span class="profile-value">{{ user.gender }}</span>
              </div>
              <div class="profile-item">
                <span class="profile-label">年龄：</span>
                <span class="profile-value">{{ user.age }}</span>
              </div>
              <div class="profile-item">
                <span class="profile-label">生日：</span>
                <span class="profile-value">{{ user.birthday }}</span>
              </div>
              <div class="profile-item">
                <span class="profile-label">邮箱：</span>
                <span class="profile-value">{{ user.email }}</span>
              </div>
              <div class="profile-item">
                <span class="profile-label">电话：</span>
                <span class="profile-value">{{ user.phoneNumber }}</span>
              </div>
              <div class="profile-item">
                <span class="profile-label">地址：</span>
                <span class="profile-value">{{ user.address }}</span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
<!--      编辑用户信息页面=====================================================================================-->
      <div v-else>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
<!--          头像-->
          <el-form-item label="用户头像" prop="avatar">
            <el-upload
                class="avatar-uploader"
                action="http://localhost:9090/file/upload"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
              <img v-if="imageUrl" :src="imageUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
<!--          用户名-->
          <el-form-item label="用户名" prop="username">
            <el-input v-model="ruleForm.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-input v-model="ruleForm.gender" autocomplete="off"></el-input>
          </el-form-item>

<!--          用户地址-->
          <el-form-item label="地址" prop="address">
            <el-input v-model="ruleForm.address" autocomplete="off"></el-input>
          </el-form-item>
<!--          生日-->
          <el-form-item label="生日" required>
            <el-col :span="16">
              <el-form-item>
                <el-date-picker prop="birthday" type="date" placeholder="选择日期" v-model="ruleForm.birthday" style="width: 100%;" autocomplete="off"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-form-item>
<!--          提交按钮-->
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <el-button type="danger" icon="el-icon-close" @click="$emit('close')" circle></el-button>
  </div>
</template>

<script>
import headerBg from '@/assets/headbg.jpg'
export default {
  name: "UserInfo",
  data(){
    return{
      // 用户头像背景
      headerBg,
      // 用户上传头像
      imageUrl: null,
      // 表单数据
      ruleForm:Object,
      // 表单验证规则
      rules: {
        username: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
        ],
        gender: [
          { required: true, message: '请输入性别', trigger: 'blur' },
        ],
        address: [
          { required: true, message: '请输入地址信息', trigger: 'blur' }
        ],
        birthday: [
          { type: 'date', required: true, message: '请选择日期', trigger: 'blur' }
        ]
      }
    }
  },
  props: {
    user:Object,
    showContent:Boolean,
  },
  created() {
    // 把用户数据放入到表单中
    this.ruleForm = Object.assign({}, this.user)
    if (this.user.avatar!=null){
      this.imageUrl = 'data:image/*;base64,' + this.user.avatar
    }
  },
  methods:{
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
          const formData = new FormData();
          formData.append('avatar',this.ruleForm.avatar)
          formData.append('username',this.ruleForm.username)
          formData.append('gender',this.ruleForm.gender)
          formData.append('birthday',this.ruleForm.birthday)
          formData.append('address',this.ruleForm.address)
          formData.append('id',this.ruleForm.id)
          this.axios.put('http://localhost:9090/user/infoEdit',formData,{
            headers:{
              'Content-Type':'multipart/form-data'
            }
          }).then(response=>{
            if (response.status===200){
              this.$message.success("用户信息修改成功")
            }
            console.log(response)
          }).catch(error=>{
            this.$message.success("用户信息修改失败")
            console.log(error)
          })

        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 头像上传相关方法
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
      this.ruleForm.avatar = file.raw
      console.log(this.ruleForm.avatar)
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>

<style scoped>
.popup {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  padding: 20px;
  z-index: 1000;
}
.user-info img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 20px;
}

/*头像容器样式*/
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
/*用户信息查看界面样式*/
.profile {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  height: 100%;
}

.profile-card {
  width: 600px;
}

.profile-header {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.profile-header h2 {
  margin: 10px 0;
}

.profile-content {
  margin-top: 30px;
}

.profile-content h3 {
  margin-bottom: 20px;
}

.profile-item {
  margin-bottom: 10px;
}

.profile-label {
  font-weight: bold;
}

.profile-value {
  margin-left: 5px;
}
</style>