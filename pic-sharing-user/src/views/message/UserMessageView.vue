<template>
  <div class="message-list">
    <div class="message-header">
      <h3>消息列表</h3>
    </div>
    <el-divider></el-divider>
    <el-scrollbar wrap-class="message-content" class="message-content">
      <div v-for="(message, index) in messageList" :key="index" class="message-item" :class="{'unread': !message.isRead}">
        <div class="message-avatar">
          <el-avatar :size="48" :src="message.avatar"></el-avatar>
        </div>
        <div class="message-info" @click="toUserMessage(message.id)">
          <div class="message-meta">
            <span class="message-sender">{{ message.sender }}</span>
            <span class="message-time">{{ message.time }}</span>
          </div>
          <div class="message-body">
            {{ message.content }}
            <el-badge v-show="message.notReadMessageNumber!==0" :value="message.notReadMessageNumber" style="float: right">
            </el-badge>
          </div>
        </div>
        <div class="message-actions">
          <el-button size="small" type="text" @click="markAsRead(index)" v-show="!message.isRead">标为已读</el-button>
          <el-button size="small" type="text" @click="deleteMessage(index)">删除</el-button>
        </div>
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
export default {
  name: "UserMessageView",
  data() {
    return {
      messageList: [
      ],
      messageNumber:0,
    };
  },
  created() {
    this.load()
    this.loadMessageNumber()
  },
  methods: {
    loadMessageNumber(){
      this.$http.get('/message/number').then(response=>{
        if (response.status===200){
          this.messageNumber = response.data.data
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    // 跳转到用户聊天界面
    toUserMessage(otherUserId){
      const sender = this.$store.state.user.id
      const receiver = otherUserId
      this.$router.push({
        name:'userChat',
        query:{
          sender,
          receiver
        }
      })
    },
    load(){
      this.$http.get('/receive/allUserMessages').then(response=>{
        if (response.status === 200){
          for(let i = 0; i<response.data.data.userList.length;i++){
            this.messageList.push({
              id : response.data.data.userList[i].id,
              sender: response.data.data.userList[i].username,
              time: new Date(response.data.data.userList[i].createdAt).toLocaleString(),
              content: '',
              avatar: 'data:image/*;base64,'+response.data.data.userList[i].avatar,
              isRead: false,
              notReadMessageNumber:0,
            })
          }
        for (let i = 0;i<response.data.data.messageList.length;i++){
          for(let j = 0;j<this.messageList.length;j++){
            if (this.messageList[j].id===response.data.data.messageList[i].senderId || this.messageList[j].id===response.data.data.messageList[i].receiverId){
              this.messageList[j].content = response.data.data.messageList[i].message
              if (this.messageList[j].id===response.data.data.messageList[i].senderId && response.data.data.messageList[i].read === false){
                this.messageList[j].notReadMessageNumber++
              }
            }
          }
        }
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    markAsRead(index) {
      this.messageList[index].isRead = true;
    },
    deleteMessage(index) {
      this.messageList.splice(index, 1);
    },
  },
}
</script>

<style scoped>
.message-list {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.message-header {
  padding-top: 10px;
  padding-left: 20px;
}

.message-content {
  flex: 1;
  padding: 10px;
}

.message-item {
  display: flex;
  align-items: center;
  padding: 10px;
}

.message-item:hover {
  background-color: #f5f7fa;
}

.message-avatar {
  margin-right: 10px;
}

.message-info {
  flex: 1;
}

.message-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.message-sender {
  font-weight: bold;
  margin-right: 10px;
}

.message-actions {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.unread {
  font-weight: bold;
}
</style>