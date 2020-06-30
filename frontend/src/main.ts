import Vue from 'vue'
import App from './App.vue'
import axios, { AxiosStatic } from 'axios'

import router from './router'

Vue.config.productionTip = false
Vue.prototype.$axios = axios

declare module 'vue/types/vue' {
  interface Vue {
    $axios: AxiosStatic;
  }
}

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
axios.defaults.baseURL = 'http://localhost:8098'
