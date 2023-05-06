<template>
  <div>
<!--    搜索栏-->
    <div style="margin-bottom: 20px">
      <el-input style="width: 240px" placeholder="请输入名称" v-model="params.username"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="search"><i class="el-icon-search"></i>搜索</el-button>
    </div>
    <el-table
        :data="tableData"
        border
        style="width: 100%"
        :row-class-name="setRowClassName">
      <el-table-column
          fixed
          prop="id"
          label="id"
          width="150">
      </el-table-column>
      <el-table-column
          fixed
          label="注册日期"
          width="150">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ new Date(scope.row.updatedAt).toLocaleDateString() }}</span>
        </template>
      </el-table-column>
      <el-table-column
          prop="username"
          label="姓名"
          width="120">
      </el-table-column>
      <el-table-column
          prop="gender"
          label="性别"
          width="120">
      </el-table-column>
      <el-table-column
          prop="phoneNumber"
          label="手机号码"
          width="120">
      </el-table-column>
      <el-table-column
          prop="birthday"
          label="生日"
          width="120">
      </el-table-column>
      <el-table-column
          prop="address"
          label="地址"
          width="300">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button @click="handleClickUpdate(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="handleClickBan(scope.row)" type="text" size="small">冻结/解冻</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--    分页功能-->
    <div style="margin-top: 20px">
      <el-pagination
          background
          :page-size="5"
          :current-page="params.pageNum"
          :current-size="params.pageSize"
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>

export default {
  name: "UserManageView",
  data() {
    return{
      tableData:[
      ],
      total:0,
      // 判断是搜索结果的分页导航还是所有数据的分页导航
      judge:0,
      params:{
        username:'',
        pageNum:1,
        pageSize:5
      }
    }
  },
  created(){
    this.judge=0
    this.load()
  },
  mounted() {
    this.load()
  },
  methods:{
    //加载后端的用户数据
    load(){
      if(this.judge===0){
        this.axios.get('http://localhost:9090/get/allUser/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
          if(response.status === 200){
            this.tableData=response.data.data.list
            this.total = response.data.data.total
          }
        }).catch(error=>{
          console.log(error)
        })
      }
      else{
        this.axios.get('http://localhost:9090/search/user/'+this.params.username+'/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
          if (response.status === 200){
            this.tableData=response.data.data.list
            this.total = response.data.data.total
          }
        }).catch(error=>{
          console.log(error)
        })
      }
    },
    // 漫画搜索结果页面，分页导航栏数据reset
    reset(){
      this.params.pageSize=5
      this.params.pageNum=1
      this.judge=1
    },
    //根据用户名模糊查询用户数据
    search(){
      this.reset()
      this.axios.get('http://localhost:9090/search/user/'+this.params.username+'/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.status === 200){
          this.tableData=response.data.data.list
          this.total = response.data.data.total
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
    },
    //用户信息编辑
    handleClickUpdate(row) {
      this.$router.push({
        name:'userUpdate',
        query:{
          row
        }
      })
      console.log(row);
    },
    //拉黑用户
    handleClickBan(row){
      if (row.isDelete===0){
        this.axios.put('http://localhost:9090/user/ban/'+row.id).then(response=>{
          if (response.status===200){
            row.isDelete=1
            this.$message.success("冻结完毕")
          }
        }).catch(error=>{
          this.$message.success("冻结失败")
          console.log(error)
        })
      }
      else {
        this.axios.put('http://localhost:9090/user/ban/'+row.id).then(response=>{
          if (response.status===200){
            row.isDelete=0
            this.$message.success("解冻完毕")
          }
        }).catch(error=>{
          this.$message.success("解冻失败")
          console.log(error)
        })
      }
    },
    //根据isDelete属性更换表格行的颜色
    setRowClassName({row,rowIndex}){
      return row.isDelete===1 ? 'ban' : 'notBan';
    }
  }
}
</script>

<style>
.el-table .ban {
  background: #d99494;
}
.el-table .notBan {
}
</style>