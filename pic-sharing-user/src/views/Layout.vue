<template>
  <div class="main-container">
    <div class="nav-container" :class="{'show-nav': showNav}">
      <!-- 导航栏内容 -->
    </div>
    <div class="content-container">
      <!-- 内容区域 -->
      <div class="header">
        <button @click="toggleNav">显示/隐藏导航栏</button>
      </div>
      <div class="content">
        <!-- 内容区域内容 -->
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Layout",
  data() {
    return {
      showNav: false // 导航栏默认隐藏
    }
  },
  methods: {
    toggleNav() {
      this.showNav = !this.showNav;
    }
  }
}
</script>

<style scoped>
.main-container {
  display: flex;
  height: 100vh;
}

.nav-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 200px;
  height: 100%;
  background-color: #ccc;
  transform: translateX(-200px);
  transition: transform 0.3s ease-in-out;
}

.show-nav {
  transform: translateX(0);
}

.content-container {
  flex: 1;
}

.header {
  height: 50px;
  background-color: #eee;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 20px;
}

.content {
  padding: 20px;
}

</style>