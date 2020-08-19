<template>
  <div class="row">
    <div v-if="label" class="col-lg-6 p-0 mt-2">
      <label class="datepicker-label">{{label}}</label>
    </div>
    <div class="col-lg-6 p-0">
      <b-form-datepicker
          name="name"
          v-model="value"
          :min="minValue"
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
  @Prop({ required: true }) private name: string
  @Prop() private label: string
  @Prop() private placeholder: string
  @Prop() private minValue: Date
  @Prop() private defaultValue: Date

  value: Date = this.defaultValue || null
  dateFormatOptions: object = { year: 'numeric', month: 'numeric', day: 'numeric' }
  datePickerPlaceholder = this.placeholder || 'Select a date'

  change () {
    this.$emit('change', this.value)
  }
}
</script>
