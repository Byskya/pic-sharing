<template>
  <div>
    <div>
      <el-row :gutter="40" class="panel-group">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" @click="handleSetUserChart()">
            <div class="card-panel-description">
              <div class="card-panel-text">
                平台用户数量
              </div>
              <count-to :start-val="0" :end-val="userNumber" :duration="2600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" @click="handleSetWorkChart()">
            <div class="card-panel-description">
              <div class="card-panel-text">
                平台作品数量
              </div>
              <count-to :start-val="0" :end-val="workNumber" :duration="3000" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-description">
              <div class="card-panel-text">
                消息数量
              </div>
              <count-to :start-val="0" :end-val="messageNumber" :duration="3200" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-description">
              <div class="card-panel-text">
                平台访问数量
              </div>
              <count-to :start-val="0" :end-val="16422" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
<!--    图表区-->
    <div>
      <div v-show="chartSwitch===true" ref="userChart" style="width: 1500px; height: 700px;"></div>
      <div v-show="chartSwitch===false" ref="workChart" style="width: 1500px; height: 700px;"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import CountTo from 'vue-count-to'
export default {
  name: "DataAnalyzeView",
  components: {
    CountTo
  },
  data(){
    return{
      // 图片切换开关
      chartSwitch: true,
      // 接收后端数据的容器
      userNumber:0,
      workNumber:0,
      messageNumber:0,
      female: 0,
      male: 0,
      fm: 0,
      userList:[],
      workList:[],
      // 图表数据容器
      genderData: [],
      tagData: [],
      tagDataTem: {},
      workTimeData:[],
      userTimeData: [],

    }
  },
  created() {
    this.load()
  },
  async mounted() {
    await new Promise((resolve) => setTimeout(resolve, 70))
    this.renderUserChart()
    this.renderWorkChart()
  },
  methods:{
    // 图表切换方法
    handleSetUserChart(){
      this.chartSwitch = true
    },
    handleSetWorkChart(){
      this.chartSwitch =false;
    },
    // 作品数据渲染
    renderWorkChart(){
      const chartData = {
        title: {
          text: '作品发布时间'
        },
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.workTimeData.map(item => item.name)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'Works',
            type: 'line',
            data: this.workTimeData.map(item => item.value),
          },
          {
            name: 'Tags',
            type: 'pie',
            center: ['95%',100],
            radius: 60,
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '25',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.tagData
          }
        ]
      }
      this.workChart = echarts.init(this.$refs.workChart)
      this.workChart.setOption(chartData)
    },
    // 用户数据渲染
    renderUserChart () {
      const chartData = {
        title: {
          text: '用户注册时间'
        },
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.userTimeData.map(item => item.name),
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'Users',
            type: 'line',
            data: this.userTimeData.map(item => item.value),
            color: ['#dd6b66','#759aa0','#e69d87','#8dc1a9','#ea7e53','#eedd78','#73a373','#73b9bc','#7289ab', '#91ca8c','#f49f42'],
          },
          {
            name: 'Users2',
            type: 'bar',
            data: this.userTimeData.map(item => item.value)
          },
          {
            name: 'Gender',
            type: 'pie',
            center: ['95%',100],
            radius: 60,
            avoidLabelOverlap: false,
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '25',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: this.genderData
          }
        ]
      }
      this.userChart = echarts.init(this.$refs.userChart)
      this.userChart.setOption(chartData)
    },
    userRegisterTime(){
      this.userList.sort((a, b) => {
        return new Date(a.createdAt) - new Date(b.createdAt);
      });
      const groups = {};
      this.userList.forEach((user) => {
        const date = new Date(user.createdAt);
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const key = `${year}-${month < 10 ? '0' : ''}${month}`;
        if (!groups[key]) {
          groups[key] = 0;
        }
        groups[key]++;
      });
      Object.entries(groups).forEach(([key, value]) => {
        this.userTimeData.push({
          name: key,
          value: value
        });
      });
      console.log(groups);
    },
    workUploadTime(){
      this.workList.sort((a, b) => {
        return new Date(a.createdAt) - new Date(b.createdAt);
      });
      const groups = {};
      this.workList.forEach((work) => {
        const date = new Date(work.createdAt);
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const key = `${year}-${month < 10 ? '0' : ''}${month}`;
        if (!groups[key]) {
          groups[key] = 0;
        }
        groups[key]++;
      });
      Object.entries(groups).forEach(([key, value]) => {
        this.workTimeData.push({
          name: key,
          value: value
        });
      });
      console.log(groups);
    },
    load(){
      this.axios.get('http://localhost:9090/platform/data').then(response=>{
        if (response.status===200){
          console.log(response.data.data)
          this.userNumber = response.data.data.userNumber
          this.workNumber = response.data.data.workNumber
          this.messageNumber = response.data.data.messageNumber
          this.userList = response.data.data.userList
          this.workList = response.data.data.workList
          // 把标签统计数据转换成数组的形式
          Object.entries(response.data.data.tagMap).forEach(([key, value]) => {
            this.tagData.push({
              name: key,
              value: value
            });
          });
          this.userRegisterTime()
          this.workUploadTime()
          this.genderData.push(
              { value: response.data.data.male, name: '男' },
              { value: response.data.data.female, name: '女' },
              { value: response.data.data.fm, name: '不明' }
          )
        }
      }).catch(error=>{
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
.panel-group {
  margin-bottom: 20px;
}

.card-panel-col {
  padding-left: 10px;
  padding-right: 10px;
  margin-bottom: 20px;
}

.card-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100px;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease-in-out;
}

.card-panel:hover {
  box-shadow: 0px 4px 20px rgba(0, 0, 0, 0.2);
}


.card-panel-description {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  margin-left: 20px;
}

.card-panel-text {
  font-size: 16px;
  font-weight: bolder;
  color: #333;
  margin-bottom: 10px;
}

.card-panel-num {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
}

</style>