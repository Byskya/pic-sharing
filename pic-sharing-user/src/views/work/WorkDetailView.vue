<template>
  <div class="illustration-detail">
    <!-- 左侧区域显示插画图像 -->
    <div class="illustration-image">
      <el-image v-if="illustration.imageResource"
          :src="'data:image/*;base64,' +illustration.imageResource"
          :preview-src-list="srcList">
      </el-image>
    </div>
    <!-- 右侧区域显示插画信息 -->
    <div class="illustration-info">
      <el-card>
        <!-- 插画标题 -->
        <h2>{{ illustration.title }}</h2>
        <!-- 插画描述 -->
        <p>{{ illustration.description }}</p>
        <!-- 插画作者和上传日期 -->
        <div class="author-date">
          <span>
            <UserInfoMini :user="authorInfo"></UserInfoMini>
          </span>
          <span>{{ new Date(illustration.createdAt) }}</span>
        </div>
<!--        一些收藏数，浏览数，点赞数的数据显示-->
        <div>
          <p>浏览数：{{viewsInfo}}</p>
        </div>
        <!-- 标签和评论折叠面板 -->
        <el-collapse>
          <el-collapse-item title="标签">
            <!-- 标签列表 -->
            <ul class="tag-list">
              <li v-for="tag in illustration.tags" :key="tag.id">
                <div style="height: 6px"></div>
                <el-tag type="success">{{ tag.tagName }}</el-tag>
              </li>
            </ul>
          </el-collapse-item>
          <el-collapse-item title="评论">
            <UserInfoMini v-if="userInfo" :user="userInfo" :size="50"></UserInfoMini>
<!--            评论区-->
            <CommentList :illustration="workIndex"></CommentList>
          </el-collapse-item>
        </el-collapse>
        <!-- 操作按钮 -->
        <div class="actions">
          <el-button @click="like">点赞{{illustration.likes}}</el-button>
          <div v-if="isMine">
            <el-button>mine</el-button>
          </div>
          <div v-else>
            <el-button v-if="isCollect" @click="deleteFavorite">已收藏</el-button>
            <el-button v-else @click="favorite">收藏</el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>

import CommentList from "@/components/CommentList";
import UserInfoMini from "@/components/UserInfoMini";
export default {
  name: 'WorkDetailView',
  components: {UserInfoMini, CommentList},
  data(){
    return{
      authorInfo:{
        id:Number,
        username:'',
      },
      workIndex:{},
      illustration:{},
      srcList:[],
      views: 0,
      isCollect:false,
      isMine:false
    }
  },
  computed:{
    userInfo:{
      get(){
        return this.$store.state.user
      }
    },
    viewsInfo:{
      get(){
        this.views = this.illustration.views
        return this.views
      }
    }
  },
  created() {
    this.workIndex = JSON.parse(this.$route.query.itemJson.toString())
    this.authorInfo.id = this.workIndex.userId
    this.load()
  },
  async mounted() {
    await this.sleep(100);
    // 添加浏览监听器
    this.$el.addEventListener('click',this.incrementView())
    // 检查当前用户是否收藏了作品
    this.checkCollectLikes()
    // 检查当前作品的作者和用户是否是一个人
    if (this.authorInfo.id === this.userInfo.id){
      this.authorInfo = this.userInfo
    }
  },
  methods: {
    // 取消收藏
    deleteFavorite(){
      this.axios.post('http://localhost:9090/delete/collect/'+this.illustration.id).then(response=>{
        if (response.data.state===200){
          this.$message.success("已取消收藏")
          this.isCollect = false
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    // 检测是否收藏和点赞
    checkCollectLikes(){
      if (this.illustration.userId === this.userInfo.id){
        this.isMine = true
      }
      else {
        this.axios.get('http://localhost:9090/check/isCollect/'+this.illustration.id).then(response=>{
          if (response.data.state===200){
            this.isCollect = true
          }
        }).catch(error=>{
          console.log(error)
        })
      }
    },
    //睡眠方法，解决异步处理顺序的问题
    sleep(ms) {
      return new Promise((resolve) => setTimeout(resolve, ms));
    },
    incrementView(){
      this.axios.post('http://localhost:9090/up/views/'+this.illustration.id).then(response=>{
        if (response.data.state===200){
          this.illustration.views = response.data.data.views
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    loadTags(){
      this.axios.get('http://localhost:9090/work/tags/'+this.workIndex.id).then(response=>{
        if (response.data.state === 200){
          this.$message.success("获取标签成功")
          this.illustration.tags = response.data.data;
        }
      }).catch(error=>{
        console.log(error)
      });
    },
    load(){
      this.axios.get('http://localhost:9090/work/'+this.workIndex.id).then(response=>{
        if (response.data.state===200){
          this.illustration = response.data.data
          // 请求返回作品数据后，为图片添加一个大图预览功能
          const base64Str = 'data:image/*;base64,'+this.illustration.imageResource
          const blob = this.$base64ToBlob(base64Str)
          this.srcList.push(URL.createObjectURL(blob))
          // 加载作品标签
          this.loadTags()
        }
      }).catch(error=>{
        console.log(error)
      });
    },
    like() {
      // 点赞逻辑
      this.axios.post('http://localhost:9090/up/likes/'+this.illustration.id).then(response=>{
        if (response.data.state===200){
          this.illustration.likes = response.data.data.likes
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    favorite() {
      // 收藏逻辑
      this.axios.post('http://localhost:9090/collect/'+this.illustration.id).then(response=>{
        if (response.data.state===200){
          this.$message.success("收藏成功")
        }
      }).catch(error=>{
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
.illustration-detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
}
.illustration-image {
  width: 50%;
  margin-right: 20px;
}
.illustration-image img {
  width: 100%;
}
.illustration-info {
  width: 50%;
}
.author-date {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
.tag-list,
.comment-list {
  margin: 0;
  padding: 0;
  list-style: none;
}
.comment-header {
  display: flex;
  justify-content: space-between;
}
.comment-body {
  margin-top: 10px;
}
.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}
</style>
