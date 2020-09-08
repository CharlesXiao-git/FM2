<template>
    <div>
        <b-form-select :disabled="disabledSelect" v-model="selectedClient" @change="emitSelectedClient">
            <b-form-select-option :value="null">Choose Account</b-form-select-option>
            <b-form-select-option v-for="client in clientOptions" :value="client" v-bind:key="client.id">
                {{ client.name }}
            </b-form-select-option>
        </b-form-select>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator'
import { getDefaultConfig } from '@/helpers/auth/RequestHelpers'
import { ClientReference } from '@/helpers/types'

@Component
export default class ClientSelect extends Vue {
  @Prop() client: ClientReference
  disabledSelect = false
  selectedClient: ClientReference = null
  clientOptions: object[] = null

  emitSelectedClient () {
    this.$emit('selected-client', this.selectedClient)
  }

  created () {
    if (this.client) {
      this.selectedClient = this.client
      this.disabledSelect = true
    }

    this.$axios.get('/api/v1/user/children', getDefaultConfig())
      .then(response => {
        this.clientOptions = Object.keys(response.data).sort()
          .map(key => ({ id: Number(response.data[key]), name: key }))
      }, error => {
        this.$log.error(error.response.data)
      })
  }

  @Watch('client', { immediate: true, deep: true })
  clientChanged () {
    if (this.client) {
      this.selectedClient = this.client
      this.disabledSelect = true
    } else {
      this.selectedClient = null
      this.disabledSelect = false
    }
  }
}
</script>
