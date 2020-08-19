<template>
  <div class="row">
    <div v-if="label" class="col-lg-6 p-0">
      <label for="datepicker" class="datepicker-label">{{label}}</label>
    </div>
    <div class="col-lg-6 p-0">
      <b-form-datepicker
          id="datepicker"
          v-model="value"
          :min="min"
          :date-format-options="dateFormatOptions"
          :placeholder="datePickerPlaceholder"
          :start-weekday="1"
          :hide-header="true"
          selected-variant="success"
          locale="en-GB"
          value-as-date
          @input="change"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class DatePicker extends Vue {
  @Prop() private label: string
  @Prop() private placeholder: string
  @Prop() private minValue: Date
  @Prop() private defaultValue: Date

  value: Date = this.defaultValue || new Date()
  min: Date = this.minValue || new Date()
  dateFormatOptions: object = { year: 'numeric', month: 'numeric', day: 'numeric' }
  datePickerPlaceholder = this.placeholder || 'Select a date'

  change () {
    this.$emit('change', this.value)
  }
}
</script>
