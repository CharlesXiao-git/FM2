import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import Login from '../views/Login/Login.vue'

Vue.use(VueRouter)

const routes: Array<RouteConfig> = [
  {
    path: '/',
    redirect: {
      name: 'Login'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
