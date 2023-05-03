<template>
  <div>
    <div style="width: 100%">
      <div style="width: 100%">
        <el-table
            :data="tableData.filter(data => !search || data.description.toLowerCase().includes(search.toLowerCase()))" style="width: 100%">
          <el-table-column
              label="发布时间">
            <template slot-scope="props">
                <span>{{ new Date(props.row.createdAt).toLocaleString() }}</span>
            </template>
          </el-table-column>
          <el-table-column
              label="ID"
              prop="id">
          </el-table-column>
          <el-table-column
              label="内容"
              prop="content">
            <template slot-scope="scope">
              <div style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                {{ scope.row.content }}
              </div>
            </template>
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
                  @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
              <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
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
export default {
  name: "MessageEditView",
  data(){
    return{
      // 分页参数
      params:{
        pageNum:1,
        pageSize:5,
        total:0
      },
      tableData: [],
      search: '',
      content:'',
    }
  },
  created() {
    this.load()
  },
  methods:{
    handleEdit(index, row) {
      console.log(index, row);
      this.$prompt('请输入修改内容', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        this.$message({
          type: 'success',
          message: '修改内容: ' + value,
        })
        this.content = value
        this.axios.put('http://localhost:9090/notification/update/'+row.id+'/'+this.content).then(response=>{
          if (response.data.state===200){
            this.$message.success("修改内容成功")
          }
        }).catch(error=>{
          console.log(error)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },
    handleDelete(index, row) {
      this.axios.delete('http://localhost:9090/notification/delete/'+row.id).then(response=>{
        if (response.status===200){
          this.$message.success("删除成功")
        }
      }).catch(error=>{
        console.log(error)
        this.$message.error(error)
      })
    },
    // 分页导航栏
    handleCurrentChange(pageNum){
      this.params.pageNum = pageNum
      this.load()
    },
    load(){
      this.axios.get('http://localhost:9090/notifications/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.status===200){
          this.tableData = response.data.data.list
          this.params.total = response.data.data.total
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