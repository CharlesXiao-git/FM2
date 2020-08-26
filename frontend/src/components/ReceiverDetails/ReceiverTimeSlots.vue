<template>
  <div>
    <div class="row">
      <div class="mt-2">
        <b-form-checkbox v-model="isSelected" class="font-weight-bold">Receiver time slots</b-form-checkbox>
      </div>
    </div>

    <div v-if="isSelected" class="row pb-4">
      <div class="p-0">
        <div class="row p-0">
        <div class="col-12 mt-2 p-0">
            <b-form-group
                label="Receive on date"
                label-align-sm="left"
                label-for="receive-date"
                label-class="font-weight-bold pt-2 p-0"
            >
              <DatePicker
                  name='receive-date'
                  :min-value="dispatchDate"
                  :show-reset-btn="true"
                  @selected-date="handleReceivedDate"
              />
            </b-form-group>
          </div>
          <div class="col-12 mt-2 p-0">
            <TimeSlotPicker :base-date="selectedDate" @selected-slot="handleTimeSlot"></TimeSlotPicker>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop, Watch } from 'vue-property-decorator'
import DatePicker from '@/components/DatePicker/DatePicker.vue'
import TimeSlotPicker from '@/components/TimeSlotPicker/TimeSlotPicker.vue'
import { TimeSlot } from '@/model/TimeSlot.ts'

@Component({
  components: { DatePicker, TimeSlotPicker }
})
export default class ReceiverTimeSlots extends Vue {
  @Prop({ default: new Date() }) private dispatchDate: Date

  isSelected = false
  selectedDate: Date = null
  timeSlot: TimeSlot = null

  handleReceivedDate (receiveDate: Date) {
    this.selectedDate = receiveDate
  }

  handleTimeSlot (timeSlot: TimeSlot) {
    this.timeSlot = timeSlot
    this.$emit('selected-time-slot', this.timeSlot)
  }

  @Watch('dispatchDate', { immediate: true, deep: true })
  onDispatchDate () {
    this.selectedDate = null
    this.timeSlot = null
    this.$forceUpdate()
  }
}
</script>
