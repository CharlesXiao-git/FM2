<template>
  <div class="consignments row">
    <div class="consignments-alert">
      <Alert v-if="displayAlert" :variant="variant" :text="alertMessage" autoDismissInterval="5" />
    </div>
    <div class="consignments-header">
      <div class="d-flex col-md-11 col-12 ml-xl-auto ml-2 mr-auto py-4">
        <div class="img-container">
          <i class="fas fa-box"></i>
        </div>
        <div class="pl-4">
          <h5>Consignments Page</h5>
          <p>List of all your consignments</p>
        </div>
      </div>
    </div>
    <div class="consignments-content col-md-11 col-12 ml-xl-auto ml-2 mr-auto mt-4">
        <b-form-group>
          <b-form-radio-group
              v-model="selected"
              :options="options"
              buttons
              name="radios-btn-default"
          ></b-form-radio-group>
        </b-form-group>
      <div class="row">
        <template v-if="loading">Loading...</template>
        <template v-else>
          <template v-if="noConsignments">{{ noConsignmentsErrorMessage }}</template>
          <template v-else-if="errorFetching">{{ errorFetchingMessage }}</template>
          <template v-else>
            <ConsignmentDataTable :consignment="consignments"  :is-mutable="isMutable" @emit-delete-consignment="emittedDeleteConsignment"></ConsignmentDataTable>
          </template>
        </template>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import { Consignment } from '@/model/Consignment'
import { getAuthenticatedToken } from '@/helpers/auth/RequestHelpers'
import ConsignmentDataTable from '@/components/ConsignmentDataTable/ConsignmentDataTable.vue'
import Alert from '@/components/Alert/Alert.vue'

@Component({
  components: { ConsignmentDataTable, Alert }
})

export default class ConsignmentList extends Vue {
  displayAlert = false
  variant: string = null
  alertMessage: string = null
  consignments: Consignment[] = []
  errorFetching = false
  errorFetchingMessage = 'Error while fetching consignments'
  noConsignments = false
  noConsignmentsErrorMessage = 'No consignments found'
  selected = 'allConsignments'
  options = [
    { text: 'All Consignments', value: 'allConsignments' },
    { text: 'Open Consignments', value: 'openConsignments' }
  ]

  loading = true
  isMutable = true

  emittedDeleteConsignment (consignments: Consignment[]) {
    this.resetFlag()
    this.$axios.delete('/api/v1/consignment', this.prepareConfig(consignments))
      .then(response => {
        this.$log.info(response.data)
        consignments.forEach(consignment => {
          this.consignments.splice(this.consignments.indexOf(consignment), 1)
        })
        this.setAlert('success', 'Consignment(s) deleted successfully')
      }, error => {
        this.$log.warn(error)
        this.setAlert('danger', 'Error deleting consignment(s)')
      })
  }

  config = {
    headers: getAuthenticatedToken()
  }

  getAllConsignments () {
    this.resetFlag()
    this.$axios.get('/api/v1/consignment', this.config)
      .then(response => {
        this.consignments = response.data.consignments
        this.consignments.reverse()
        this.loading = false
        this.noConsignments = response.data.consignments.length === 0
        this.isMutable = false
      }, error => {
        this.loading = false
        this.errorFetching = true
        this.$log.error(error.response.data)
      })
  }

  getOpenConsignments () {
    // TODO Needs to pass the variable telling it to get OPEN consignments only
    this.consignments = []
    this.$forceUpdate()
    this.noConsignments = true
    this.isMutable = true
  }

  setAlert (variant: string, alertMessage: string) {
    this.displayAlert = true
    this.variant = variant
    this.alertMessage = alertMessage
  }

  resetFlag () {
    this.displayAlert = false
  }

  prepareConfig (consignments: Consignment[]) {
    const params = new URLSearchParams()

    consignments.forEach(consignment => {
      params.append('ids', consignment.id)
    })

    return {
      headers: getAuthenticatedToken(),
      params: params
    }
  }

  @Watch('selected', { immediate: true, deep: true })
  onChangeSelection () {
    if (this.selected === 'allConsignments') {
      this.getAllConsignments()
    } else {
      this.getOpenConsignments()
    }
  }
}
</script>

<style scoped lang="scss" src="./ConsignmentList.scss"></style>
