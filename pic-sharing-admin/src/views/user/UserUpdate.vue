<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px">编辑用户</div>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="用户名">
        <el-input v-model="formInline.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="formInline.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="formInline.gender" placeholder="请输入性别"></el-input>
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="formInline.phoneNumber" placeholder="请输入手机号码"></el-input>
      </el-form-item>
      <el-form-item label="生日">
        <el-col :span="20">
          <el-date-picker type="date" placeholder="选择日期" v-model="formInline.birthday" style="width: 100%;"></el-date-picker>
        </el-col>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="formInline.address" placeholder="请输入地址"></el-input>
      </el-form-item>
      <el-form-item label="管理员">
        <el-switch v-model="formInline.admin"></el-switch>
      </el-form-item>
      <div></div>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name:'UserUpdate',
  mounted() {
    let query = this.$route.query
    this.formInline={
      id:query.row.id,
      username: query.row.username,
      password:query.row.password,
      gender:query.row.gender,
      phoneNumber:query.row.phoneNumber,
      birthday: query.row.birthday,
      address: query.row.address,
      admin: query.row.admin
    }
  },
  data() {
    return {
      formInline: {
        id:'',
        username: '',
        password:'',
        gender: '',
        phoneNumber:'',
        birthday:'',
        address:'',
        admin: '',
      }
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!');
      const jsonUserData = JSON.stringify(this.formInline)
      console.log(jsonUserData)
      this.axios.put('http://localhost:9090/user/update',jsonUserData,{
        headers:{
          'Content-Type':'application/json'
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
    }
  }
}
</script>

<style scoped>

</style>