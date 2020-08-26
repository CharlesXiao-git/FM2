<template>
    <Consignment title="RETURN CONSIGNMENT" @selected-client="getSelectedClient">
        <template v-slot:sender>
            <AddressSelect modal-id="sender-address-select" :client="selectedClient" @selected-address="getSelectedAddress"></AddressSelect>
        </template>
        <template v-slot:receiver>
            <template v-if="receiverAddress">
                <strong>{{ receiverAddress.companyName }}</strong>
                <p class="consignment-default-address"> {{ receiverAddress.addressLine1 }}
                    <template v-if="receiverAddress.addressLine2">
                        , {{ receiverAddress.addressLine2 }}
                    </template>
                    , {{ receiverAddress.town }}, {{ receiverAddress.state }}, {{ receiverAddress.postcode }}
                </p>
            </template>
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
export default class ReturnConsignment extends Vue {
  selectedClient: ClientReference = null
  senderAddress: Address = null
  receiverAddress: Address = null

  getSelectedClient (selectedClient: ClientReference) {
    this.selectedClient = selectedClient
    this.getReceiverAddress(this.selectedClient.id)
  }

  getSelectedAddress (address: Address) {
    this.senderAddress = address
  }

  getReceiverAddress (clientId: number = null) {
    const config = {
      headers: getAuthenticatedToken(),
      params: {
        clientId: clientId
      }
    }

    this.$axios.get('/api/v1/address/default', config)
      .then(response => {
        this.receiverAddress = response.data
      }, error => {
        this.$log.error(error.response.data)
      })
  }

  created () {
    this.getReceiverAddress()
  }
}
</script>
