<template>
  <div>
    <label v-if="label" for="datepicker" class="datepicker-label">{{label}}</label>
    <b-form-datepicker
        id="datepicker"
        v-model="value"
        :min="min"
        :date-format-options="dateFormatOptions"
        :placeholder="datePickerPlaceholder"
        :start-weekday="1"
        :hide-header="hideHeader"
        selected-variant="success"
        locale="en-GB"
        value-as-date
        @input="change"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class DatePicker extends Vue {
  @Prop() private label: string
  @Prop() private placeholder: string

  value: Date = null
  min: Date = new Date()
  dateFormatOptions: object = { year: 'numeric', month: 'numeric', day: 'numeric' }
  datePickerPlaceholder = this.placeholder || 'Please select a date'
  hideHeader = true

  change () {
    this.$emit('change', this.value)
  }
}
</script>
