<template>
    <div class="consignment col-11 m-auto p-0">
        <div class="consignment-content mt-5">
            <div class="consignment-sub-heading pt-2 pb-1 px-4">
                <div class="row">
                    <h3 class="col-8 col-sm-6 col-md-6 col-lg-8 col-xl-9">{{ title }}</h3>
                    <template v-if="!isClient">
                        <ClientSelect class="col-6 col-sm-6 col-lg-4 col-xl-3" @selected-client="getSelectedClient"></ClientSelect>
                    </template>
                </div>
            </div>

            <div class="consignment-dispatch-date row px-4 pt-4">
                <div class="col-md-6">
                    <b-form-group
                        label="Dispatch date"
                        label-align-sm="left"
                        label-for="dispatch-date"
                        label-class="font-weight-bold pt-2 p-0"
                    >
                        <DatePicker
                            name='dispatch-date'
                            :min-value="minDate"
                            :default-value="defaultDate"
                            @selected-date="handleDispatchDate"
                        />
                    </b-form-group>
                </div>

                <div class="col-md-6">
                    <ReceiverTimeSlots @selected-time-slot="handleReceiverTimeslot" :dispatch-date="dispatchDate" />
                </div>
            </div>

            <div class="consignment-sender-receiver row px-4 py-2">
                <div class="col-md-6">
                    <h3>Sender Details</h3>
                    <slot name="sender"></slot>
                </div>
                <div class="col-md-6">
                    <h3>Receiver Details</h3>
                    <slot name="receiver"></slot>
                </div>
            </div>
            <div class="consignment-sender-receiver row">
                <div class="col-md-6">
                    <!-- Handled by, packed by, managed by selection-->
                </div>
                <div class="col-md-6">
                    <div class="mt-3 ml-2">
                        <DeliveryDetails
                                @selected-address-class="getAddressClass"
                                @selected-special-instructions="getSpecialInstructions"
                                @selected-auth-to-leave="getAuthToLeave"
                                @selected-tailgate-required="getTailgateRequired"
                        />
                    </div>
                </div>
            </div>
        </div>
        <div class="consignment-content">
            <div class="consignment-sub-heading mt-5 pt-2 pb-1 pl-4">
                <h3>ITEM INFO</h3>
            </div>
            <ItemPanel />
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import ClientSelect from '@/components/ClientSelect/ClientSelect.vue'
import { isUserClient } from '@/helpers/auth/UserHelpers'
import { ClientReference } from '@/helpers/types'
import ItemPanel from '@/components/Item/ItemPanel.vue'
import DatePicker from '@/components/DatePicker/DatePicker.vue'
import { subDays } from 'date-fns'
import DeliveryDetails from '@/components/ReceiverDetails/DeliveryDetails.vue'
import AddressClass from '@/helpers/types/AddressClass.ts'
import ReceiverTimeSlots from '@/components/ReceiverDetails/ReceiverTimeSlots.vue'
import { TimeSlot } from '@/model/TimeSlot'
import { Address } from '@/model/Address'

@Component({
  components: { ItemPanel, ClientSelect, DatePicker, DeliveryDetails, ReceiverTimeSlots }
})
export default class Consignment extends Vue {
  @Prop({ default: 'NEW CONSIGNMENT' }) title: string
  @Prop({ required: true }) senderAddress: Address
  @Prop({ required: true }) receiverAddress: Address
  isClient = isUserClient()
  selectedClient: ClientReference = null

  defaultDate: Date = new Date()
  dispatchDate: Date = this.defaultDate
  minDate: Date = subDays(this.defaultDate, 1)
  receiverTimeSlot: TimeSlot = null

  addressClass: AddressClass = 'BUSINESS'
  specialInstructions: string = null
  isAuthToLeave = false
  isTailgateRequired = false

  handleDispatchDate (date: Date) {
    this.dispatchDate = date
  }

  handleReceiverTimeslot (timeslot: TimeSlot) {
    this.receiverTimeSlot = timeslot
  }

  getSelectedClient (selectedClient: ClientReference) {
    this.selectedClient = selectedClient
    this.$emit('selected-client', this.selectedClient)
  }

  getAddressClass (addressClass: AddressClass) {
    this.addressClass = addressClass
  }

  getSpecialInstructions (specialInstructions: string) {
    this.specialInstructions = specialInstructions
  }

  getAuthToLeave (authToLeave: boolean) {
    this.isAuthToLeave = authToLeave
  }

  getTailgateRequired (tailgateRequired: boolean) {
    this.isTailgateRequired = tailgateRequired
  }
}
</script>

<style scoped lang="scss" src="./Consignment.scss"></style>
