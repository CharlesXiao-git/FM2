<template>
    <div>
        <div class="row">
            <div class="col-md-12 col-lg-6 col-xl-4 p-0 mr-2">
                <b-form-group
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
                    label="To"
                    label-align-sm="left"
                    label-for="to-timepicker"
                    label-class="font-weight-bold pt-2"
                >
                    <TimePicker name="to-timepicker" @selected-time="handleSelectedTime" :disabled="!baseDate" />
                </b-form-group>
            </div>
        </div>

        <div class="row">
            <div v-if="timeSlotErrorMsg" class="error-msg">{{timeSlotErrorMsg}}</div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import TimePicker from '@/components/TimeSlotPicker/TimePicker.vue'
import { TimeSlot } from '@/components/TimeSlotPicker/TimeSlot'

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

  isTimeRangeValid (from: Date, to: Date): boolean {
    if (from && to) {
      return from < to
    }
    return true
  }

  handleSelectedTime (response: TimePickerData) {
    console.log(this.baseDate)
    const timeParts = response.time.split(':') // hours, minutes, seconds
    const date = new Date(this.baseDate.getFullYear(), this.baseDate.getMonth(), this.baseDate.getDate(), parseInt(timeParts[0]), parseInt(timeParts[1]), parseInt(timeParts[2]))

    // Compare response name
    if (response.name === 'from-timepicker') {
      if (this.isTimeRangeValid(date, this.toDateTime)) {
        this.fromDateTime = date
        this.resetTimeRangeErrorMsg()
      } else {
        this.setTimeRangeErrorMsg()
      }
    } else if (response.name === 'to-timepicker') {
      if (this.isTimeRangeValid(this.fromDateTime, date)) {
        this.toDateTime = date
        this.resetTimeRangeErrorMsg()
      } else {
        this.setTimeRangeErrorMsg()
      }
    }

    if (this.fromDateTime && this.toDateTime && this.isTimeRangeValid(this.fromDateTime, this.toDateTime)) {
      this.$emit('selected-slot', new TimeSlot(this.fromDateTime, this.toDateTime))
    }
  }
}
</script>
