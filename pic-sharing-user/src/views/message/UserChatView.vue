<template>
  <div class="message-container">
    <div v-for="message in messages" :key="message.id">
      <div v-if="message.senderId === currentUser.id" class="message right">
        <div class="message-bubble">{{ message.message }}</div>
        <div class="message-avatar">
          <img :src="setAvatar(message.senderId)" alt="avatar" />
        </div>
      </div>
      <div v-else class="message left">
        <div class="message-avatar">
          <img :src="setAvatar(message.senderId)" alt="avatar" />
        </div>
        <div class="message-bubble">{{ message.message }}</div>
      </div>
    </div>
    <form class="message-form" @submit.prevent="sendMessage">
      <el-input
          v-model="newMessage"
          placeholder="输入消息..."
          type="text"
          autofocus
          class="message-input"
      >
        <el-button slot="append" type="primary" @click="sendMessage">
          发送
        </el-button>
      </el-input>
    </form>
  </div>
</template>

<script>

export default {
  name: "UserChatView",
  data() {
    return {
      currentUser:{},
      otherUser:{},
      messages: [
      ],

      newMessage: "",
    };
  },
  created() {
    this.openWebsocket()
    this.load()
  },
  methods: {
    openWebsocket(){
      // 创建WebSocket连接
      this.socket = new WebSocket('ws://192.168.31.46:9090/UserChat');
      this.socket.onopen = () => {
        console.log('WebSocket连接已打开');
      };
      this.socket.onmessage = (event) => {
        console.log('收到消息:', event.data);
      };
      this.socket.onclose = () => {
        console.log('WebSocket连接已关闭');
      };
    },
    //根据isDelete属性更换表格行的颜色
    setAvatar(senderId){
      return senderId===this.currentUser.id ? 'data:image/*;base64,'+this.currentUser.avatar : 'data:image/*;base64,'+this.otherUser.avatar;
    },
    load(){
      const senderId = this.$route.query.sender
      const receiver = this.$route.query.receiver
      // 加载用户信息和聊天记录
      this.$http.get('/user/chat/'+senderId+'/'+receiver).then(response=>{
        if (response.status===200){
          this.currentUser = response.data.data.sender
          this.otherUser = response.data.data.receiver
          this.messages = response.data.data.chatMessages
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    sendMessage() {
      if (!this.newMessage) {
        return;
      }

      // 发送消息
      this.socket.send(this.newMessage);
      this.$message.success("通知发布成功")
      // 清空消息
      this.newMessage = '';

      this.$http.post('/send/message/'+this.currentUser.id+'/'+this.otherUser.id+'/'+this.newMessage).then(response=>{
        if (response.status === 200){
          console.log("发送信息成功")
          this.messages.push({
            senderId : this.currentUser.id,
            message: this.newMessage,
            senderName: this.currentUser.username,
            senderAvatar: 'data:image/*;base64,'+this.currentUser.avatar,
          });
          this.newMessage = "";
        }
      }).catch(error=>{
        console.log(error)
      })
    },
  },
}
</script>

<style scoped>
.message-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
}

.message {
  display: flex;
  margin-bottom: 15px;
}
.right{
  float: right;
}
.left{
  float: left;
}
.right .message-bubble {
  background-color: #5eb95e;
  color: white;
  border-radius: 10px 10px 0 10px;
  padding: 10px 15px;
  max-width: 70%;
  word-wrap: break-word;
}

.left .message-bubble {
  background-color: #eee;
  color: black;
  border-radius: 10px 10px 10px 0;
  padding: 10px 15px;
  max-width: 70%;
  word-wrap: break-word;
}

.message-avatar {
  margin-right: 10px;
  margin-top: auto;
  margin-bottom: auto;
  width: 40px;
  height: 40px;
}

.message-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.message-form {
  display: flex;
  margin-top: auto;
}
.message-input {
  width: 100%;
}

.message-input .el-input__inner {
  border-radius: 20px;
  padding: 10px 20px;
  border: none;
  background-color: #eee;
  box-shadow: none;
}

.message-input .el-input__suffix {
  padding-left: 10px;
  padding-right: 10px;
}

.message-input .el-button--primary {
  border-radius: 20px;
  background-color: #5eb95e;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
}

.message-input .el-button--primary:hover {
  background-color: #4da34d;
}
</style>