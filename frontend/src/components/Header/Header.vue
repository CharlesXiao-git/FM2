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
import { Component, Vue } from 'vue-property-decorator'
import { extractUserFromToken, getToken, removeToken } from '@/service/AuthService'

@Component
export default class Header extends Vue {
  user: string = this.getUser()

  getUser () {
    const token = getToken()

    if (token) {
      this.user = extractUserFromToken(token).username
    }
    return this.user
  }

  logout () {
    removeToken()
    this.$router.push({ name: 'Login' })
  }
}
</script>

<style scoped lang="scss" src="./Header.scss"></style>
