<template>
  <b-form-datepicker
    :name="name"
    class="datepicker"
    v-model="value"
    :min="minValue"
    :date-format-options="dateFormatOptions"
    :placeholder="placeholder"
    :start-weekday="1"
    :hide-header="true"
    :reset-button="showResetBtn"
    reset-button-variant="outline-success"
    selected-variant="success"
    locale="en-GB"
    value-as-date
    @input="change"
  />
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class DatePicker extends Vue {
  @Prop({ required: true }) private name: string
  @Prop({ default: 'Select a date' }) private placeholder: string
  @Prop({ default: false }) private showResetBtn: boolean
  @Prop() private defaultValue: Date
  @Prop() private minValue: Date

  value: Date = this.defaultValue || null
  dateFormatOptions: object = { year: 'numeric', month: 'numeric', day: 'numeric' }

  change () {
    this.$emit('selected-date', this.value)
  }
}
</script>

<style scoped lang="scss" src="./DatePicker.scss" />
