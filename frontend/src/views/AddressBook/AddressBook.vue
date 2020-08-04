<template>
    <div class="address-book row">
        <div class="address-book-alert">
            <Alert v-if="displayAlert" :variant="variant" :text="alertMessage" autoDismissInterval="5" />
        </div>
        <div class="address-book-header">
            <div class="d-flex col-md-11 col-12 ml-xl-auto ml-2 mr-auto py-4">
                <div class="img-container">
                    <i class="fas fa-address-book"></i>
                </div>
                <div class="pl-4">
                    <h5>Address Book</h5>
                    <p>List of all your addresses</p>
                </div>
            </div>
        </div>
        <div class="address-book-content col-md-11 col-12 ml-xl-auto ml-2 mr-auto mt-4">
            <div class="row">
                <b-button class="primary-button mb-4" v-b-modal.new-address-modal ><i class="fas mr-2 fa-plus"></i>New Address</b-button>
                <AddressFormModal modal-id="new-address-modal" header-title="New Address" id-label="Receiver ID" @emit-address="emittedNewAddress" button-name="Add"></AddressFormModal>
            </div>
            <div class="row">
                <template v-if="loading">Loading...</template>
                <template v-else>
                    <template v-if="noAddresses">{{ noAddressesErrorMessage }}</template>
                    <template v-else-if="errorFetching">{{ errorFetchingMessage }}</template>
                    <template v-else>
                        <AddressDataTable :address="addresses" @emit-address="emittedUpdatedAddress" @emit-delete-address="emittedDeleteAddress"></AddressDataTable>
                    </template>
                </template>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { Address } from '@/model/Address'
import { getAuthenticatedToken, getDefaultConfig } from '@/service/AuthService'
import AddressDataTable from '@/components/AddressDataTable/AddressDataTable.vue'
import AddressFormModal from '@/components/AddressFormModal/AddressFormModal.vue'
import Alert from '@/components/Alert/Alert.vue'

@Component({
  components: { AddressDataTable, AddressFormModal, Alert }
})

export default class AddressBook extends Vue {
  displayAlert = false
  variant: string = null
  alertMessage: string = null
  addresses: Address[] = []
  errorFetching = false
  errorFetchingMessage = 'Error while fetching addresses'
  noAddresses = false
  noAddressesErrorMessage = 'No addresses found'

  loading = true

  emittedNewAddress (address: Address) {
    this.resetFlag()
    this.$axios.post('/api/v1/address', this.prepareAddressData(address), getDefaultConfig())
      .then(response => {
        address = response.data
        this.addresses.unshift(address)
        this.setAlert('success', 'Address added successfully')
      }, error => {
        this.$log.warn(error)
        this.setAlert('danger', 'Error adding new Address')
      })
  }

  emittedUpdatedAddress (address: Address) {
    this.resetFlag()
    this.$axios.put('/api/v1/address', this.prepareAddressData(address), getDefaultConfig())
      .then(response => {
        this.addresses.splice(this.addresses.indexOf(this.addresses.find(oldAddress => oldAddress.id === address.id)), 1)
        const updatedAddress = response.data
        this.addresses.unshift(updatedAddress)
        this.setAlert('success', 'Address updated successfully')
      }, error => {
        this.$log.warn(error)
        this.setAlert('danger', 'Error updating Address')
      })
  }

  emittedDeleteAddress (addresses: Address[]) {
    this.resetFlag()
    this.$axios.delete('/api/v1/address', this.prepareConfig(addresses))
      .then(response => {
        this.$log.info(response.data)
        addresses.forEach(address => {
          this.addresses.splice(this.addresses.indexOf(address), 1)
        })
        this.setAlert('success', 'Address(es) deleted successfully')
      }, error => {
        this.$log.warn(error)
        this.setAlert('danger', 'Error deleting address(es)')
      })
  }

  setAlert (variant: string, alertMessage: string) {
    this.displayAlert = true
    this.variant = variant
    this.alertMessage = alertMessage
  }

  resetFlag () {
    this.displayAlert = false
  }

  prepareConfig (addresses: Address[]) {
    const params = new URLSearchParams()

    addresses.forEach(address => {
      params.append('ids', address.id)
    })

    return {
      headers: getAuthenticatedToken(),
      params: params
    }
  }

  prepareAddressData (address: Address) {
    const sendAddressData: Address = {
      id: null,
      addressType: 'DELIVERY',
      referenceId: address.referenceId,
      companyName: address.companyName,
      addressLine1: address.addressLine1,
      addressLine2: address.addressLine2,
      town: address.town,
      postcode: address.postcode,
      state: address.state,
      contactName: address.contactName,
      contactNo: address.contactNo,
      contactEmail: address.contactEmail,
      notes: address.notes
    }
    if (address.id) {
      sendAddressData.id = address.id
    }

    return sendAddressData
  }

  created (): void {
    const config = {
      headers: getAuthenticatedToken(),
      params: {
        addressType: 'DELIVERY',
        page: 0,
        size: 10000
      }
    }

    this.$axios.get('/api/v1/address', config)
      .then(response => {
        this.addresses = response.data.addresses
        this.addresses.reverse()
        this.loading = false
        this.noAddresses = response.data.addresses.length === 0
      }, error => {
        this.loading = false
        this.errorFetching = true
        this.$log.error(error.response.data)
      })
  }
}
</script>

<style scoped lang="scss" src="./AddressBook.scss"></style>
