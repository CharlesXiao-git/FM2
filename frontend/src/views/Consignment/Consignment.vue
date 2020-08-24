<template>
    <div class="consignment col-11 m-auto p-0">
        <div class="consignment-content mt-5">
            <div class="consignment-sub-heading pt-2 pb-1 px-4">
                <div class="row">
                    <h3 class="col-8 col-sm-6 col-md-6 col-lg-8 col-xl-9">NEW CONSIGNMENT</h3>
                    <template v-if="!isClient">
                        <ClientSelect class="col-6 col-sm-6 col-lg-4 col-xl-3" @selected-client="getSelectedClient"></ClientSelect>
                    </template>
                </div>
            </div>
            <div class="consignment-sender-receiver row p-4">
                <div class="col-md-6">
                    <h3>Sender Details</h3>
                    <template v-if="senderAddress">
                        <b>{{ senderAddress.companyName }}</b>
                        <p class="consignment-sender-address"> {{ senderAddress.addressLine1 }}
                            <template v-if="senderAddress.addressLine2">
                                , {{ senderAddress.addressLine2 }}
                            </template>
                            , {{ senderAddress.town }}, {{ senderAddress.state }}, {{ senderAddress.postcode }}
                        </p>
                    </template>
                    <template v-else>
                        <AddressSelect modal-id="sender-address-select" :client="selectedClient"></AddressSelect>
                    </template>
                </div>
                <div class="col-md-6">
                    <h3>Receiver Details</h3>
                    <AddressSelect modal-id="receiver-address-select" :client="selectedClient"></AddressSelect>
                </div>
            </div>
        </div>
        <div class="consignment-content">
            <div class="consignment-sub-heading mt-5 pt-2 pb-1 pl-4">
                <h3>ITEM INFO</h3>
            </div>
            <div v-for="item in items" :key="item.id">
                <ItemPanel :item="item" :item-types="itemTypes" @delete-item="deleteItem" @emitted-item="emittedItem"></ItemPanel>
            </div>
            <b-button class="primary-button m-3">Calculate</b-button>
            <b-button class="secondary-button ml-2" v-on:click="addItem" :disabled="disableAdd"><i class="fas mr-2 fa-plus" />Add Item</b-button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import AddressSelect from '@/components/AddressSelect/AddressSelect.vue'
import ItemPanel from '@/components/ItemPanel/ItemPanel.vue'
import { Item } from '@/model/Item'
import { getAuthenticatedToken } from '@/helpers/auth/RequestHelpers'
import ClientSelect from '@/components/ClientSelect/ClientSelect.vue'
import { isUserClient } from '@/helpers/auth/UserHelpers'
import { clientReference } from '@/helpers/types'
import { Address } from '@/model/Address'

@Component({
  components: { ClientSelect, AddressSelect, ItemPanel }
})
export default class Consignment extends Vue {
  isClient = isUserClient()
  selectedClient: clientReference = null
  id = 0
  items: Item[] = [new Item(this.id)]
  disableAdd = false
  itemTypes: [] = null
  senderAddress: Address = null

  getSelectedClient (selectedClient: clientReference) {
    this.selectedClient = selectedClient
    this.getSenderAddress(this.selectedClient.id)
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

  addItem () {
    if (this.items.length !== 0) {
      this.id = this.items[this.items.length - 1].id
    }
    this.items.push(new Item(this.id + 1))
  }

  deleteItem (item: Item) {
    this.items.splice(this.items.indexOf(item), 1)
    if (this.items.length === 0) {
      this.disableAdd = false
    }
    this.$forceUpdate()
  }

  emittedItem (item: Item, oldItem: Item, deleteTheItem = false) {
    this.disableAdd = false
    if (this.items.indexOf(oldItem) !== -1) {
      this.items[this.items.indexOf(oldItem)] = item
    }

    if (deleteTheItem) {
      if (this.items.length > 1) {
        this.items.splice(this.items.indexOf(item), 1)
      } else {
        this.disableAdd = true
      }
    }
    this.$forceUpdate()
  }

  created () {
    const config = {
      headers: getAuthenticatedToken(),
      params: {
        isCustom: false
      }
    }

    this.$axios.get('/api/v1/consignment/itemTypes', config)
      .then(response => {
        this.itemTypes = response.data
      }, error => {
        this.$log.error(error.response.data)
      })

    this.getSenderAddress()
  }
}
</script>

<style scoped lang="scss" src="./Consignment.scss">
</style>
