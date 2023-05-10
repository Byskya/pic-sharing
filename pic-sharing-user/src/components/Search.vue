<template>
  <div>
    <el-autocomplete
        v-model="state"
        :fetch-suggestions="querySearchAsync"
        placeholder="请输入内容"
        @select="handleSelect"
    ></el-autocomplete>
    <el-button type="primary" @click="search"><i class="el-icon-search"></i>搜索</el-button>
  </div>
</template>

<script>
export default {
  name: "Search",
  data() {
    return {
      restaurants: [],
      state: '',
      timeout:  null
    };
  },
  methods: {
    // 插画搜索结果
    search(){
      const itemJson = JSON.stringify(this.state)
      this.$router.push({
        name:'searchResult',
        query:{
          itemJson
        }
      })
    },
    loadAll() {
      return [
        { "value": "自然" },
        { "value": "风景" },
        { "value": "SAO" }
      ];
    },
    querySearchAsync(queryString, cb) {
      const restaurants = this.restaurants;
      const results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;
      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        cb(results);
      }, 3000 * Math.random());
    },
    createStateFilter(queryString) {
      return (state) => {
        return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    handleSelect(item) {
      console.log(item);
    }
  },
  mounted() {
    this.restaurants = this.loadAll();
  }
}
</script>

<style scoped>

</style>