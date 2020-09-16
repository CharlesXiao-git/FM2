<template>
  <div class="manifests row">
    <div class="manifests-header">
      <div class="d-flex col-md-11 col-12 ml-xl-auto ml-2 mr-auto py-4">
        <div class="img-container">
          <i class="fas fa-box"></i>
        </div>
        <div class="pl-4">
          <h5>Manifests Page</h5>
          <p>List of all your manifests</p>
        </div>
      </div>
    </div>
    <div class="manifests-content col-md-11 col-12 ml-xl-auto ml-2 mr-auto mt-4">
      <div class="row">
        <template v-if="loading">Loading...</template>
        <template v-else>
          <template v-if="noManifests">{{ noManifestsErrorMessage }}</template>
          <template v-else-if="errorFetching">{{ errorFetchingMessage }}</template>
          <template v-else>
            <ManifestDataTable :manifest="manifests"></ManifestDataTable>
          </template>
        </template>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { Manifest } from '@/model/Manifest'
import { getDefaultConfig } from '@/helpers/auth/RequestHelpers'
import ManifestDataTable from '@/components/ManifestDataTable/ManifestDataTable.vue'
import Alert from '@/components/Alert/Alert.vue'

@Component({
  components: { ManifestDataTable, Alert }
})

export default class ManifestList extends Vue {
  manifests: Manifest[] = []
  errorFetching = false
  errorFetchingMessage = 'Error while fetching manifests'
  noManifests = false
  noManifestsErrorMessage = 'No manifests found'
  loading = true

  created () {
    this.$axios.get('/api/v1/manifest', getDefaultConfig())
      .then(response => {
        if (response.data && response.data.manifests) {
          this.manifests = response.data.manifests
          this.manifests.reverse()
        }
        this.loading = false
        this.noManifests = this.manifests.length === 0
      }, error => {
        this.loading = false
        this.errorFetching = true
        this.$log.error(error.response)
      })
  }
}
</script>

<style scoped lang="scss" src="./ManifestList.scss"></style>
