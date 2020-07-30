<template>
  <div class="data-table">
    <div class="row data-table-content">
      <div class="col-lg">
        <b-form-group class="left" label="Show entries" label-cols="4" label-cols-md="3" label-for="pageSelect">
          <b-form-select :options="options" v-model="perPage" id="pageSelect"></b-form-select>
        </b-form-group>
      </div>
      <div class="col-lg">
        <b-form-group class="right" label="Search:" label-cols-sm="4" label-for="filterInput">
          <b-form-input v-model="filter" type="search" id="filterInput" placeholder="Type to Search"></b-form-input>
        </b-form-group>
      </div>
    </div>
    <b-table responsive="true" hover :items="addresses" :fields="fieldsToShow" :current-page="currentPage" :per-page="perPage" :filter="filter">
      <template v-slot:cell(edit)>
        <slot name="edit" />
      </template>
    </b-table>
    <div class="justify-content-center row my-1">
      <b-pagination size="md" :total-rows="totalRows" :per-page="perPage" v-model="currentPage" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class AddressTable extends Vue {
  @Prop({ required: true }) address: Array<object>
  @Prop({ required: true }) fieldsToShow: Array<object>
  @Prop({ required: true }) options: Array<object>
  @Prop({ required: true, default: 10 }) perPage: number

  addresses = this.address
  currentPage = 1
  filter: string | RegExp | object | [] = null
  totalRows = this.addresses ? this.addresses.length : this.perPage
}
</script>

<style scoped lang="scss" src="./DataTable.scss"></style>
