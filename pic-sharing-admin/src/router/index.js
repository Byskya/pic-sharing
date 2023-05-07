import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import UserManageView from "@/views/user/UserManageView";
import PictureMangeView from "@/views/picture/PictureMangeView";
import MessageView from "@/views/message/MessageSendView";
import UserUpdate from "@/views/user/UserUpdate";
import Login from "@/views/Login";
import Layout from "@/views/Layout";
import ReviewWorkView from "@/views/work/ReviewWorkView";
import RemoveWorkView from "@/views/work/RemoveWorkView";
import MessageEditView from "@/views/message/MessageEditView";
import AddWorkTagView from "@/views/work/AddWorkTagView";
import DataAnalyzeView from "@/views/data/DataAnalyzeView";

Vue.use(VueRouter)

const routes = [
    // 登录
  {
    path: '/login',
    name: 'login',
    component: Login
  },
    //主页
  {
    path: '/',
    name:'layout',
    component: Layout,
    redirect: '/home',
    meta: {
      requiresAuth: true,  // 需要进行登录验证
    },
    children:[
      {
        path: 'home',
        name: 'home',
        component: HomeView
      },
      {
        path: 'dataAnalyze',
        name: 'dataAnalyze',
        component: DataAnalyzeView
      },
      {
        path: 'userManage',
        name: 'userManage',
        component: UserManageView
      },
      {
        path: 'pictureManage',
        name: 'pictureManage',
        component: PictureMangeView
      },
      {
        path: 'message',
        name: 'message',
        component: MessageView
      },
      {
        path: 'messageEdit',
        name: 'messageEdit',
        component: MessageEditView
      },
      {
        path: 'userUpdate',
        name: 'userUpdate',
        component: UserUpdate
      },
      {
        path: 'reviewWork',
        name:'reviewWork',
        component: ReviewWorkView
      },
      {
        path: 'removeWork',
        name:'removeWork',
        component: RemoveWorkView
      },
      {
        path: 'addWorkTag',
        name: 'addWorkTag',
        component: AddWorkTagView
      }
    ]
  }
]
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
//全局前置守卫，未登录前禁止访问网站的一切内容
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  if (to.path === '/login') {
    // 如果是访问登录页面，则不需要进行登录验证，直接放行
    next()
  } else if (to.matched.some(record => record.meta.requiresAuth)) { //判断跳转的目标路由是否需要进行登录才能访问
    // 如果需要进行登录验证，则判断用户是否已登录
    if (!isLoggedIn) {
      // 如果用户未登录，则重定向到登录页面
      next('/login')
    } else {
      // 如果用户已登录，则放行
      next()
    }
  } else {
    // 如果不需要进行登录验证，则直接放行
    next()
  }
});
export default router
