import Vue from 'vue'
import App from './App.vue'
import axios, { AxiosInstance } from 'axios'
import BootstrapVue from 'bootstrap-vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import { CoolSelectPlugin } from 'vue-cool-select'
import 'vue-cool-select/dist/themes/bootstrap.css'
import VueLogger from 'vuejs-logger'
import VueLoggerPlugin from 'vuejs-logger/dist/vue-logger'

import Home from '@/views/Home/Home.vue'
import Default from '@/views/Default/Default.vue'

Vue.config.productionTip = false
const isProduction = process.env.NODE_ENV === 'production'
const options = {
  isEnabled: true,
  logLevel: isProduction ? 'error' : 'debug',
  stringifyArguments: false,
  showLogLevel: true,
  showMethodName: true,
  separator: '|',
  showConsoleColors: true
}
Vue.use(VueLoggerPlugin, options)
Vue.prototype.$axios = axios.create({
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8080',
  timeout: process.env.VUE_APP_DEFAULT_AXIOS_TIMEOUT || 20000
})
Vue.use(BootstrapVue)
Vue.use(CoolSelectPlugin)
Vue.component('default-layout', Default)
Vue.component('home-layout', Home)

declare module 'vue/types/vue' {
  interface Vue {
    $axios: AxiosInstance;
  }
}

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
