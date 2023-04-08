<template>
  <div>
<!--    搜索栏-->
    <div style="margin-bottom: 20px">
      <el-input style="width: 240px" placeholder="请输入名称"></el-input>
      <el-button style="margin-left: 5px" type="primary"><i class="el-icon-search"></i>搜索</el-button>
    </div>
    <el-table
        :data="tableData"
        border
        style="width: 100%">
      <el-table-column
          fixed
          prop="date"
          label="注册日期"
          width="150">
      </el-table-column>
      <el-table-column
          prop="name"
          label="姓名"
          width="120">
      </el-table-column>
      <el-table-column
          prop="gender"
          label="性别"
          width="120">
      </el-table-column>
      <el-table-column
          prop="phone"
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
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄',
        gender: '男',
        birthday: '2000-09-09',
        phone: '13189387152',
        zip: 200333
      }, {
        date: '2016-05-04',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1517 弄',
        gender: '男',
        birthday: '2000-09-09',
        phone: '13189387152',
        zip: 200333
      }, {
        date: '2016-05-01',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1519 弄',
        gender: '男',
        birthday: '2000-09-09',
        phone: '13189387152',
        zip: 200333
      }, {
        date: '2016-05-03',
        name: '王小虎',
        province: '上海',
        city: '普陀区',
        address: '上海市普陀区金沙江路 1516 弄',
        gender: '男',
        birthday: '2000-09-09',
        phone: '13189387152',
        zip: 200333
      }]
    }
  }
}
</script>

<style scoped>

</style>