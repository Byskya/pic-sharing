<template>
  <div>
    <el-input type="textarea" :rows="2" v-model="message" placeholder="请输入消息"></el-input>
    <el-button type="primary" @click="sendMessage">发送消息</el-button>
  </div>
</template>

<script>
export default {
  name: "MessageView",
  data(){
    return{
      message: '',
      socket: null, // WebSocket实例
    }
  },
  created() {
    // 创建WebSocket连接
    this.socket = new WebSocket('ws://localhost:9090/managerMessage');
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
  methods:{
    sendMessage() {
      // 发送消息
      this.socket.send(this.message);
      this.$message.success("通知发布成功")
      // 清空消息
      this.message = '';
    }
  }
}
</script>

<style scoped>

</style>