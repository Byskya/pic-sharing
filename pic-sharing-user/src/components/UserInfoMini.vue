<template>
  <div class="user-info">
    <el-avatar v-if="userInfo.avatar!=null" :src="'data:image/*;base64,'+userInfo.avatar" :size="size"></el-avatar>
    <el-avatar v-else :src="headerBg" :size="size"></el-avatar>
    <span class="username">{{ userInfo.username }}</span>
  </div>
</template>

<script>
import headerBg from '@/assets/headbg.jpg'
export default {
  name: "UserInfoMini",
  data(){
    return{
      // 头像背景
      headerBg,
      userInfo:{}
    }
  },
  props: {
    user: {
      type: Object,
      required: true,
      default: () => ({})
    },
    size: {
      type: Number,
      default: 40
    }
  },
  created() {
    if (this.user.username===''){
      this.load()
    }
    else {
      this.userInfo = this.user
    }
  },
  methods:{
    load(){
      this.$http.get('/user/info/'+this.user.id).then(response=>{
        if (response.status===200){
          this.userInfo = response.data.data
        }
      }).catch(error=>{
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
}
.username {
  margin-left: 10px;
  font-weight: bold;
}
</style>
