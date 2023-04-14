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
        <div style="align-self: center">
          <Avatar></Avatar>
        </div>
        <div style="align-self: end;">
          <div>
            <h3>{{user.username}}</h3>
            <el-button @click="showInfo">点击</el-button>
          </div>
        </div>
      </div>
    </div>
<!--编辑个人信息-->
    <div style="align-self: flex-end">个人资料编辑</div>
  </div>
<!--  作品区-->
  <div>个人精选作品区or收藏区</div>
</div>
</template>

<script>
import Avatar from "@/components/Avatar";
export default {
  name: "MyInfoView",
  components: {Avatar},
  data(){
    return{
      user:{}
    }
  },
  created() {
    this.load()
  },
  methods:{
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
    showInfo(){
      this.$router.push({
        name:'UserInfo'
      })
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