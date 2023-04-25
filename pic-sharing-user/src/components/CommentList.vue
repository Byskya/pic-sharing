<template>
  <div>
    <el-form>
      <el-form-item label="评论内容">
        <el-input v-model="commentContent"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitComment">提交评论</el-button>
      </el-form-item>
    </el-form>
    <comment v-for="(comment, index) in comments" :key="index" :comment="comment"></comment>
  </div>
</template>

<script>
import Comment from './Comment.vue';

export default {
  name:'CommentList',
  components: {
    Comment,
  },
  data() {
    return {
      comments: [],
      commentContent: '',
    };
  },
  props:{
    illustration:{
      type:Object,
      required:true
    }
  },
  created() {
    this.load()
  },
  methods: {
    // 页面数据加载方法,加载作品的评论数据
    load(){
      this.axios.get('http://localhost:9090/work/comments/'+this.illustration.id).then(response=>{
        if (response.data.state===200){
          this.comments = response.data.data.list
        }
      }).catch(error=>{
        console.log(error)
      });
    },
    submitComment() {
      if (!this.commentContent){
        this.$message.error('请输入评论内容');
        return;
      }
      this.axios.post('http://localhost:9090/work/comment/send/'+this.illustration.id+'/'+this.commentContent).then(response=>{
        if (response.data.state === 200){
          this.$message.success("评论发表成功")
          this.commentContent = '';
        }
      }).catch(error=>{
        console.log(error)
      });
    },
  },
};
</script>

<style scoped>
.comment-list {
  margin-top: 20px;
}
</style>
