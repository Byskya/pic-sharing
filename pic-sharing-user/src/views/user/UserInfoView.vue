<template>
  <div>
    <!--  背景-->
    <div class="bg" style="background-color: white;overflow: hidden;height: 400px">
      <img src="@/assets/bg.png" style="overflow: hidden" width="100%" alt="bg">
    </div>

    <!--  个人信息区别-->
    <div class="myInfo">
      <!--    个人信息-->
      <div style="align-self: flex-start">
        <div style="display: flex;justify-content: space-between;">
          <div>
            <UserInfoMini v-if="userInfo.username!==''" :user="userInfo" :size="80"></UserInfoMini>
            <el-button @click="displayWin">查看个人信息</el-button>
          </div>
        </div>
      </div>
      <div style="align-self: flex-end">
        <el-button icon="el-icon-message" type="info" @click="toUserMessage">私信</el-button>
        <el-button v-if="isFollow" type="danger" @click="deleteFollow">已关注</el-button>
        <el-button v-else type="danger" @click="follow">关注</el-button>
      </div>
    </div>
    <!--  作品区-->
    <div>
      <el-row>
        <el-col :span="24">
          <div class="grid-content bg-purple-dark">
            <h2>个人作品区</h2>
          </div>
        </el-col>
      </el-row>
      <div class="card-list">
        <el-empty v-if="total===0" description="空空如也">
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
    <div>
      <component v-if="show" :is="popup" @close="hidePopup" :user="user" :showContent="judge"></component>
    </div>
  </div>
</template>

<script>
import Avatar from "@/components/Avatar";
import UserInfo from "@/components/UserInfo";
import UserInfoMini from "@/components/UserInfoMini";

export default {
  name: "UserInfoView",
  components: {UserInfoMini, Avatar},
  data(){
    return{
      isFollow:false,
      user:{},
      show: false,
      popup: null,
      // 决定弹窗显示内容
      judge: true,
      cardList:[],
      loading: true,
      currentDate: Date,
      total:0,
      params:{
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  computed:{
    userInfo:{
      get(){
        return this.user
      }
    }
  },
  created() {
    this.user = JSON.parse(this.$route.query.userJson.toString())
    this.load()
    this.loadUserWorks()
    this.$bus.$on('showModal', () => {
      this.popup = UserInfo
      this.show = true
    })
    this.$bus.$on('closeWin',()=>{
      this.show = false
    })
    this.$bus.$on('displayEditUI',()=>{
      this.judge = false
    })
  },
  async mounted() {
    await this.sleep(100);
    // 检查当前用户是否关注了作者
    this.checkFollow()
  },
  methods:{
    // 跳转到用户聊天界面
    toUserMessage(){
      const sender = this.$store.state.user.id
      const receiver = this.userInfo.id
      this.$router.push({
        name:'userChat',
        query:{
          sender,
          receiver
        }
      })
    },
    //睡眠方法，解决异步处理顺序的问题
    sleep(ms) {
      return new Promise((resolve) => setTimeout(resolve, ms));
    },
    // 检测是否关注
    checkFollow(){
      this.$http.get('/check/isFollow/'+this.userInfo.id).then(response=>{
        if (response.data.state===200){
          this.isFollow = true
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    follow(){
      // 关注逻辑
      this.$http.post('/follow/'+this.userInfo.id).then(response=>{
        if (response.data.state===200){
          this.$message.success("关注成功")
        }
      }).catch(error=>{
        console.log(error)
      })

    },
    // 取消关注
    deleteFollow(){
      this.$http.post('/delete/follow/'+this.userInfo.id).then(response=>{
        if (response.data.state===200){
          this.$message.success("已取消关注")
          this.isFollow = false
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    displayWin(){
      this.judge = true
      this.$bus.$emit('showModal')
    },
    displayWin2(){
      this.judge = false
      this.$bus.$emit('showModal')
    },
    // 跳转到作品详情页
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
    loadUserWorks(){
      this.$http.get('/user/work/'+this.userInfo.id+'/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.data.state===200){
          this.cardList = response.data.data.list
          this.cardList = response.data.data.list
          this.total = response.data.data.total
          this.loading = false
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    // 分页导航栏
    handleCurrentChange(pageNum){
      this.params.pageNum = pageNum
      console.log(this.params.pageNum)
      this.loadUserWorks()
    },
    load(){
      this.$http.get('/user/info/'+this.userInfo.id).then(response=>{
        if (response.status===200){
          this.user = response.data.data
        }
        else {
          this.$message.success(response.data.data.message)
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    hidePopup() {
      this.show = false
      //关闭遮蔽层
      this.$bus.$emit('closeModal')
    }
  }
}
</script>

<style scoped>
.myInfo{
  display: flex;
  justify-content: space-between;
}
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
</style>