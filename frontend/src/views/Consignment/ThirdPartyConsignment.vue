<template>
    <Consignment title="NEW THIRD PARTY CONSIGNMENT" @selected-client="getSelectedClient">
        <template v-slot:sender>
            <AddressSelect address-type='SENDER' modal-id="sender-address-select" :client="selectedClient" @selected-address="getSelectedSenderAddress"></AddressSelect>
        </template>
        <template v-slot:receiver>
            <AddressSelect address-type='DELIVERY' modal-id="receiver-address-select" :client="selectedClient" @selected-address="getSelectedReceiverAddress"></AddressSelect>
        </template>
    </Consignment>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import AddressSelect from '@/components/AddressSelect/AddressSelect.vue'
import { ClientReference } from '@/helpers/types'
import { Address } from '@/model/Address'
import Consignment from '@/views/Consignment/Consignment.vue'

@Component({
  components: { Consignment, AddressSelect }
})
export default class ThirdPartyConsignment extends Vue {
  selectedClient: ClientReference = null
  senderAddress: Address = null
  receiverAddress: Address = null

  getSelectedClient (selectedClient: ClientReference) {
    this.selectedClient = selectedClient
  }

  getSelectedSenderAddress (address: Address) {
    this.senderAddress = address
  }

  getSelectedReceiverAddress (address: Address) {
    this.receiverAddress = address
  }
}
</script>
