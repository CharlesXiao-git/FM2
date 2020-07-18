<template>
  <div class="alert-container">
    <b-alert
      :variant="variant"
      dismissible
      :show="dismissCountDown"
      @dismissed="dismissCountDown=0"
      @dismiss-count-down="countDownChanged"
      fade
    >
      {{text}}
    </b-alert>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

type AlertVariant = 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark'

@Component
export default class Alert extends Vue {
  @Prop() private variant: AlertVariant
  @Prop() private text: string

  dismissCountDown = 10

  countDownChanged (dismissCountDown: number) {
    this.dismissCountDown = dismissCountDown
  }
}
</script>

<style>
  .alert-container {
    height: 50px;
    margin-bottom: 15px;
  }
</style>
