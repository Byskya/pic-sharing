<template>
  <div>
    <el-card class="card">
      <div style="display: flex">
        <div @click="toWorkDetail(workInfo)">
          <img v-if="workInfo.imageResource" :src="'data:image/png;base64,'+workInfo.imageResource" alt="work image" class="image"/>
        </div>
        <div style="margin-left: 20px;padding-top: 6px">
          <h3 style="margin-top: auto">{{ workInfo.title }}</h3>
          <UserInfoMini style="margin-top: 92px" :user="{username:'',id:work.userId}" :size="30"></UserInfoMini>
        </div>
        <div style="margin-left: auto;display: flex">
          <el-button type="info" style="align-self: center" icon="el-icon-delete" @click="deleteWorkHistory(work.illustrationId)" circle></el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import UserInfoMini from "@/components/UserInfoMini";
export default {
  name: "WorkInfoMIni",
  components: {UserInfoMini},
  data(){
    return{
      workInfo:{}
    }
  },
  props: {
    work: {
      type: Object,
      required: true,
      default: () => ({})
    },
    size: {
      type: Number,
      default: 40
    }
  },
  created() {
    this.load()
  },
  methods:{
    deleteWorkHistory(workId){
      this.$http.delete('/delete/work/history/'+workId).then(response=>{
        if (response.status===200){
          this.$message.success('删除成功')
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    load(){
      this.$http.get('/work/thumbnail/'+this.work.illustrationId).then(response=>{
        if (response.status===200){
          this.workInfo = response.data.data
          console.log(response.data.data)
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
  }
}
</script>

<style scoped>
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
  border-radius: 10px;
  width: 150px;
  height: 150px;
  object-fit: cover;
}
</style>