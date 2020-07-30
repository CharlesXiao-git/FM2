<template>
    <div class="address-book row">
        <div class="address-book-alert">
            <Alert v-if="confirmAddAddress" variant="success" text="Address added successfully" autoDismissInterval="10" />
            <Alert v-if="errorAddAddress" variant="danger" text="Error adding new Address" autoDismissInterval="10" />
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
                <AddressFormModal modal-id="new-address-modal" header-title="New Address" id-label="Receiver ID" @emit-address="emittedNewAddress"></AddressFormModal>
            </div>
            <div class="row">
                <template v-if="loading">Loading...</template>
                <template v-else>
                    <template v-if="noAddresses">{{ noAddressesErrorMessage }}</template>
                    <template v-else-if="errorFetching">{{ errorFetchingMessage }}</template>
                    <template v-else>
                        <AddressTable :address="addresses"></AddressTable>
                    </template>
                </template>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import AddressFormModal from '@/components/AddressFormModal/AddressFormModal.vue'
import { Address } from '@/model/Address'
import { getAuthenticatedToken } from '@/service/AuthService'
import AddressTable from '@/components/AddressTable/AddressTable.vue'
import Alert from '@/components/Alert/Alert.vue'

@Component({
  components: { Alert, AddressTable, AddressFormModal }
})

export default class AddressBook extends Vue {
  confirmAddAddress = false
  errorAddAddress = false
  addresses: Address[] = []

  errorFetching = false
  errorFetchingMessage = 'Error while fetching addresses'
  noAddressesErrorMessage = 'No addresses available'
  noAddresses = false
  loading = true

  emittedNewAddress (address: Address) {
    const config = {
      headers: getAuthenticatedToken()
    }

    this.$axios.post('/api/v1/address', this.prepareDataNewAddress(address), config)
      .then(response => {
        address = response.data
        this.addresses.unshift(address)
        this.confirmAddAddress = true
      }, error => {
        this.$log.warn(error)
        this.errorAddAddress = true
      })
  }

  prepareDataNewAddress (address: Address) {
    return {
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
  }

  created (): void {
    const config = {
      headers: getAuthenticatedToken(),
      params: {
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
