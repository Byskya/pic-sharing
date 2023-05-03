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
import ReviewScheduleView from "@/views/work/ReviewScheduleView";
import MyCollectionView from "@/views/user/MyCollectionView";
import SearchResultView from "@/views/search/SearchResultView";
import UserInfoView from "@/views/user/UserInfoView";
import EditWorkView from "@/views/work/EditWorkView";
import UserFollowView from "@/views/follow/UserFollowView";
import WorkTypeView from "@/views/type/WorkTypeView";
import WorkRankView from "@/views/rank/WorkRankView";
import MessageView from "@/views/message/MessageReceiveView";
import NotificationView from "@/views/message/NotificationView";
import UserMessageView from "@/views/message/UserMessageView";
import UserChatView from "@/views/message/UserChatView";

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
        path: 'userInfo',
        name: 'userInfo',
        component: UserInfoView,
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
        path: 'editWork',
        name: 'editWork',
        component: EditWorkView
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
      },
      {
        path: 'searchResult',
        name: 'searchResult',
        component: SearchResultView
      },
      {
        path: 'userFollow',
        name: 'userFollow',
        component: UserFollowView
      },
      {
        path: 'userChat',
        name: 'userChat',
        component: UserChatView
      },
      {
        path: 'messageView',
        name: 'messageView',
        redirect: 'messageView/notification',
        component: MessageView,
        children:[
          {
            path:'notification',
            name:'notification',
            component:NotificationView
          },
          {
            path: 'userMessage',
            name:'userMessage',
            component: UserMessageView
          }

        ]
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
