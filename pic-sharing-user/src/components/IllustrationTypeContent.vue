<template>
  <div>
    <div class="card-list">
      <el-row>
        <el-col :span="24">
          <div class="grid-content bg-purple-dark">
            <h2>{{tag.tagName}}</h2>
          </div>
        </el-col>
      </el-row>
      <el-empty v-if="total===0" description="空空如也" v-loading="loadingWork">
      </el-empty>
      <div v-else>
        <el-row  :gutter="20">
          <el-col :span="4" v-for="(item, index) in cardList" :key="index" :offset="index % 5 === 0 ? 0 : 1">
            <el-card class="card" :body-style="{ padding: '0px' }">
              <img :src="'data:image/png;base64,' + item.imageResource" class="image">
              <div class="content">
                <div class="title">{{ item.title}}</div>
                <div class="bottom clearfix">
                  <time class="time">{{new Date(item.createdAt).toLocaleString()}}</time>
                  <el-button type="text" class="button" @click="toWorkDetail(item)">查看</el-button>
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
export default {
  name: "IllustrationTypeContent",
  data(){
    return{
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
  props:{
    tag:{
      type:Object,
      required:true
    }
  },
  computed:{
    tagId:{
      get(){
        return this.tag.id
      }
    }
  },
  created() {
    this.load()
  },
  watch:{
    tag(newVal){
      this.load()
    }
  },
  methods:{
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
      this.$http.get('/illustration/bytag/'+this.tagId+'/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.data.state===200){
          console.log(response.data.data)
          this.cardList = response.data.data.list
          this.total = response.data.data.total
          this.loadingWork = false
        }
        else {
          this.total = 0
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