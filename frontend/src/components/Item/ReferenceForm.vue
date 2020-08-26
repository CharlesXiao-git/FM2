<template>
  <div class="row p-0 mt-2 pl-2">
    <div class="col-md-11 col-lg-6 col-xl-4 p-0">
      <b-input v-model="reference.value" placeholder="Reference number" @change="changeReference"></b-input>
    </div>

    <div class="col-1 p-0">
      <b-button class="delete-btn" v-on:click="deleteReference"><i class="fas fa-trash-alt fa-lg" /></b-button>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator'
import Reference from '@/helpers/types/Reference'

@Component
export default class ReferenceForm extends Vue {
  @Prop() private reference: Reference

  changeReference () {
    this.$emit('change-reference', this.reference)
  }

  deleteReference () {
    this.$emit('delete-reference', this.reference)
  }
}
</script>
