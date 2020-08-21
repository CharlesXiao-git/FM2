<template>
  <div>
    <div class="row">
      <b-form-group class="mt-2 mb-3">
        <b-form-radio-group v-model="addressClass" @input="selectedAddressClass">
          <b-form-radio value="BUSINESS">Business Address</b-form-radio>
          <b-form-radio value="RESIDENTIAL">Residential Address</b-form-radio>
        </b-form-radio-group>
      </b-form-group>
    </div>

    <div class="row">
      <b-form-input v-model="specialInstructions" placeholder="Special instructions" @input="selectedSpecialInstructions"></b-form-input>
    </div>

    <div class="row">
      <b-form-group class="mt-3">
        <b-form-checkbox v-model="isAllowedToLeave" @input="selectedAuthToLeave" inline>Authority to leave</b-form-checkbox>
        <b-form-checkbox v-model="isTailgateRequired" @input="selectedTailgateRequired" inline>Tailgate required</b-form-checkbox>
      </b-form-group>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class DeliveryDetails extends Vue {
  addressClass: string = null
  specialInstructions: string = null
  isAllowedToLeave: boolean = null
  isTailgateRequired: boolean = null

  selectedAddressClass () {
    this.$emit('selected-address-class', this.addressClass)
  }

  selectedSpecialInstructions () {
    this.$emit('selected-special-instructions', this.specialInstructions)
  }

  selectedAuthToLeave () {
    this.$emit('selected-auth-to-leave', this.isAllowedToLeave)
  }

  selectedTailgateRequired () {
    this.$emit('selected-tailgate-required', this.isTailgateRequired)
  }
}
</script>
