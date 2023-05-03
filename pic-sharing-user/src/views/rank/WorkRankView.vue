<template>
  <div>
    <el-card v-for="(work, index) in works" :key="work.id">
      <div slot="header">
        <span class="rank">{{ index + 1 }}</span>
      </div>
      <div>
        <img :src="work.image" alt="work image" />
      </div>
      <div>
        <h3>{{ work.title }}</h3>
        <p>{{ work.author.name }}</p>
        <img :src="work.author.avatar" alt="author avatar" />
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "WorkRankView",
  data() {
    return {
      works: [
        {
          id: 1,
          title: "Summer Sunset",
          image: "https://picsum.photos/400/300?random=1",
          author: {
            name: "John Smith",
            avatar: "https://picsum.photos/50?random=1"
          },
          rank: 1
        },
        {
          id: 2,
          title: "Forest Walk",
          image: "https://picsum.photos/400/300?random=2",
          author: {
            name: "Jane Doe",
            avatar: "https://picsum.photos/50?random=2"
          },
          rank: 2
        },
      ],
    };
  },
  mounted() {

  }
}
</script>

<style scoped>
.rank {
  font-size: 24px;
  font-weight: bold;
  margin-right: 10px;
}
</style>