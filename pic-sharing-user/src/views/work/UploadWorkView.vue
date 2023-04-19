<template>
  <div>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-upload
          action="http://localhost:9090/file/upload"
          list-type="picture-card"
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
  name: "UploadWorkView",
  data() {
    return {
      // 作品标签
      tags: [
      ],
      // 上传图片
      dialogImageUrl: '',
      dialogVisible: false,
      //表单数据体
      ruleForm: {
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
       const commentSwitch = this.ruleForm.commentSwitch
       const description = this.ruleForm.desc
      return {
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
    load(){
      this.axios.get("http://localhost:9090/work/tags").then(response=>{
        if (response.data.state===200){
          console.log(response.data.data)
          this.tags = response.data.data
        }
        else {
          console.log(response.data.message)
        }
      }).catch(error=>{
        console.log(error)
      });
    },
    // 图片上传方法处理
    // 头像上传相关方法
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
      this.ruleForm.illustration = file.raw
      console.log(this.ruleForm.avatar)
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
          this.axios.post('http://localhost:9090/work/upload',formData,{
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