<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class App extends Vue {
  mounted () {
    if (!this.getToken()) {
      this.$router.replace({ name: 'Login' })
    }
  }

  getToken () {
    return localStorage.getItem('user-token') || 0
  }
}
</script>
<style lang="scss">
#app {
    font-family: "Montserrat", sans-serif !important;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    background-color: $primary;
    height: 100%;
}
</style>
