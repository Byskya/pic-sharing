import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isAuthenticated: false,
    user: null
  },
  getters: {
    isAuthenticated: state => state.isAuthenticated,
    user: state => state.user
  },
  mutations: {
    setUser(state, user) {
      state.user = user
      state.isAuthenticated = true
    },
    clearUser(state) {
      state.user = null
      state.isAuthenticated = false
    }
  },
  actions: {
    login({ commit }, user) {
      // 调用 API 来进行用户验证
      // 如果用户验证成功，将用户信息提交给 setUser mutation
      commit('setUser', user)
    },
    logout({ commit }) {
      // 调用 API 来进行用户注销
      // 如果用户注销成功，将用户信息清除
      commit('clearUser')
    }
  },
  modules: {
  }
})
