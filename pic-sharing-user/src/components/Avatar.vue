<template>
  <div class="user-menu">
    <el-dropdown trigger="click">
      <span class="avatar">
        <img v-if="avatarData!=null" :src="'data:image/*;base64,'+avatarData" alt="User Avatar">
        <img v-else src="@/assets/headbg.jpg" alt="User Avatar" @click="toLogin">
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <a @click="toMyInfo">{{user.username}}</a>
        </el-dropdown-item>
        <el-dropdown-item>
          <a @click="toMyWorks">我的作品</a>
        </el-dropdown-item>
        <el-dropdown-item>
          <a @click="toUserFollow">关注列表</a>
        </el-dropdown-item>
        <el-dropdown-item><a @click="toCollection">收藏</a></el-dropdown-item>
        <el-dropdown-item>浏览记录</el-dropdown-item>
        <el-dropdown-item>
          <el-badge :hidden="this.messageNumber===0" :value="this.messageNumber" class="item">
            <a @click="toMessageView">用户消息</a>
          </el-badge>
        </el-dropdown-item>

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
      // 判断是否为用户自己的信息
      isMine:'yes',
      user:{},
      messageNumber:0,
    }
  },
  created() {
    if (localStorage.getItem("isLoggedIn")){
      this.load()
      this.loadMessageNumber()
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
    toMessageView(){
      this.$router.push({
        name:'messageView'
      })
    },
    toUserFollow(){
      this.$router.push({
        name:'userFollow'
      })
    },
    // 跳转到收藏夹
    toCollection(){
      this.$router.push({
        name:'myCollection'
      })
    },
    toLogin(){
      localStorage.setItem('isLoggedIn','false')
      this.$message.success("跳转登录")
      this.$router.push({
        name:'index'
      })
    },
    loginOut(){
      localStorage.setItem('isLoggedIn','false')
      this.$message.success("退出登录成功")
      this.$router.push('/index')
    },
    toMyWorks(){
      this.$router.push({
        name:'myWorks',
      })
    },
    toMyInfo(){
      const isMine = JSON.stringify(this.isMine)
      this.$router.push({
        name:'myInfo',
        query:{
          isMine
        }
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
    },
    loadMessageNumber(){
      this.axios.get('http://localhost:9090/message/number').then(response=>{
        if (response.status===200){
          this.messageNumber = response.data.data
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
