<template>
  <div>
    <div class="tag-cloud">
      <div class="tag" v-for="(tag, index) in tags" :key="index" :style="tagStyle(index)" @click="handleTagClick(tag)">#{{ tag.tagName }}</div>
    </div>
    <div>
      <IllustrationTypeContent v-if="judge" :tag="tag"></IllustrationTypeContent>
    </div>
  </div>
</template>

<script>
import IllustrationTypeContent from "@/components/IllustrationTypeContent";
export default {
  name: "WorkTypeView",
  components: {IllustrationTypeContent},
  data() {
    return {
      tags: [],
      tag:{},
      judge:false
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.$http.get('/work/tags').then(response=>{
        if (response.status===200){
          this.tags = response.data.data
        }
      }).catch(error=>{
        console.log(error)
      })
    },
    handleTagClick(tag) {
      // do something when a tag is clicked
      this.tag = tag
      this.judge = true
    },
    tagStyle(index) {
      const randomColor = Math.floor(Math.random()*16777215).toString(16);
      return `
        background-color: #${randomColor};
      `;
    }
  }
}
</script>

<style scoped>
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  height: 80px;
}

.tag {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 5px;
  padding: 10px;
  border-radius: 20px;
  background-color: #ccc;
  color: #fff;
  font-size: 1.2rem;
  width: 100px;
  height: 40px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
}

.tag:hover {
  transform: scale(1.2);
}
</style>