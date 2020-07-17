<template>
    <header class="header">
        <b-navbar variant="dark" type="dark" fixed="top">
            <b-navbar-brand href="#">
                <img alt="FreightMate logo" src="../../assets/FreightMate_Home_Logo.svg">
            </b-navbar-brand>

            <b-navbar-nav class="ml-auto">
                <b-nav-item-dropdown v-if="user" right>
                    <template slot="button-content">
                            <i class="fa mr-1 fa-user"></i>
                            {{ user }}
                    </template>
                    <b-dropdown-item @click.prevent="logout()">
                        <i class="fas fa-sign-out-alt"></i>
                        Logout
                    </b-dropdown-item>
                </b-nav-item-dropdown>
            </b-navbar-nav>
        </b-navbar>
    </header>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class Header extends Vue {
  user: string = this.getUser()

  @Prop() private username: string

  getUser () {
    this.user = this.username
    const token = this.getToken()

    // If we don't have the username, we can get the username from the token
    if (!this.user && token) {
      const parsedToken = JSON.parse(atob(token.split('.')[1]))
      if (parsedToken !== null) {
        if (Object.prototype.hasOwnProperty.call(parsedToken, 'sub')) {
          this.user = parsedToken.sub
        }
      }
    }
    return this.user
  }

  getToken () {
    return localStorage.getItem('user-token') || 0
  }

  logout () {
    localStorage.removeItem('user-token')
    this.$router.push({ name: 'Login' })
  }
}
</script>

<style scoped lang="scss" src="./Header.scss"></style>
