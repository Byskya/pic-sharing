<template>
  <div class="card-list">
    <el-empty v-if="total===0" v-loading="loading">
      <el-button type="primary" @click="toUploadView">投稿作品</el-button>
    </el-empty>
    <div v-else>
      <el-row  :gutter="20">
        <el-col :span="4" v-for="(item, index) in cardList" :key="item.id" :offset="index % 5 === 0 ? 0 : 1">
          <el-card class="card" :body-style="{ padding: '0px' }">
            <img :src="'data:image/png;base64,' + item.imageResource" class="image">
            <div class="content">
              <div class="title">{{ item.title}}
              <span v-if="!item.approved" style="color:red">(审核未通过)</span>
              </div>
              <div class="bottom clearfix">
                <time class="time">{{new Date(item.createdAt)}}</time>

                <el-dropdown v-if="item.approved">
                  <span class="el-dropdown-link">
                    <el-button type="text" class="button">操作</el-button>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item><a @click="toWorkDetail(item)">查看</a></el-dropdown-item>
                    <el-dropdown-item><a @click="uploadAgain(item)">修改</a></el-dropdown-item>
                    <el-dropdown-item><a @click="deleteWork(item)">删除</a></el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
                <el-button v-else type="text" class="button" @click="toReviewSchedule(item)">查看审核进度</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <div style="display: flex; justify-content: center;margin-top: 20px">
        <el-pagination
            background
            :page-size="10"
            :current-page="params.pageNum"
            :current-size="params.pageSize"
            layout="prev, pager, next"
            @current-change="handleCurrentChange"
            :total="total">
        </el-pagination>
      </div>
      <div style="display: flex; justify-content: center; height: 150px">
        <el-button type="primary" style="height: 50px;align-self: center" @click="toUploadView">上传<i class="el-icon-upload el-icon--right"></i></el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "MyWorksView",
  data() {
    return {
      loading: true,
      currentDate: Date,
      // 用户的插画列表
      cardList:[],
      // 插画的数量
      total:0,
      params:{
        userid: Number,
        pageNum: 1,
        pageSize: 10
      }
    };
  },
  created() {
    this.load()
  },
  mounted() {
    this.load()
  },
  methods:{
    deleteWork(item){
      this.$confirm('此操作将永久删除该作品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete('/delete/work/'+item.id).then(response=>{
          if (response.status===200){
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          }
        }).catch(error=>{
          console.log(error)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    uploadAgain(item){
      const workId = item.id
      this.$router.push({
        name:'editApprovedWork',
        query:{
          workId
        }
      })
    },
    toReviewSchedule(item){
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
        name:'reviewSchedule',
        query:{
          itemJson
        }
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
      this.$http.get('/user/allIllustration/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.data.state===200){
          this.cardList = response.data.data.list
          this.total = response.data.data.total
          this.loading = false
        }
        else {
          this.loading = false;
          this.total = response.data.data.total
        }
      }).catch(error=>{
        console.log(error)
      });
    },
    toUploadView(){
      this.$router.push({
        name:'uploadWork'
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
.time {
  font-size: 13px;
  color: #999;
}
.card {
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
  border-radius: 4px;
  overflow: hidden;
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
  margin-bottom: 10px;
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
.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>