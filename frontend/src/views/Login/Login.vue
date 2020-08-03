<template>
    <b-container fluid>
        <b-row>
            <form class="login text-center my-5 mx-auto pt-5 px-0 px-sm-5" @submit.prevent="login">
                <img src="../../assets/FreightMate_Logo.svg" alt="FreightMate Logo" />
                <div class="input-group mt-2">
                    <div class="input-group-prepend">
                        <div class="input-group-text">
                            <i class="fa fa-user"></i>
                        </div>
                    </div>
                    <b-form-input required v-model="username" :state="validation" class="form-control" placeholder="Username"></b-form-input>
                </div>

                <div class="input-group my-3">
                    <div class="input-group-prepend">
                        <div class="input-group-text">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <b-form-input required type="password" v-model="password" :state="validation" class="form-control" placeholder="Password"></b-form-input>
                </div>

                <b-form-invalid-feedback :state="validation">{{ errorMessage }}</b-form-invalid-feedback>

                <button class="btn mb-5 btn-lg btn-primary btn-block" type="submit" :disabled="multipleAttempts">Sign in</button>
                <a class='btn btn-primary btn-block' id="reset_pw_button" >Reset Password</a>
            </form>
        </b-row>
    </b-container>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { extractUserFromToken, setToken } from '@/service/AuthService'

@Component
export default class Login extends Vue {
    username = ''
    password = ''
    response = ''
    validation: boolean = null
    errorMessage = 'Error logging in! Please try again or press the reset password button below.'
    multipleAttempts = false

    login (): void {
      const user = this.prepareData()
      this.$axios.post('/api/v1/auth/login', user)
        .then(response => {
          const token = response.data
          setToken(token)
          const user = extractUserFromToken(token)
          this.validation = true
          this.$router.push({ name: 'Home', params: { role: user.role } })
        }, error => {
          if (error.response.data !== null) {
            this.errorMessage = error.response.data
          }
          this.validation = false
          if (error.response.status === 429) {
            this.disableSignIn()
          }
        })
    }

    prepareData () {
      return {
        username: this.username,
        password: this.password
      }
    }

    disableSignIn () {
      this.multipleAttempts = true
      setTimeout(() => { this.multipleAttempts = false }, 60000)
    }
}
</script>

<style scoped lang="scss" src="./Login.scss"></style>
