<template>
    <div>
        <div class="row">
            <div class="col-md-12 col-lg-6 pl-0">
                <b-form-group
                    class="mb-0"
                    label="From"
                    label-align-sm="left"
                    label-for="from-timepicker"
                    label-class="font-weight-bold pt-2"
                >
                    <b-form-timepicker
                        v-model="from"
                        name="from-timepicker"
                        :hide-header="true"
                        placeholder="Select time"
                        :disabled="!baseDate"
                        no-close-button
                        minutes-step="15"
                        class="timepicker"
                        locale="en"
                        @input="handleFromTime" />
                </b-form-group>
            </div>
            <div class="col-md-12 col-lg-6 pl-0">
                <b-form-group
                    class="mb-0"
                    label="To"
                    label-align-sm="left"
                    label-for="to-timepicker"
                    label-class="font-weight-bold pt-2"
                >
                    <b-form-timepicker
                        v-model="to"
                        name="to-timepicker"
                        :hide-header="true"
                        placeholder="Select time"
                        :disabled="!baseDate"
                        no-close-button
                        minutes-step="15"
                        class="timepicker"
                        locale="en"
                        @input="handleToTime" />
                </b-form-group>
            </div>
        </div>

        <div class="row p-0">
            <div v-if="timeSlotErrorMsg" class="error-msg p-0 ml-0">{{timeSlotErrorMsg}}</div>
        </div>
    </div>

</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import { TimeSlot } from '@/model/TimeSlot'
import { validateTimeSlot } from '@/helpers/ValidationHelpers'
import { applyTimeToDate } from '@/helpers/DateHelpers'

@Component
export default class TimeSlotPicker extends Vue {
  @Prop({ required: true }) baseDate: Date

  from: string = null
  to: string = null
  fromDateTime: Date = null
  toDateTime: Date = null
  timeSlotErrorMsg: string = null

  setTimeRangeErrorMsg (): void {
    this.timeSlotErrorMsg = 'Invalid time range'
  }

  resetTimeRangeErrorMsg (): void {
    this.timeSlotErrorMsg = null
  }

  handleFromTime (time: string) {
    const date = applyTimeToDate(this.baseDate, time)
    if (!validateTimeSlot(date, this.toDateTime)) {
      return this.setTimeRangeErrorMsg()
    }

    this.fromDateTime = date
    this.resetTimeRangeErrorMsg()
    this.selectSlot()
  }

  handleToTime (time: string) {
    const date = applyTimeToDate(this.baseDate, time)
    if (!validateTimeSlot(this.fromDateTime, date)) {
      return this.setTimeRangeErrorMsg()
    }

    this.toDateTime = date
    this.resetTimeRangeErrorMsg()
    this.selectSlot()
  }

  selectSlot () {
    if (this.fromDateTime && this.toDateTime) {
      this.$emit('selected-slot', new TimeSlot(this.fromDateTime, this.toDateTime))
    }
  }

  @Watch('baseDate', { immediate: true, deep: true })
  onChangeBaseDate () {
    this.from = null
    this.to = null
    this.fromDateTime = null
    this.toDateTime = null
    this.timeSlotErrorMsg = null
    this.$forceUpdate()
    this.$emit('selected-slot', null)
  }
}
</script>

<style scoped lang="scss" src="./TimeSlotPicker.scss" />
