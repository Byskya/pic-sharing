<template>
  <div>
    <div>
      <el-table
          :data="tableData.filter(data => !search || data.username.toLowerCase().includes(search.toLowerCase()))"
          style="width: 100%">
        <el-table-column label="用户" >
          <template slot-scope="scope">
            <UserInfoMini :user="scope.row"></UserInfoMini>
          </template>
        </el-table-column>

        <el-table-column
            label="签名"
            prop="name">
        </el-table-column>

        <el-table-column
            align="right">
          <template slot="header" slot-scope="scope">
            <el-input
                v-model="search"
                size="mini"
                placeholder="输入关键字搜索"/>
          </template>
          <template slot-scope="scope">
            <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">查看</el-button>
<!--            <el-button-->
<!--                size="mini"-->
<!--                type="danger"-->
<!--                @click="handleDelete(scope.$index, scope.row)">回关</el-button>-->
          </template>
        </el-table-column>

      </el-table>
    </div>
    <!--    导航栏-->
    <div>
      <div style="margin-top: 20px">
        <el-pagination
            background
            :page-size="5"
            :current-page="params.pageNum"
            :current-size="params.pageSize"
            layout="prev, pager, next"
            @current-change="handleCurrentChange"
            :total="params.total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import UserInfoMini from "@/components/UserInfoMini";

export default {
  name: "UserFansView",
  components: {UserInfoMini},
  data() {
    return {
      params:{
        username:'',
        pageNum:1,
        pageSize:5,
        total:0
      },
      tableData: [],
      search: '',
    }
  },
  // 钩子函数
  created(){
    this.load()
  },
  methods: {
    // 分页导航栏
    handleCurrentChange(pageNum){
      this.params.pageNum = pageNum
      console.log(this.params.pageNum)
      this.load()
    },
    load(){
      this.$http.get('/user/fansList/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.status===200){
          this.tableData = response.data.data.list
          this.params.total = response.data.data.total
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    handleEdit(index, row) {
      console.log(index, row);
      const id = row.id
      const username = ''
      const userInfo = {
        id,
        username
      }
      const userJson = JSON.stringify(userInfo)
      this.$router.push({
        name:'userInfo',
        query:{
          userJson
        }
      })

    },
    handleDelete(index, row) {
      console.log(index, row);
      this.$http.post('/delete/follow/'+row.id).then(response=>{
        if (response.data.state===200){
          this.$message.success("已取消关注")
        }
      }).catch(error=>{
        console.log(error)
      })
    },
  },
}
</script>

<style scoped>

</style>