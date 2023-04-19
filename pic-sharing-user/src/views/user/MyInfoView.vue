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
          <Avatar></Avatar>
        </div>
        <div style="align-self: end;">
          <div>
            <h3>{{user.username}}</h3>
            <el-button @click="displayWin">查看个人信息</el-button>
          </div>
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
    个人精选作品区or收藏区

  </div>
  <div>
    <component v-if="show" :is="popup" @close="hidePopup" :user="user" :showContent="judge"></component>
  </div>
</div>
</template>

<script>
import Avatar from "@/components/Avatar";
import UserInfo from "@/components/UserInfo";

export default {
  name: "MyInfoView",
  components: {Avatar},
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
</style>