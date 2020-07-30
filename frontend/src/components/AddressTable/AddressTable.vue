<template>
  <div class="address-table">
    <DataTable :address="addresses" :fields-to-show="fieldsToShow" :options="options" :per-page="perPage">
      <template v-slot:edit>
        <b-button class="edit-button"><i class="fas fa-edit"></i></b-button>
      </template>
    </DataTable>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import DataTable from '@/components/DataTable/DataTable.vue'
import { Address } from '@/model/Address'

@Component({
  components: { DataTable }
})
export default class AddressTable extends Vue {
  @Prop({ required: true }) address: Address[]

  addresses = this.address
  fieldsToShow = [
    { key: 'referenceId', label: 'Address Reference' },
    { key: 'companyName', label: 'Company Name' },
    { key: 'contactName', label: 'Contact Name' },
    { key: 'contactNo', label: 'Contact Number' },
    { key: 'contactEmail', label: 'Contact Email' },
    { key: 'addressLine1', label: 'Address Line 1' },
    { key: 'addressLine2', label: 'Address Line 2' },
    { key: 'town', label: 'Town' },
    { key: 'postcode', label: 'Postcode' },
    { key: 'state', label: 'State' },
    { key: 'notes', label: 'Special Instructions' },
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
}
</script>

<style scoped lang="scss" src="./AddressTable.scss"></style>
