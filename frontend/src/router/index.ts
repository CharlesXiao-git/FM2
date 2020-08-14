import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import Login from '@/views/Login/Login.vue'
import Home from '@/views/Home/Home.vue'
import AddressBook from '@/views/AddressBook/AddressBook.vue'
import Consignment from '@/views/Consignment/Consignment.vue'

Vue.use(VueRouter)

const routes: Array<RouteConfig> = [
  {
    path: '/',
    redirect: {
      name: 'Home'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    props: true
  },
  {
    path: '/consignment',
    name: 'Consignment',
    meta: { layout: 'home' },
    component: Consignment,
    props: true
  },
  {
    path: '/address-book',
    name: 'AddressBook',
    meta: { layout: 'home' },
    component: AddressBook,
    props: true
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
