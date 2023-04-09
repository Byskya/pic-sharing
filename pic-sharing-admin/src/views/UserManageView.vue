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
        style="width: 100%">
      <el-table-column
          fixed
          prop="id"
          label="id"
          width="150">
      </el-table-column>
      <el-table-column
          fixed
          prop="createdAt"
          label="注册日期"
          width="150">
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
          width="100">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
          <el-button type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--    分页功能-->
    <div style="margin-top: 20px">
      <el-pagination
          background
          layout="prev, pager, next"
          :total="tableData.length">
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
      params:{
        username:'',
        pageNum:'1',
        pageSize:'10'
      }
    }
  },
  methods:{
    search(){
      this.axios.get('http://localhost:8081/search/user/'+this.params.username).then(reponse=>{
        this.tableData=reponse.data.data
      })
      .catch(error=>{
        console.log(error)
      })
    }
  },
  mounted() {
    this.axios.get('http://localhost:8081/get/allUser').then(response=>{
      this.tableData=response.data.data
    })
    .catch(error=>{
      console.log(error)
    })
  }
}
</script>

<style scoped>

</style>