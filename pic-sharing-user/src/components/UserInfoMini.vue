<template>
  <div class="user-info">
    <el-avatar v-if="user.avatar!=null" :src="'data:image/*;base64,'+user.avatar" :size="size"></el-avatar>
    <el-avatar v-else :src="headerBg" :size="size"></el-avatar>
    <span class="username">{{ user.username }}</span>
  </div>
</template>

<script>
import headerBg from '@/assets/headbg.jpg'
export default {
  name: "UserInfoMini",
  data(){
    return{
      // 头像背景
      headerBg
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
