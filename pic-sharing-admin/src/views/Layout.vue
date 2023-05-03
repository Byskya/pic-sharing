<template>
  <div v-if="!isLoggedIn">您尚未登录，请先登录</div>
  <div v-else>
    <!--    头部区域-->
    <div style="height: 60px; line-height: 60px;background-color: white">
      <img src="@/assets/logo.png" alt="logo" style="width: 40px;position: relative; top: 10px; left: 20px">
      <span style="margin-left: 30px; font-size: 24px">插画分享网站后台管理系统</span>
    </div>
    <!--    导航栏和内容区-->
    <div style="display: flex">
      <!--      导航区-->
      <div style="width: 300px; min-height: 100vh; overflow: hidden; margin-right: 2px; background-color: white">
        <el-menu :default-active="$route.path" router class="el-menu-demo" @select="handleSelect">
          <el-menu-item index="/userManage">
            <i class="el-icon-user-solid"></i>
            <span>用户管理</span>
          </el-menu-item>
          <el-submenu index="/pictureManage">
            <template slot="title">
              <i class="el-icon-picture"></i>
              <span>作品管理</span>
            </template>
            <el-menu-item index="/reviewWork">审核</el-menu-item>
            <el-menu-item index="/removeWork">下架</el-menu-item>
          </el-submenu>
          <el-submenu index="/message">
            <template slot="title">
              <i class="el-icon-message-solid"></i>
              <span>消息中心</span>
            </template>
            <el-menu-item index="/message">
              消息发布
            </el-menu-item>
            <el-menu-item index="/messageEdit">消息编辑</el-menu-item>
          </el-submenu>
          <el-menu-item @click="loginOut">
            <i class="el-icon-switch-button"></i>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
      </div>
      <!--      内容区-->
      <div style="width: 100%">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Layout",
  data() {
    return {
      isLoggedIn: localStorage.getItem('isLoggedIn') === 'true',
    }
  },
  methods:{
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    loginOut(){
      localStorage.setItem('isLoggedIn','false')
      this.$message.success("退出登录成功")
      window.location.reload();
    }
  }
}
</script>

<style scoped>

</style>