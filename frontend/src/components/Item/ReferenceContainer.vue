<template>
  <div class="row">
    <div class="col-12 mt-4">
      <div class="row">
        <b-button class="secondary-button ml-2" v-on:click="newReferenceForm" :disabled="disableAdd">
          <i class="fas mr-2 fa-plus" />Add Reference
        </b-button>
      </div>

      <div class="row">
        <div v-for="reference in references" :key="reference.id" class="col-12 p-0 mt-2">
          <ReferenceForm
            :reference="reference"
            @change-reference="handleExistingReference"
            @delete-reference="handleDeletedReference"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import ReferenceForm from '@/components/Item/ReferenceForm.vue'
import Reference from '@/helpers/types/Reference'

@Component({
  components: { ReferenceForm }
})
export default class ReferenceContainer extends Vue {
  references: Array<Reference> = []
  disableAdd = false

  newReferenceForm () {
    const reference: Reference = { id: this.getNextId(), value: '' }
    this.references.push(reference)
    this.disableAdd = true
  }

  getNextId () {
    const count = this.references.length
    if (count === 0) return 1

    const lastId: number = this.references[count - 1].id
    return lastId + 1
  }

  handleExistingReference (reference: Reference) {
    const existingReference: Reference = this.references.filter(ref => ref.id === reference.id)[0]

    if (existingReference) {
      existingReference.value = reference.value
      this.disableAdd = false
      this.updatedReferences()
    } else {
      console.error('Reference id not found')
    }
  }

  handleDeletedReference (reference: Reference) {
    const otherReferences: Array<Reference> = this.references.filter(ref => ref.id !== reference.id)
    this.references = otherReferences
    this.disableAdd = false
    this.updatedReferences()
  }

  updatedReferences () {
    this.$emit('updated-references', this.references)
  }
}
</script>
