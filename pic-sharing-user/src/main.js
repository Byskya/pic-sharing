import Vue, {onMounted} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import VueAxios from 'vue-axios'
import base64 from "@/plugins/base64";
import randomUID from "@/plugins/randomUID";


const axiosInstance = axios.create({
  baseURL: 'http://192.168.31.46:9090', //设置请求前缀
  timeout: 10000, //设置请求超时时间,
  withCredentials: true //设置axios发送的请求携带cookie
})

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(VueAxios, axiosInstance)
Vue.use(base64)
Vue.use(randomUID)

const vm = new Vue({
  router,
  store,
  // 全局事件注册，在每次实例重新创建后重新注册
  created() {
    Vue.prototype.$bus = this
  },
  render: h => h(App)
}).$mount('#app')


