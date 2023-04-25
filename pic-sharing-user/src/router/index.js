import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import Layout from "@/views/Layout"
import Index from "@/views/Index";
import LoginView from "@/views/login/LoginView";
import RegisterView from "@/views/login/RegisterView";
import MyWorksView from "@/views/work/MyWorksView";
import MyInfoView from "@/views/user/MyInfoView";
import UploadWorkView from "@/views/work/UploadWorkView";
import WorkDetailView from "@/views/work/WorkDetailView";
import WorkTypeView from "../../../pic-sharing-admin/src/views/type/WorkTypeView";
import WorkRankView from "../../../pic-sharing-admin/src/views/rank/WorkRankView";
import ReviewScheduleView from "@/views/work/ReviewScheduleView";
import MyCollectionView from "@/views/user/MyCollectionView";

Vue.use(VueRouter)

const routes = [
  {
    path: '/index',
    name: 'index',
    component: Index,
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView
  },
  {
    path: '/',
    name: 'layout',
    component: Layout,
    redirect: '/home',
    children:[
      {
        path: 'home',
        name: 'home',
        component: HomeView
      },
      {
        path: 'myWorks',
        name: 'myWorks',
        component: MyWorksView
      },
      {
        path: 'myInfo',
        name: 'myInfo',
        component: MyInfoView
      },
      {
        path: 'myCollection',
        name: 'myCollection',
        component: MyCollectionView
      },
      {
        path:'uploadWork',
        name:'uploadWork',
        component: UploadWorkView
      },
      {
        path: 'workDetail',
        name: 'workDetail',
        component: WorkDetailView
      },
      {
        path: 'workType',
        name: 'workType',
        component: WorkTypeView
      },
      {
        path: 'workRank',
        name: 'workRank',
        component: WorkRankView
      },
      {
        path: 'reviewSchedule',
        name: 'reviewSchedule',
        component: ReviewScheduleView
      }
    ]
  },
  // {
  //   path: '/about',
  //   name: 'about',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  // }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
