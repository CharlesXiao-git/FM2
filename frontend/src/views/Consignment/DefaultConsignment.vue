<template>
    <Consignment @selected-client="getSelectedClient">
        <template v-slot:sender>
            <template v-if="senderAddress">
                <strong>{{ senderAddress.companyName }}</strong>
                <p class="consignment-sender-address"> {{ senderAddress.addressLine1 }}
                    <template v-if="senderAddress.addressLine2">
                        , {{ senderAddress.addressLine2 }}
                    </template>
                    , {{ senderAddress.town }}, {{ senderAddress.state }}, {{ senderAddress.postcode }}
                </p>
            </template>
        </template>
        <template v-slot:receiver>
            <AddressSelect modal-id="receiver-address-select" :client="selectedClient" @selected-address="getSelectedAddress"></AddressSelect>
        </template>
    </Consignment>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import AddressSelect from '@/components/AddressSelect/AddressSelect.vue'
import { getAuthenticatedToken } from '@/helpers/auth/RequestHelpers'
import { ClientReference } from '@/helpers/types'
import { Address } from '@/model/Address'
import Consignment from '@/views/Consignment/Consignment.vue'

@Component({
  components: { Consignment, AddressSelect }
})
export default class DefaultConsignment extends Vue {
  selectedClient: ClientReference = null
  senderAddress: Address = null
  receiverAddress: Address = null

  getSelectedClient (selectedClient: ClientReference) {
    this.selectedClient = selectedClient
    this.getSenderAddress(this.selectedClient.id)
  }

  getSelectedAddress (address: Address) {
    this.receiverAddress = address
  }

  getSenderAddress (clientId: number = null) {
    const config = {
      headers: getAuthenticatedToken(),
      params: {
        clientId: clientId
      }
    }

    this.$axios.get('/api/v1/address/default', config)
      .then(response => {
        this.senderAddress = response.data
      }, error => {
        this.$log.error(error.response.data)
      })
  }

  created () {
    this.getSenderAddress()
  }
}
</script>
