import Vue from 'vue'
import App from './App.vue'
import axios, { AxiosStatic } from 'axios'
import BootstrapVue from 'bootstrap-vue'

import router from './router'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.config.productionTip = false
Vue.prototype.$axios = axios
Vue.use(BootstrapVue)

declare module 'vue/types/vue' {
  interface Vue {
    $axios: AxiosStatic;
  }
}

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
axios.defaults.baseURL = 'http://localhost:8080'
