<template>
  <div>
    <div style="width: 100%;height: 36px">
      <el-button type="danger" style="float: right" size="60" @click="deleteUserAllHistory">清空</el-button>
    </div>
    <div class="block">
      <el-timeline>
        <el-timeline-item
            v-for="(activity, index) in activities"
            :key="index"
            :timestamp="new Date(activity.updatedAt).toLocaleString()"
            placement="top">
          <WorkInfoMIni :work="activity"></WorkInfoMIni>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script>
import UserInfoMini from "@/components/UserInfoMini";
import WorkInfoMIni from "@/components/WorkInfoMIni";
export default {
  name: "UserWatchHistoryView",
  components: {WorkInfoMIni, UserInfoMini},
  data(){
    return{
      activities:[],
    }
  },
  created() {
    this.load()
  },
  methods:{
    deleteUserAllHistory(){
      this.$confirm('此操作将清空所有浏览记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete('/delete/user/allHistory').then(response=>{
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
    load(){
      this.$http.get('/get/user/watchHistory').then(response=>{
        if (response.status===200){
          console.log(response.data.data)
          this.activities = response.data.data.sort((a,b)=>{
            return new Date(b.updatedAt)-new Date(a.updatedAt);
          });
        }
      }).catch(error=>{
        console.log(error)
      })
    }
  }

}
</script>

<style scoped>

</style>