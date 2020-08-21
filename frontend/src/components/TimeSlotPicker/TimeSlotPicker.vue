<template>
    <div class="row">
        <div class="col-md-12 col-lg-6 col-xl-4 p-0 mr-2">
            <b-form-group
                class="mb-0"
                label="From"
                label-align-sm="left"
                label-for="from-timepicker"
                label-class="font-weight-bold pt-2"
            >
                <TimePicker name="from-timepicker" @selected-time="handleSelectedTime" :disabled="!baseDate" />
            </b-form-group>
        </div>
        <div class="col-md-12 col-lg-6 col-xl-4 p-0">
            <b-form-group
                class="mb-0"
                label="To"
                label-align-sm="left"
                label-for="to-timepicker"
                label-class="font-weight-bold pt-2"
            >
                <TimePicker name="to-timepicker" @selected-time="handleSelectedTime" :disabled="!baseDate" />
            </b-form-group>
        </div>

        <div v-if="timeSlotErrorMsg" class="error-msg p-0 ml-0 col-12">{{timeSlotErrorMsg}}</div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import TimePicker from '@/components/TimeSlotPicker/TimePicker.vue'
import { TimeSlot } from '@/components/TimeSlotPicker/TimeSlot'
import { validateTimeSlot } from '@/helpers/ValidationHelpers'
import { applyTimeToDate } from '@/helpers/DateHelpers'

export type TimePickerData = {
  name: string;
  time: string;
}
@Component({
  components: { TimePicker }
})
export default class TimeSlotPicker extends Vue {
  @Prop({ required: true }) baseDate: Date

  fromDateTime: Date = null
  toDateTime: Date = null
  timeSlotErrorMsg: string = null

  setTimeRangeErrorMsg (): void {
    this.timeSlotErrorMsg = 'Invalid time range'
  }

  resetTimeRangeErrorMsg (): void {
    this.timeSlotErrorMsg = null
  }

  handleSelectedTime (response: TimePickerData) {
    const date = applyTimeToDate(this.baseDate, response.time)

    // Compare response name
    if (response.name === 'from-timepicker') {
      if (validateTimeSlot(date, this.toDateTime)) {
        this.fromDateTime = date
        this.resetTimeRangeErrorMsg()
      } else {
        return this.setTimeRangeErrorMsg()
      }
    } else if (response.name === 'to-timepicker') {
      if (validateTimeSlot(this.fromDateTime, date)) {
        this.toDateTime = date
        this.resetTimeRangeErrorMsg()
      } else {
        return this.setTimeRangeErrorMsg()
      }
    }

    if (this.fromDateTime && this.toDateTime) {
      this.$emit('selected-slot', new TimeSlot(this.fromDateTime, this.toDateTime))
    }
  }
}
</script>
