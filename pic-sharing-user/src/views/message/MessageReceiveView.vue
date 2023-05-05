<template>
  <div v-if="!isLoggedIn">您尚未登录，请先登录</div>
  <div v-else>
    <!--    导航栏和内容区-->
    <div style="display: flex">
      <!--      导航区-->
      <div style="width: 300px; min-height: 100vh; overflow: hidden; margin-right: 2px; background-color: white">
        <el-menu :default-active="$route.path" router class="el-menu-demo">
          <el-menu-item index="notification">
            <i class="el-icon-message-solid"></i>
            <span>系统通知</span>
          </el-menu-item>
          <el-menu-item index="userMessage">
            <i class="el-icon-message"></i>
            <el-badge :hidden="this.messageNumber===0" :value="this.messageNumber" class="item">
              <span>用户信息</span>
            </el-badge>
          </el-menu-item>
        </el-menu>
      </div>
      <!--      内容区-->
      <div style="width: 80%">
        <router-view/>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: "MessageView",
  data(){
    return{
      isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
      messageNumber:0,
    }
  },
  created() {
    this.loadMessageNumber()
  },
  methods:{
    loadMessageNumber(){
      this.$http.get('/message/number').then(response=>{
        if (response.status===200){
          this.messageNumber = response.data.data
        }
      }).catch(error=>{
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>

</style>