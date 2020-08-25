<template>
  <div>
    <div class="row">
      <div class="mt-2">
        <b-form-checkbox v-model="isSelected">Receiver time slots</b-form-checkbox>
      </div>
    </div>

    <div v-if="isSelected" class="row pl-2">
      <div class="col-lg-12 col-xl-5 mt-2 p-0">
        <b-form-group
            label="Receive on date"
            label-align-sm="left"
            label-for="receive-date"
            label-class="font-weight-bold pt-2 p-0"
        >
          <DatePicker
              name='receive-date'
              :min-value="minDate"
              @selected-date="handleReceivedDate"
          />
        </b-form-group>
      </div>
      <div class="col-lg-12 col-xl-7 mt-2 pl-0">
        <TimeSlotPicker :base-date="selectedDate" @selected-slot="handleTimeSlot"></TimeSlotPicker>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { subDays } from 'date-fns'
import DatePicker from '@/components/DatePicker/DatePicker.vue'
import TimeSlotPicker from '@/components/TimeSlotPicker/TimeSlotPicker.vue'
import { TimeSlot } from '@/model/TimeSlot.ts'

@Component({
  components: { DatePicker, TimeSlotPicker }
})
export default class ReceiverTimeSlots extends Vue {
  isSelected = false
  minDate: Date = subDays(new Date(), 1)
  selectedDate: Date = null
  timeSlot: TimeSlot = null

  handleReceivedDate (receiveDate: Date) {
    this.selectedDate = receiveDate
  }

  handleTimeSlot (timeSlot: TimeSlot) {
    this.timeSlot = timeSlot
    this.$emit('selected-time-slot', this.timeSlot)
  }
}
</script>
