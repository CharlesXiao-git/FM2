<template>
    <div class="p-4">
        <template v-if="lowestOffer">
            Lowest Price
            <div class="selected-data-table mt-2">
                <b-table responsive="true" :items="[lowestOffer]" :fields="fieldsToShow">
                    <template v-slot:cell(selectedOffer)="data">
                        <b-form-checkbox name="selected-items" v-model="selectedOffer" :value="data.item"></b-form-checkbox>
                    </template>
                </b-table>
            </div>
        </template>
        <template v-if="offers.length >= 1">
            <br />
            All Carriers
            <div class="data-table mt-2">
                <b-table responsive="true" :items="offers" :fields="fieldsToShow">
                    <template v-slot:cell(selectedOffer)="data">
                        <b-form-checkbox name="selected-items" v-model="selectedOffer" :value="data.item"></b-form-checkbox>
                    </template>
                </b-table>
            </div>
        </template>
        <template v-else>
            No offers found
        </template>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import { Offer } from '@/model/Offer'
import { addDays, format } from 'date-fns'

@Component
export default class CarrierPanel extends Vue {
  @Prop({ required: true }) offers: Offer[]
  @Prop({ default: new Date() }) dispatchDate: Date

  selectedOffer: Offer = null
  lowestOffer: Offer = null

  fieldsToShow = [
    { key: 'carrierAccount.carrier.displayName', label: 'Carrier Name' },
    { key: 'eta', label: 'ETA', formatter: 'etaFormatter' },
    { key: 'freightCost', label: 'Freight Cost', formatter: 'priceFormatter' },
    { key: 'category1Fees', label: 'DG / HTG/ Zone Surcharge', formatter: 'priceFormatter' },
    { key: 'fuelSurcharge', label: 'Fuel Levy', formatter: 'priceFormatter' },
    { key: 'category2Fees', label: 'Additional Fees', formatter: 'priceFormatter' },
    { key: 'totalCost', label: 'Total inc. GST', formatter: 'priceFormatter' },
    { key: 'selectedOffer', label: '' }
  ]

  priceFormatter (value: string) {
    return '$' + value
  }

  etaFormatter (value: number) {
    if (value) {
      const eta = addDays(this.dispatchDate, value)
      return format(eta, 'd MMM yyyy')
    }
    return null
  }

  @Watch('offers', { immediate: true, deep: true })
  onOffersChange () {
    if (this.offers.length > 0) {
      this.lowestOffer = this.offers[0]
    }
  }

  @Watch('selectedOffer', { immediate: true, deep: true })
  onSelectedOfferChange () {
    this.$emit('selected-offer', this.selectedOffer)
  }
}
</script>

<style scoped lang="scss" src="./CarrierPanel.scss"></style>
