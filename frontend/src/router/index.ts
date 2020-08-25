import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import Login from '@/views/Login/Login.vue'
import Home from '@/views/Home/Home.vue'
import AddressBook from '@/views/AddressBook/AddressBook.vue'
import DefaultConsignment from '@/views/Consignment/DefaultConsignment.vue'
import ThirdPartyConsignment from '@/views/Consignment/ThirdPartyConsignment.vue'
import ReturnConsignment from '@/views/Consignment/ReturnConsignment.vue'

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
    path: '/consignment/default',
    name: 'DefaultConsignment',
    meta: { layout: 'home' },
    component: DefaultConsignment,
    props: true
  },
  {
    path: '/consignment/third-party',
    name: 'ThirdPartyConsignment',
    meta: { layout: 'home' },
    component: ThirdPartyConsignment,
    props: true
  },
  {
    path: '/consignment/return',
    name: 'ReturnConsignment',
    meta: { layout: 'home' },
    component: ReturnConsignment,
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
