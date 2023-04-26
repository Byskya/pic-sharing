<template>
  <div>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-upload
          action="http://localhost:9090/file/upload"
          list-type="picture-card"
          :file-list="list"
          :on-success="handleAvatarSuccess"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove">
        <i class="el-icon-plus"></i>
      </el-upload>
      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
      <el-form-item label="作品标题" prop="name">
        <el-input v-model="ruleForm.name"></el-input>
      </el-form-item>
      <el-form-item label="公开范围" prop="scope">
        <el-select v-model="ruleForm.scope" placeholder="请选择活动区域">
          <el-option label="好友" value="好友"></el-option>
          <el-option label="私人" value=私人></el-option>
          <el-option label="所有人" value="所有人"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="评论区" prop="commentSwitch">
        <el-switch v-model="ruleForm.commentSwitch"></el-switch>
      </el-form-item>
      <el-form-item label="作品标签" prop="tags">
        <el-select v-model="ruleForm.selectedTags" multiple filterable>
          <el-option v-for="tag in tags" :key="tag.id" :label="tag.tagName" :value="tag.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="作品描述" prop="desc">
        <el-input type="textarea" v-model="ruleForm.desc"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "EditWorkView",
  data() {
    return {
      // el-upload的图片暂存列表
      list:[],
      // 作品标签
      tags: [
      ],
      // 上传图片
      dialogImageUrl: '',
      dialogVisible: false,
      //表单数据体
      ruleForm: {
        workId:Number,
        illustration: null,
        name: '',
        scope: [],
        commentSwitch: false,
        selectedTags: [],
        desc: ''
      },

      //表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入作品标题', trigger: 'blur' },
          { min: 1, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择公开范围', trigger: 'change' }
        ],
        desc: [
          { required: true, message: '请填写描述', trigger: 'blur' }
        ]
      }
    };
  },
  computed:{
    workInfo:{
      get(){
        const userId = this.$store.state.user.id
        const title = this.ruleForm.name
        const scope = this.ruleForm.scope
        const id = this.ruleForm.workId
        const commentSwitch = this.ruleForm.commentSwitch
        const description = this.ruleForm.desc
        return {
          id,
          userId,
          title,
          description
        }
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    // 加载需要编辑修改的作品的信息
    load(){
      const workId = this.$route.query.workId
      const request1 = this.axios.get('http://localhost:9090/work/tags')
      const request2 = this.axios.get('http://localhost:9090/work/return/'+workId)
      this.axios.all([request1,request2]).then(this.axios.spread((response1,response2)=>{
        if (response1.status===200 && response2.status === 200){
          console.log(response1.data.data)
          console.log(response2.data.data)
          this.ruleForm.workId = response2.data.data.id
          this.tags = response1.data.data
          this.ruleForm.name = response2.data.data.title
          for (let i = 0;i<response2.data.data.tags.length;i++){
            this.ruleForm.selectedTags.push(response2.data.data.tags[i].id)
          }
          this.ruleForm.desc = response2.data.data.description
          const base64Str = 'data:image/*;base64,'+response2.data.data.imageResource
          const blob = this.$base64ToBlob(base64Str)
          this.list.push({name:'xxx',url:URL.createObjectURL(blob)})
        }
      })).catch(error=>{
        console.log(error)
      })
    },
    // 图片上传方法处理
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
      this.ruleForm.illustration = file.raw
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true
    },
    // 表单提交方法处理
    submitForm(formName) {
      const jsonWorkInfo = JSON.stringify(this.workInfo)
      console.log(this.ruleForm.selectedTags+"===================")
      const jsonTags = JSON.stringify(this.ruleForm.selectedTags)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const formData = new FormData();
          formData.append('illustration',this.ruleForm.illustration)
          formData.append('workInfo',jsonWorkInfo)
          formData.append('tags',jsonTags)
          this.axios.post('http://localhost:9090/work/edit',formData,{
            headers:{
              'Content-Type':'multipart/form-data'
            }
          }).then(response=>{
            if (response.status===200){
              this.$message.success("用户信息修改成功")
            }
            console.log(response)
          }).catch(error=>{
            this.$message.success("用户信息修改失败")
            console.log(error)
          });
          alert('submit!');
        }
        else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      console.log(this.ruleForm.selectedTags+"===================")
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>

</style>