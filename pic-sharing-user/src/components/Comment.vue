<template>
  <el-card>
    <div slot="header">
      <img :src="'data:image/*;base64,'+comment.avatar" class="comment-avatar">
      <span class="comment-username">{{ comment.username }}</span>
      <span class="comment-time">{{ new Date(comment.createdAt) }}</span>
    </div>
    <div>{{ comment.content }}</div>
  </el-card>
</template>

<script>
export default {
  name:'Comment',
  props: {
    comment: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.comment-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}
.comment-username {
  margin-left: 10px;
  margin-right: 10px;
  font-weight: bold;
}
.comment-time {
  font-size: 12px;
  color: #999;
}
</style>
