<template>
  <div>
    <div>
      <el-table
          :data="tableData"
          style="width: 100%">
        <el-table-column
            label="日期"
            width="180">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            <span style="margin-left: 10px">{{ new Date(scope.row.updatedAt).toLocaleDateString() }}</span>
          </template>
        </el-table-column>
        <el-table-column
            label="名称"
            width="180">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top">
              <p>名称: {{ scope.row.tagName }}</p>
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium">{{ scope.row.tagName }}</el-tag>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作">
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
    <div>
      <el-button type="success" @click="addTag">添加</el-button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "AddWorkTagView",
  data() {
    return {
      tableData: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    addTag(){
      this.$prompt('请输入标签名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValidator: value => value!==null,
      }).then(({ value }) => {
        this.axios.post('http://localhost:9090/add/workTag/'+value).then(response=>{
          if (response.status===200){
            this.$message({
              type: 'success',
              message: '添加的标签: ' + value,
            });
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
    load(){
      this.axios.get('http://localhost:9090/work/tags').then(response=>{
        if (response.status===200){
          this.tableData = response.data.data
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    handleEdit(index, row) {
      console.log(index, row);
      this.$prompt('请输入标签名', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValidator: value => value!==null,
      }).then(({ value }) => {
        this.axios.put('http://localhost:9090/edit/tag/'+row.id+'/'+value).then(response=>{
          if (response.status===200){
            this.$message.success("修改标签成功")
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
      console.log(index, row);
      this.axios.delete('http://localhost:9090/delete/tag/'+row.id).then(response=>{
        if (response.status===200){
          this.$message.success("删除成功")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>