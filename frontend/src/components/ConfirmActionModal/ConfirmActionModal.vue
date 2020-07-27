<template>
  <div>
    <div v-b-modal.confirm-action-modal>
      <slot name="trigger"></slot>
    </div>

    <b-modal id="confirm-action-modal" @ok="confirmed" hide-header centered ok-variant="success" static>
      <slot name="content"></slot>
    </b-modal>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class ConfirmActionModal extends Vue {
  @Prop({ required: true }) confirmed!: Function
}

</script>
