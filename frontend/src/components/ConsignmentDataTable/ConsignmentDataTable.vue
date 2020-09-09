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
    <div v-if="isMutable" class="row">
      <b-button :disabled="this.selectedItems.length < 1" class="secondary-button ml-2 mb-2" v-b-modal.confirm-action-modal><i class="fas mr-2 fa-trash-alt" />Delete</b-button>
      <ConfirmActionModal modal-id="confirm-action-modal" button-name="Yes, delete it!" button-class="warning-button" @confirmed="deleteItems">
        <template v-slot:content>
          <i class="fa fa-exclamation-triangle fa-2x"/><br/>
          <h3>Delete selected consignment(s)?</h3>
          <p>Are you sure? <br/> You will not be able to recover the consignments once deleted.</p>
        </template>
      </ConfirmActionModal>
    </div>
    <b-table responsive="true" hover :items="consignments" :fields="fieldsToShow" :current-page="currentPage" :per-page="perPage" :filter="filter">
      <template v-slot:cell(delete)="data">
        <b-form-checkbox name="selected-items" v-model="selectedItems" :value="data.item"></b-form-checkbox>
      </template>
      <template v-slot:cell(dispatchedAt)="data">
        <template v-if="data.item.dispatchedAt">
          {{ data.item.dispatchedAt[3] }}/{{ data.item.dispatchedAt[1] }}/{{ data.item.dispatchedAt[0] }}
        </template>
      </template>
    </b-table>
    <div class="justify-content-center row my-1">
      <b-pagination size="md" :total-rows="totalRows" :per-page="perPage" v-model="currentPage" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import { Consignment } from '@/model/Consignment'
import ConfirmActionModal from '@/components/ConfirmActionModal/ConfirmActionModal.vue'

@Component({
  components: { ConfirmActionModal }
})
export default class ConsignmentDataTable extends Vue {
  @Prop({ required: true }) consignment: Consignment[]
  @Prop({ default: false }) isMutable: boolean

  // TODO update carrier field to point at carrier.name
  selectedItems: Consignment[] = []
  fieldsToShow = this.getFieldsToShow()

  options = [
    { text: 10, value: 10 },
    { text: 25, value: 25 },
    { text: 50, value: 50 },
    { text: 100, value: 100 },
    { text: 200, value: 200 }
  ]

  perPage = 10
  consignments = this.consignment
  currentPage = 1
  filter: string | RegExp | object | [] = null
  totalRows = this.consignments ? this.consignments.length : this.perPage

  getFieldsToShow () {
    const fieldsToShow = []

    fieldsToShow.push(
      { key: 'connoteNumber', label: 'Connote Number' },
      { key: 'dispatchedAt', label: 'Dispatch Date' },
      { key: 'carrier', label: 'Carrier' },
      { key: 'serviceType', label: 'Service Type' },
      { key: 'senderAddress.company', label: 'Sender Company Name' },
      { key: 'deliveryAddress.company', label: 'Receiver Company Name' },
      { key: 'deliveryAddress.suburb.name', label: 'Receiver Suburb' }
    )

    if (this.isMutable) {
      fieldsToShow.unshift({ key: 'delete', label: '' })
      fieldsToShow.push({ key: 'edit', label: 'Edit' })
    }

    return fieldsToShow
  }

  deleteItems () {
    this.$emit('emit-delete-consignment', this.selectedItems)
  }

  @Watch('consignment', { immediate: true, deep: true })
  onChangeConsignment () {
    this.consignments = this.consignment
  }
}
</script>

<style scoped lang="scss" src="./ConsignmentDataTable.scss"></style>
