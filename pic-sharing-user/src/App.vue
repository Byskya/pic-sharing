<template>
  <div id="app">
<!--    在根组件中添加一个遮蔽层，弹窗后激活，关闭弹窗后取消-->
    <div v-if="show" class="modal" @click="hideModal"></div>
    <router-view/>
  </div>
</template>
<script>

import MyInfoView from "@/views/user/MyInfoView";
import UserInfo from "@/components/UserInfo";

export default {
  data() {
    return {
      show: false
    }
  },
  components:{
    MyInfoView,
    UserInfo
  },
  mounted() {
    this.$bus.$on('showModal', () => {
      this.show = true
    })
    this.$bus.$on('closeModal',()=>{
      this.hideModal()
    })
  },
  methods: {
    hideModal() {
      this.show = false
      this.$bus.$emit('closeWin')
    }
  }
}
</script>
<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}
</style>
