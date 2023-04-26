<template>
  <div>
    <div class="block">
      <div class="radio" style="margin-bottom: 30px;">
        排序：
        <el-radio-group v-model="reverse">
          <el-radio :label="true">倒序</el-radio>
          <el-radio :label="false">正序</el-radio>
        </el-radio-group>
      </div>

      <el-timeline :reverse="reverse">
        <el-timeline-item
            v-for="(activity, index) in activities"
            :key="index"
            :timestamp="activity.timestamp">
          {{activity.content}}
        </el-timeline-item>
      </el-timeline>
    </div>
    <div>
      <el-button type="danger" v-if="judge" @click="uploadAgain">
        再次申请
      </el-button>
    </div>
  </div>

</template>

<script>
export default {
  name: "ReviewScheduleView",
  data() {
    return {
      reverse: true,
      activities:[],
      workInfo:{},
      judge:false
    };
  },
  // 钩子函数
  created() {
    this.workInfo = JSON.parse(this.$route.query.itemJson.toString())
    this.load()
  },
  methods:{
    uploadAgain(){
      const workId = this.workInfo.id
      this.$router.push({
        name:'editWork',
        query:{
          workId
        }
      })
    },
    load(){
      this.axios.get('http://localhost:9090/work/timeline/'+this.workInfo.id).then(response=>{
        if (response.status===200){
          console.log("获取成功")
          console.log(response.data.data.illustration)
          console.log(response.data.data.auditInfo)
          if (response.data.data.illustration.approved===false){
            this.activities.push({
              content:'创建成功，待审核',
              timestamp: new Date(response.data.data.illustration.createdAt).toLocaleString()
            })
            if (response.data.data.auditInfo.status==='不通过'){
              this.activities.push({
                content:'不通过'+'原因:'+response.data.data.auditInfo.feedback,
                timestamp: new Date(response.data.data.auditInfo.updatedAt).toLocaleString(),
              })
              this.judge = true
            }
          }
        else {
            this.activities.push({
              content:'创建成功，待审核',
              timestamp: new Date(response.data.data.illustration.createdAt).toLocaleString()
            })
            this.activities.push({
              content:'通过',
              timestamp: new Date(response.data.data.auditInfo.updatedAt).toLocaleString()
            })
          }
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