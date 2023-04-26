<template>
  <div>
    <div style="width: 100%">
      <div style="width: 100%">
        <el-table
            :data="tableData.filter(data => !search || data.title.toLowerCase().includes(search.toLowerCase()))" style="width: 100%">
          <el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item label="作品名称：">
                  <span>{{ props.row.title }}</span>
                </el-form-item>

                <el-form-item label="作品ID：">
                  <span>{{ props.row.id }}</span>
                </el-form-item>
                <el-form-item label="作者ID：">
                  <span>{{ props.row.userId }}</span>
                </el-form-item>
                <el-form-item label="作品标签：">
                  <span>{{ props.row.tags }}</span>
                </el-form-item>
                <el-form-item label="作品描述描述：">
                  <span>{{ props.row.description }}</span>
                </el-form-item>
                <el-form-item label="作品内容：">
                <span>
                  <div class="demo-image__preview">
                    <el-image
                        style="width: 100px; height: 100px"
                        :src="'data:image/*;base64,'+props.row.imageResource"
                    >
                    </el-image>
                  </div>
                </span>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
          <el-table-column
              label="作品 ID"
              prop="id">
          </el-table-column>
          <el-table-column
              label="作品名称"
              prop="title">
          </el-table-column>
          <el-table-column
              label="描述"
              prop="description">
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
              <el-switch
                  style="display: block"
                  v-model="scope.row.approved"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-text="上架"
                  inactive-text="下架"
                  :change="handleDelete(scope.$index, scope.row)">
              </el-switch>
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
  name: "RemoveWorkView",
  data() {
    return {
      // 分页参数
      params:{
        username:'',
        pageNum:1,
        pageSize:5,
        total:0
      },
      tableData: [],
      search: '',
      feedback:'',
    }
  },
  // ================钩子函数===================
  created(){
    this.load()
  },
  methods: {
    // 分页导航栏
    handleCurrentChange(pageNum){
      this.params.pageNum = pageNum
      this.load()
    },
    load(){
      this.axios.get('http://localhost:9090/work/all/includeUnapproved/'+this.params.pageNum+'/'+this.params.pageSize).then(response=>{
        if (response.data.state===200){
          this.tableData = response.data.data.list
          this.params.total = response.data.data.total
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    handleDelete(index, row) {

    }
  }
}
</script>

<style scoped>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>