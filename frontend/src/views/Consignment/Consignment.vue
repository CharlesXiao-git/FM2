<template>
    <div class="consignment row">
        <div class="consignment-alert">
                <Alert v-if="displayAlert" :variant="variant" :text="alertMessage" autoDismissInterval="5" />
        </div>
        <div class="col-11 m-auto p-0">
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
                                    :notes="specialInstructions"
                            />
                        </div>
                    </div>
                </div>
            </div>
            <div class="consignment-content">
                <div class="consignment-sub-heading mt-5 pt-2 pb-1 pl-4">
                    <h3>ITEM INFO</h3>
                </div>

                <ReferenceContainer @updated-references="getReferences" />
                <ItemPanel @calculate="calculate" @emitted-items="getEmittedItems"/>
            </div>
            <div v-if="showOffers" class="consignment-content">
                <div class="consignment-sub-heading mt-5 pt-2 pb-1 pl-4">
                    <h3>CARRIER</h3>
                </div>
                <CarrierPanel :dispatch-date="dispatchDate" :offers="offers" @selected-offer="getSelectedOffer"></CarrierPanel>
            </div>
            <div class="consignment-sub-heading mt-5 py-2 pl-4">
                <div class="row">
                    <div class="col-8 col-lg-10">
                        <h3>CONSIGNMENT</h3>
                    </div>
                    <div class="col-4 col-lg-2">
                        <b-button class="primary-button" @click="handleSubmit">Submit</b-button>
                        <!-- Print labels -->
                    </div>
                </div>
            </div>
            <br />
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
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
import ReferenceContainer from '@/components/Item/ReferenceContainer.vue'
import CarrierPanel from '@/components/Carrier/CarrierPanel.vue'
import { getAuthenticatedToken, getDefaultConfig } from '@/helpers/auth/RequestHelpers'
import { Offer } from '@/model/Offer'
import { Consignment as ConsignmentModel } from '@/model/Consignment'
import { Item } from '@/model/Item'
import Alert from '@/components/Alert/Alert.vue'

@Component({
  components: { Alert, CarrierPanel, ItemPanel, ClientSelect, DatePicker, DeliveryDetails, ReceiverTimeSlots, ReferenceContainer }
})
export default class Consignment extends Vue {
  @Prop({ default: 'NEW CONSIGNMENT' }) title: string
  @Prop({ required: true }) senderAddress: Address
  @Prop({ required: true }) receiverAddress: Address

  specialInstructions = this.receiverAddress ? this.receiverAddress.specialInstructions : ''
  isClient = isUserClient()
  selectedClient: ClientReference = null

  defaultDate: Date = new Date()
  dispatchDate: Date = this.defaultDate
  minDate: Date = subDays(this.defaultDate, 1)
  receiverTimeSlot: TimeSlot = null

  addressClass: AddressClass = 'BUSINESS'
  isAuthToLeave = false
  isTailgateRequired = false

  references: string[] = []

  showOffers = false
  offers: Offer[] = []
  selectedOffer: Offer = null

  items: Item[] = null

  displayAlert = false
  variant: string = null
  alertMessage: string = null

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

  getReferences (references: string[]) {
    this.references = references
  }

  calculate (calculate: boolean) {
    if (calculate) {
      // todo : validate the consignment and items
      // todo : once validated, create a consignment
      // todo : send the consignment to grab the offers

      this.showOffers = true

      this.$axios.get('/api/v1/consignment/offers', getDefaultConfig())
        .then(response => {
          this.offers = response.data
          this.offers.sort(function (a, b) {
            return a.totalCost - b.totalCost
          })
        }, error => {
          this.$log.error(error.response)
        })
    }
  }

  getEmittedItems (items: Item[]) {
    this.items = items
  }

  getSelectedOffer (selectedOffer: Offer) {
    this.selectedOffer = selectedOffer
  }

  handleSubmit () {
    // validate consignment

    const userClientId = this.selectedClient && this.selectedClient.id ? this.selectedClient.id : null
    const deliveryWindowBegin = this.receiverTimeSlot && this.receiverTimeSlot.from ? this.receiverTimeSlot.from : null
    const deliveryWindowEnd = this.receiverTimeSlot && this.receiverTimeSlot.to ? this.receiverTimeSlot.to : null
    const senderAddressId = this.senderAddress && this.senderAddress.id ? this.senderAddress.id : null
    const receiverAddressId = this.receiverAddress && this.receiverAddress.id ? this.receiverAddress.id : null
    const offerId = this.selectedOffer && this.selectedOffer.id ? this.selectedOffer.id : null

    const consignment = new ConsignmentModel(
      null,
      userClientId,
      null,
      this.dispatchDate,
      deliveryWindowBegin,
      deliveryWindowEnd,
      this.addressClass,
      this.isAuthToLeave,
      this.isTailgateRequired,
      senderAddressId,
      receiverAddressId,
      this.items
    )

    this.$axios.post('/api/v1/consignment', consignment, getDefaultConfig())
      .then(response => {
        if (response.data && response.data.id) {
          const consignmentId = response.data.id
          this.setSelectedOffer(consignmentId, offerId)
          this.setAlert('success', 'Consignment created successfully')
        }
      }, error => {
        this.$log.error(error)
        this.setAlert('danger', 'Error creating Consignment')
      })
  }

  setSelectedOffer (consignmentId: number, offerId: number) {
    const params = {
      offerId: offerId,
      consignmentId: consignmentId
    }

    const config = {
      headers: getAuthenticatedToken(),
      params: params
    }

    this.$axios.post('/api/v1/consignment/offers', null, config)
      .then(response => {
        if (response.status === 204) {

        }
      }, error => {
        this.$log.error(error.response)
      })
  }

  setAlert (variant: string, alertMessage: string) {
    this.displayAlert = true
    this.variant = variant
    this.alertMessage = alertMessage
  }

  @Watch('receiverAddress', { immediate: true, deep: true })
  onChangeReceiverAddress () {
    this.specialInstructions = this.receiverAddress ? this.receiverAddress.specialInstructions : ''
    this.$forceUpdate()
  }
}
</script>

<style scoped lang="scss" src="./Consignment.scss"></style>
