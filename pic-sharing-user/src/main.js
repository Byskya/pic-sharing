import Vue, {onMounted} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'
import VueAxios from 'vue-axios'
//设置axios发送的请求携带cookie
axios.defaults.withCredentials = true;

Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.use(VueAxios, axios)
const vm = new Vue({
  router,
  store,
  // 全局事件注册，在每次实例重新创建后重新注册
  created() {
    Vue.prototype.$bus = this
  },
  render: h => h(App)
}).$mount('#app')


