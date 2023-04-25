<template>
<div>
<!--  背景-->
  <div class="bg">
    <img src="@/assets/bg.png" height="200px" width="100%" alt="bg">
  </div>

<!--  个人信息区别-->
  <div class="myInfo">
<!--    个人信息-->
    <div style="align-self: flex-start">
      <div style="display: flex;justify-content: space-between;">
        <div>
          <UserInfoMini :user="user" :size="80"></UserInfoMini>
          <el-button @click="displayWin">查看个人信息</el-button>
        </div>
      </div>
    </div>
<!--编辑个人信息-->
    <div style="align-self: flex-end">
      <el-button @click="displayWin2">个人资料编辑</el-button>
    </div>
  </div>
<!--  作品区-->
  <div>
    <el-row>
      <el-col :span="24"><div class="grid-content bg-purple-dark">
        <h2>个人作品精选区</h2>
      </div></el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
      <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
    </el-row>

  </div>
  <div>
    <component v-if="show" :is="popup" @close="hidePopup" :user="user" :showContent="judge"></component>
  </div>
</div>
</template>

<script>
import Avatar from "@/components/Avatar";
import UserInfo from "@/components/UserInfo";
import UserInfoMini from "@/components/UserInfoMini";

export default {
  name: "MyInfoView",
  components: {UserInfoMini, Avatar},
  data(){
    return{
      user:{},
      show: false,
      popup: null,
      // 决定弹窗显示内容
      judge: true
    }
  },
  created() {
    this.load()
    this.$bus.$on('showModal', () => {
      this.popup = UserInfo
      this.show = true
    })
    this.$bus.$on('closeWin',()=>{
      this.show = false
    })
    this.$bus.$on('displayEditUI',()=>{
      this.judge = false
    })
  },
  methods:{
    displayWin(){
      this.judge = true
      this.$bus.$emit('showModal')
    },
    displayWin2(){
      this.judge = false
      this.$bus.$emit('showModal')
    },
    load(){
      this.axios.get('http://localhost:9090/user/get/info').then(response=>{
        if (response.status===200){
          console.log("加载成功")
          this.user = response.data.data
          this.$store.dispatch('login',response.data.data)
        }
        else {
          this.$message.success(response.data.data.message)
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    hidePopup() {
      this.show = false
      //关闭遮蔽层
      this.$bus.$emit('closeModal')
    }
  }
}
</script>

<style scoped>
.myInfo{
  display: flex;
  justify-content: space-between;
}
.el-row {
  margin-bottom: 20px;
&:last-child {
   margin-bottom: 0;
 }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>