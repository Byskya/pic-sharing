<template>
  <div class="main-container">
    <el-drawer
        title="我是标题"
        :visible.sync="drawer"
        :with-header="false"
        direction="ltr"
        size="300px">
      <el-menu :default-active="$route.path" :router="true" class="el-menu-demo" @select="handleSelect">
        <el-menu-item index="home">首页</el-menu-item>
        <el-menu-item index="workType">分类</el-menu-item>
        <el-menu-item index="workRank">排行榜</el-menu-item>
      </el-menu>
    </el-drawer>
    <div class="content-container">
      <!-- 内容区域 -->
      <div class="header">
        <el-button @click="drawer = true" icon="el-icon-s-unfold"  type="primary">
        </el-button>
        <div style="align-self: center;background-color: aqua">
          <Search></Search>
        </div>
        <div style="align-self: flex-end;margin-bottom: 6px">
          <Avatar></Avatar>
        </div>
      </div>
      <div class="content">
        <router-view></router-view>
      </div>
<!--      旧版的侧边导航栏-->
<!--      &lt;!&ndash; 遮罩层 &ndash;&gt;-->
<!--      <div class="mask" v-show="sidebarOpen" @click="closeSidebar"></div>-->
    </div>
  </div>
</template>

<script>
import Avatar from "@/components/Avatar";
import Search from "@/components/Search";
export default {
  name: "Layout",
  components: {Search, Avatar},
  data() {
    return {
      // showNav: false, // 导航栏默认隐藏
      // sidebarOpen: false,
      drawer: false,
      user:{}
    }
  },
  computed:{

  },
  created() {
    console.log("加载页面")
    this.load()
  },
  methods: {
    // 导航栏相关方法
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    load(){
      this.axios.get('http://localhost:9090/user/get/info').then(response=>{
        if (response.status===200){
          console.log("加载成功")
          this.user = response.data.data
          this.$store.dispatch('login',this.user)
        }
        else {
          this.$message.success(response.data.data.message)
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    // 旧版的侧边导航栏
    // toggleNav() {
    //   this.showNav = !this.showNav;
    //   this.sidebarOpen = !this.sidebarOpen;
    // },
    // closeSidebar() {
    //   if (this.sidebarOpen) {
    //     this.sidebarOpen = false;
    //     this.showNav = false;
    //   }
    // }
  }
}
</script>

<style scoped>
.main-container {
  position: relative;
  display: flex;
  height: 100vh;
}
/*.nav-container {*/
/*  position: fixed;*/
/*  top: 0;*/
/*  left: 0;*/
/*  width: 200px;*/
/*  height: 100%;*/
/*  background-color: #ccc;*/
/*  transform: translateX(-200px);*/
/*  transition: transform 0.3s ease-in-out;*/
/*  z-index: 991;*/
/*}*/
/*.show-nav {*/
/*  transform: translateX(0);*/
/*}*/
.content-container {
  flex: 1;
}
.header {
  height: 60px;
  background-color: #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 10px;
  padding-left: 10px;;
}
.content {
  padding: 20px;
}
/*.mask {*/
/*  position: absolute;*/
/*  top: 0;*/
/*  left: 0;*/
/*  width: 100%;*/
/*  height: 100%;*/
/*  background-color: rgba(0, 0, 0, 0.5);*/
/*  !*是CSS中用来控制元素层级关系的属性。它控制元素在堆叠顺序中的显示优先级。*!*/
/*  !*具体来说，z-index值越大的元素，就会显示在其他元素的上方。*!*/
/*  z-index: 990;*/
/*}*/
</style>