<template>
  <div class="home">
    <!-- 内容区域内容 -->
    <div>
      <el-row>
        <el-col :span="24">
          <div class="grid-content bg-purple-dark">
            <h2>关注用户的作品</h2>
          </div>
        </el-col>
      </el-row>
      <el-row v-if="followWorkList" :gutter="20" style="display: flex;justify-content: center">
        <el-carousel :interval="4000" type="card" height="250px" :autoplay="false"  style="width: 1000px">
          <el-carousel-item v-for="item in followWorkList" :key="item.id">
            <el-card class="card" :body-style="{ padding: '0px' }">
              <img :src="'data:image/png;base64,' + item.imageResource" class="image" @click="toWorkDetail(item)">
              <div class="content">
                <div class="title">{{ item.title}}</div>
                <div class="bottom clearfix">
                  <time class="time">{{new Date(item.createdAt).toLocaleString()}}</time>
<!--                  <el-button type="text" class="button" @click="toWorkDetail(item)">查看</el-button>-->
                </div>
              </div>
            </el-card>
          </el-carousel-item>
        </el-carousel>
      </el-row>
      <el-empty v-else description="空白" v-loading="loadingWork">
      </el-empty>
    </div>
    <div class="card-list">
      <el-row>
        <el-col :span="24">
          <div class="grid-content bg-purple-dark">
            <h2>最新作品</h2>
          </div>
        </el-col>
      </el-row>
      <el-empty v-if="total===0" description="加载中" v-loading="loadingWork">
      </el-empty>
      <div v-else>
        <el-row  :gutter="20">
          <el-col :span="4" v-for="(item, index) in cardList" :key="item.id" :offset="index % 5 === 0 ? 0 : 1">
            <el-card class="card" :body-style="{ padding: '0px' }">
              <img :src="'data:image/png;base64,' + item.imageResource" class="image" @click="toWorkDetail(item)">
              <div class="content">
                <div class="title">{{item.title}}</div>
                <div class="bottom">
                  <UserInfoMini :size="20" :user="{username:'',id: item.userId}"></UserInfoMini>
                  <time class="time">{{new Date(item.createdAt).toLocaleDateString()}}</time>
<!--                  <el-button type="text" class="button" @click="toWorkDetail(item)">查看</el-button>-->
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <div style="display: flex; justify-content: center;margin-top: 20px">
          <el-pagination
              background
              :current-page="params.pageNum"
              :current-size="params.pageSize"
              layout="prev, pager, next"
              @current-change="handleCurrentChange"
              :total="total">
          </el-pagination>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import UserInfoMini from "@/components/UserInfoMini";
export default {
  name: 'HomeView',
  components: {UserInfoMini},
  data(){
    return{
      followWorkList:[],
      // 关注作品的数量
      followWorkTotal:Number,
      cardList:[],
      loadingWork: true,
      currentDate: Date,
      total:0,
      params:{
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.load()
    this.loadFollowWorks()
  },
  methods:{
    loadFollowWorks(){
      this.$http.get('/following/works').then(response=>{
        if (response.status===200){
          console.log(response.data.data)
          this.followWorkList = response.data.data
          this.followWorkTotal = response.data.data.length
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    toWorkDetail(item){
      const id = item.id
      const title = item.title
      const userId = item.userId
      const workInfo = {
        id,
        title,
        userId,
      }
      const itemJson = JSON.stringify(workInfo)
      this.$router.push({
        name:'workDetail',
        query:{
          itemJson
        }
      })
    },
    load(){
      this.$http.get('/work/all/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.data.state===200){
          this.cardList = response.data.data.list
          this.total = response.data.data.total
          this.loadingWork = false
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    // 分页导航栏
    handleCurrentChange(pageNum){
      this.params.pageNum = pageNum
      console.log(this.params.pageNum)
      this.load()
    }
  }
}
</script>
<style scoped>
.el-row {
  margin-bottom: 20px;
&:last-child {
   margin-bottom: 0;
 }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.time {
  font-size: 13px;
  color: #999;
}
.card {
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 5px;
  transition: box-shadow 0.3s ease;
}
.card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card .image {
  display: block;
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.card .content {
  padding: 20px;
}

.card .title {
  font-size: 20px;
  font-weight: bold;
  /*margin-bottom: 10px;*/
}

.card .bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  color: #999;
  margin-top: 13px;
  line-height: 12px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>