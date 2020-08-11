<template>
  <div id="app">
    <component :is="layout">
      <router-view/>
    </component>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { isUserSessionValid } from '@/helpers/auth/UserHelpers'

@Component
export default class App extends Vue {
  mounted () {
    if (!isUserSessionValid()) {
      this.$router.replace({ name: 'Login' })
    }
  }

  get layout () {
    return (this.$route.meta.layout || 'default') + '-layout'
  }
}
</script>
<style lang="scss">
#app {
    font-family: "Montserrat", sans-serif !important;
    -moz-osx-font-smoothing: grayscale;
    background-color: $primary;
    height: 100%;
}
</style>
