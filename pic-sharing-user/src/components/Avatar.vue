<template>
  <div class="user-menu">
    <el-dropdown trigger="click">
      <span class="avatar">
        <img v-if="avatarData!=null" :src="'data:image/*;base64,'+avatarData" alt="User Avatar">
        <img v-else src="@/assets/headbg.jpg" alt="User Avatar">
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <a @click="toMyInfo">{{user.username}}</a>
        </el-dropdown-item>
        <el-dropdown-item>
          <a @click="toMyWorks">我的作品</a>
        </el-dropdown-item>
        <el-dropdown-item><a @click="toCollection">收藏</a></el-dropdown-item>
        <el-dropdown-item>浏览记录</el-dropdown-item>
        <el-dropdown-item><a @click="loginOut">退出登录</a></el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>
<script>

import {mapState} from 'vuex'

export default {
  name:'Avatar',
  data(){
    return{
      user:{},
    }
  },
  created() {
    if (localStorage.getItem("isLoggedIn")){
      this.load()
    }
  },
  computed:{
    ...mapState({
      isAuthenticated: state => state.isAuthenticated,
      //优先使用vuex中的user对象
      // user: state=>state.user
    }),
    avatarData:{
      get(){
        return this.user.avatar
      }
    }
  },
  methods:{
    // 跳转到收藏夹
    toCollection(){
      this.$router.push({
        name:'myCollection'
      })
    },
    loginOut(){
      localStorage.setItem('isLoggedIn','false')
      this.$message.success("退出登录成功")
      this.$router.push('index')
    },
    toMyWorks(){
      this.$router.push({
        name:'myWorks'
      })
    },
    toMyInfo(){
      this.$router.push({
        name:'myInfo'
      })
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
    }
  }
};
</script>
<style scoped>
.user-menu {
  position: relative;
  display: inline-block;
}

.avatar {
  display: block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
}

.avatar img {
  display: block;
  width: 100%;
  height: auto;
}
</style>
