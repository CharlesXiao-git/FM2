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
    <b-table responsive="true" hover :items="manifests" :fields="fieldsToShow" :current-page="currentPage" :per-page="perPage" :filter="filter">
        <template v-slot:cell(consignments)="data">
            <template v-if="data.value && data.value[0] && data.value[0].selectedOffer &&
            data.value[0].selectedOffer.carrierAccount && data.value[0].selectedOffer.carrierAccount.carrier &&
            data.value[0].selectedOffer.carrierAccount.carrier.displayName"
            >
                {{ data.value[0].selectedOffer.carrierAccount.carrier.displayName }}
            </template>
        </template>
        <template v-slot:cell(createdAt)="data">
            <template v-if="data.value">
                {{ data.value[2] }}/{{ data.value[1] }}/{{ data.value[0] }}
            </template>
        </template>
        <template v-slot:cell(printManifest)>
            <i class="fas fa-paste fa-lg"></i>
        </template>
        <template v-slot:cell(printLabels)>
            <i class="fas fa-print fa-lg"></i>
        </template>
    </b-table>
    <div class="justify-content-center row my-1">
      <b-pagination size="md" :total-rows="totalRows" :per-page="perPage" v-model="currentPage" />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { Manifest } from '@/model/Manifest'

@Component({
})
export default class ManifestTable extends Vue {
  @Prop({ required: true }) manifest: Manifest[]

  fieldsToShow = [
    { key: 'createdAt', label: 'Date Submitted' },
    { key: 'id', label: 'Manifest ID', formatter: 'manifestIdFormatter' },
    { key: 'consignments', label: 'Carrier' },
    { key: 'printManifest', label: 'Print Manifest' },
    { key: 'printLabels', label: 'Print Labels' },
    { key: 'manifestDetails', label: 'ManifestDetails' }
  ]

  options = [
    { text: 10, value: 10 },
    { text: 25, value: 25 },
    { text: 50, value: 50 },
    { text: 100, value: 100 },
    { text: 200, value: 200 }
  ]

  perPage = 10
  manifests = this.manifest
  currentPage = 1
  filter: string | RegExp | object | [] = null
  totalRows = this.manifests ? this.manifests.length : this.perPage

  // This will eventually come from the backend
  manifestIdFormatter (value: string) {
    return 'FMM000000' + value
  }
}
</script>

<style scoped lang="scss" src="./ManifestDataTable.scss"></style>
