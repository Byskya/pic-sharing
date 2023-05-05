<template>
  <div>
    <el-card class="card" v-for="(work, index) in works" :key="work.id">
      <div style="display: flex;">
        <div slot="header">
          <span class="rank">{{ index + 1 }}</span>
        </div>
        <div @click="toWorkDetail(work)">
          <img :src="'data:image/png;base64,'+work.imageResource" alt="work image" class="image"/>
          <h3>{{ work.title }}</h3>
        </div>
        <div style="margin-left: auto; margin-top: auto">
          <UserInfoMini :user="{username:'',id:work.userId}"></UserInfoMini>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import UserInfoMini from "@/components/UserInfoMini";
export default {
  name: "WorkRankView",
  components: {UserInfoMini},
  data() {
    return {
      works: [
      ],
    };
  },
  mounted() {
    this.load()
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
      this.$http.get('/rank/works').then(response=>{
        if (response.status===200){
          console.log(response.data.data)
          this.works = response.data.data
        }
      }).catch(error=>{
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
.rank {
  font-size: 24px;
  font-weight: bold;
  margin-right: 10px;
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
  width: 200px;
  height: 200px;
  object-fit: cover;
}
</style>