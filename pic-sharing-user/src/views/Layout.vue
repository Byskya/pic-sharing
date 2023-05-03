<template>
  <div class="main-container">
    <el-drawer
        title="我是标题"
        :visible.sync="drawer"
        :with-header="false"
        direction="ltr"
        size="300px">
      <el-menu :default-active="$route.path" :router="true" class="el-menu-demo" @select="handleSelect">
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/workType">分类</el-menu-item>
        <el-menu-item index="/workRank">排行榜</el-menu-item>
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
        <div style="align-self: flex-end;margin-bottom: 6px;">
          <Avatar></Avatar>
        </div>
      </div>
      <div class="content">
        <router-view></router-view>
      </div>
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
  }
}
</script>

<style scoped>
.main-container {
  position: relative;
  display: flex;
  height: 100vh;
}
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
</style>