<template>
    <div class="address-select">
        <div class="address-select-content col-md-6 order-sm-4 mb-4">
            <b-button class="address-select-button" v-b-modal.new-address-modal><i class="fas fa-plus"></i> New Address</b-button>
            <AddressFormModal modal-id="new-address-modal" header-title="New Address" id-label="Client ID" @emit-address="emittedNewAddress" button-name="Add"></AddressFormModal>
            <cool-select
                    v-model="selectedAddress"
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
import { Component, Vue } from 'vue-property-decorator'
import AddressFormModal from '@/components/AddressFormModal/AddressFormModal.vue'
import Alert from '@/components/Alert/Alert.vue'
import { CoolSelect } from 'vue-cool-select'
import { Address } from '@/model/Address'
import { getAuthenticatedToken, getDefaultConfig } from '@/service/AuthService'
import { prepareAddressData } from '@/helpers/AddressHelper'

@Component({
  components: { Alert, AddressFormModal, CoolSelect }
})
export default class AddressSelect extends Vue {
  addresses: Address[] = []
  selectedAddress: Address = null
  noData: boolean = null

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
    if (item.companyName) {
      str += item.companyName + ', '
    }
    if (item.addressLine1) {
      str += item.addressLine1 + ', '
    }
    if (item.addressLine2) {
      str += item.addressLine2 + ', '
    }
    if (item.town) {
      str += item.town + ', '
    }
    if (item.state) {
      str += item.state + ', '
    }
    if (item.postcode) {
      str += item.postcode
    }

    return str
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
        addressType: 'DELIVERY',
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
}
</script>

<style scoped lang="scss" src="./AddressSelect.scss"></style>
