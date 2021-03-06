<template>
    <div class="address-select">
        <div class="address-select-content">
            <b-button class="address-select-button mb-2" v-b-modal="modalId"><i class="fas fa-plus"></i> New Address</b-button>
            <AddressFormModal :address-type="addressType" :modal-id="modalId" header-title="New Address" id-label="Address Reference" :client="client" @emit-address="emittedNewAddress" button-name="Add"></AddressFormModal>
            <cool-select
                    v-model="selectedAddress"
                    v-on:input="emitSelectedAddress"
                    :items="addresses" class="mt-2"
                    disable-filtering-by-search
                    @search="onSearch"
                    :placeholder="selectedAddress ? '' : 'Select Address'"
            >
                <template #no-data>
                    {{ noData ? "No address found" : "We need at least 3 letters / numbers to search" }}
                </template>
                <template #item="{ item }">
                    <div class="item">
                        {{ getItem(item) }}
                    </div>
                </template>
                <template slot="selection" slot-scope="{ item }">
                    <div class="item">
                        {{ getItem(item) }}
                    </div>
                </template>
            </cool-select>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import AddressFormModal from '@/components/AddressFormModal/AddressFormModal.vue'
import Alert from '@/components/Alert/Alert.vue'
import { CoolSelect } from 'vue-cool-select'
import { Address } from '@/model/Address'
import { getAuthenticatedToken, getDefaultConfig } from '@/helpers/auth/RequestHelpers'
import { prepareAddressData } from '@/helpers/AddressHelpers'
import { ClientReference } from '@/helpers/types'

@Component({
  components: { Alert, AddressFormModal, CoolSelect }
})
export default class AddressSelect extends Vue {
  @Prop({ default: 'DELIVERY' }) addressType: string
  @Prop() client: ClientReference
  @Prop({ default: 'new-address-modal' }) modalId: string
  addresses: Address[] = []
  selectedAddress: Address = null
  noData: boolean = null

  emitSelectedAddress () {
    this.$emit('selected-address', this.selectedAddress)
  }

  emittedNewAddress (address: Address) {
    this.$axios.post('/api/v1/address', prepareAddressData(address), getDefaultConfig())
      .then(response => {
        address = response.data
        this.addresses.unshift(address)
        this.selectedAddress = address
      }, error => {
        this.$log.warn(error)
      })
  }

  getItem (item: Address) {
    let str = ''
    if (item.referenceId) {
      str += item.referenceId + ', '
    }
    if (item.company) {
      str += item.company + ', '
    }
    if (item.addressLine1) {
      str += item.addressLine1 + ', '
    }
    if (item.addressLine2) {
      str += item.addressLine2 + ', '
    }
    if (item.suburb && item.suburb.name) {
      str += item.suburb.name + ', '
    }
    if (item.suburb && item.suburb.state) {
      str += item.suburb.state + ', '
    }
    if (item.suburb && item.suburb.postcode) {
      str += item.suburb.postcode
    }

    return str
  }

  getClientId () {
    if (this.client && this.client.id) {
      return this.client.id
    }
    return null
  }

  onSearch (search: string) {
    const lettersLimit = 3

    this.noData = false
    if (search.length < lettersLimit) {
      this.addresses = []
      return
    }

    const config = {
      headers: getAuthenticatedToken(),
      params: {
        addressType: this.addressType,
        clientId: this.getClientId(),
        criteria: search
      }
    }

    this.$axios.post('/api/v1/address/search', null, config)
      .then(response => {
        this.addresses = response.data
        if (!this.addresses.length) {
          this.noData = true
        }
      }, error => {
        this.$log.error(error)
      })
  }

  @Watch('client', { immediate: true, deep: true })
  onChangeClient () {
    this.selectedAddress = null
    this.addresses = []
    this.$forceUpdate()
  }
}
</script>

<style scoped lang="scss" src="./AddressSelect.scss"></style>
