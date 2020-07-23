<template>
  <div class="alert-container">
    <b-alert v-if="autoDismissible"
      :variant="variant"
      dismissible
      :show="dismissCountDown"
      @dismissed="dismissCountDown=0"
      @dismiss-count-down="countDownChanged"
      fade
    >
      {{text}}
    </b-alert>
    <b-alert v-else
        :variant="variant"
        dismissible
        show
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
  @Prop() private text!: string
  @Prop() private variant: AlertVariant
  @Prop() private autoDismissInterval: number

  autoDismissible = this.autoDismissInterval !== undefined && this.autoDismissInterval > 0
  dismissCountDown = this.autoDismissInterval

  countDownChanged (dismissCountDown: number) {
    this.dismissCountDown = dismissCountDown
  }
}
</script>

<style scoped lang="scss" src="./Alert.scss"></style>
