<template>
  <div class="data-table">
    <div class="row">
      <div class="col-lg">
        <b-form-group label="Show entries" label-cols="3" label-cols-lg="4" label-cols-xl="3" label-for="pageSelect">
          <b-form-select :options="options" v-model="perPage" id="pageSelect"></b-form-select>
        </b-form-group>
      </div>
      <div class="col-lg">
        <b-form-group class="right" label="Search:" label-cols="4" label-cols-md="3" label-for="filterInput">
          <b-form-input v-model="filter" type="search" id="filterInput" placeholder="Type to Search"></b-form-input>
        </b-form-group>
      </div>
    </div>
    <div class="row">
      <b-button :disabled="this.selectedItems.length < 1" class="secondary-button ml-2 mb-2" v-b-modal.confirm-action-modal><i class="fas mr-2 fa-trash-alt" />Delete</b-button>
      <ConfirmActionModal modal-id="confirm-action-modal" button-name="Yes, delete it!" button-class="warning-button" @confirmed="deleteItems">
        <template v-slot:content>
          <i class="fa fa-exclamation-triangle fa-2x"/><br/>
          <h3>Delete selected address(es)?</h3>
          <p>Are you sure? <br/> You will not be able to recover the addresses once deleted.</p>
        </template>
      </ConfirmActionModal>
    </div>
    <b-table responsive="true" hover :items="addresses" :fields="fieldsToShow" :current-page="currentPage" :per-page="perPage" :filter="filter">
      <template v-slot:cell(delete)="data">
        <b-form-checkbox name="selected-items" v-model="selectedItems" :value="data.item"></b-form-checkbox>
      </template>
      <template v-slot:cell(edit)="data">
          <b-button class="edit-button" @click="getModal(data.item, $event.target)"><i class="fas fa-edit"></i></b-button>
      </template>
    </b-table>
    <AddressFormModal modal-id="update-modal" header-title="Update Address" id-label="Address Reference" :address="modalContent" :client="client" button-name="Update" @emit-address="emittedAddress"></AddressFormModal>
    <div class="justify-content-center row my-1">
      <b-pagination size="md" :total-rows="totalRows" :per-page="perPage" v-model="currentPage" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import AddressFormModal from '@/components/AddressFormModal/AddressFormModal.vue'
import { Address } from '@/model/Address'
import ConfirmActionModal from '@/components/ConfirmActionModal/ConfirmActionModal.vue'
import { prepareAddressData } from '@/helpers/AddressHelpers'
import { getDefaultConfig } from '@/helpers/auth/RequestHelpers'
import { ClientReference } from '@/helpers/types'

@Component({
  components: { ConfirmActionModal, AddressFormModal }
})
export default class AddressTable extends Vue {
  @Prop({ required: true }) address: Address[]

  selectedItems: Address[] = []
  fieldsToShow = [
    { key: 'delete', label: '' },
    { key: 'referenceId', label: 'Address Reference' },
    { key: 'company', label: 'Company Name' },
    { key: 'contactName', label: 'Contact Name' },
    { key: 'phoneNumber', label: 'Contact Number' },
    { key: 'email', label: 'Contact Email' },
    { key: 'addressLine1', label: 'Address Line 1' },
    { key: 'addressLine2', label: 'Address Line 2' },
    { key: 'suburb.name', label: 'Town' },
    { key: 'suburb.postcode', label: 'Postcode' },
    { key: 'suburb.state', label: 'State' },
    { key: 'specialInstructions', label: 'Special Instructions' },
    { key: 'edit', label: 'Edit' }
  ]

  options = [
    { text: 10, value: 10 },
    { text: 25, value: 25 },
    { text: 50, value: 50 },
    { text: 100, value: 100 },
    { text: 200, value: 200 }
  ]

  perPage = 10
  addresses = this.address
  currentPage = 1
  filter: string | RegExp | object | [] = null
  totalRows = this.addresses ? this.addresses.length : this.perPage

  modalContent: Address = null
  client: ClientReference = null

  emittedAddress (address: Address) {
    this.$emit('emit-address', address)
  }

  getModal (item: Address, button: object) {
    this.modalContent = prepareAddressData(item)
    this.populateClient(item.userClientId)
    this.$root.$emit('bv::show::modal', 'update-modal', button)
  }

  populateClient (clientId: number) {
    this.$axios.get('/api/v1/user/children', getDefaultConfig())
      .then(response => {
        const clientOptions = Object.keys(response.data).sort()
          .map(key => ({ id: Number(response.data[key]), name: key }))

        this.client = clientOptions.find(client => client.id === clientId)
      }, error => {
        this.$log.error(error.response.data)
      })
  }

  deleteItems () {
    this.$emit('emit-delete-address', this.selectedItems)
  }
}
</script>

<style scoped lang="scss" src="./AddressDataTable.scss"></style>
