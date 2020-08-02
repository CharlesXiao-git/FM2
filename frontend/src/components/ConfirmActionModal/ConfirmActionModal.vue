<template>
  <div>
    <b-modal :id="modalId" hide-header centered ok-variant="success" static @ok="confirmed">
      <slot name="content"></slot>
      <template v-slot:modal-footer="{ ok, cancel }">
        <b-button :class="buttonClass" type="submit" @click="ok()">{{ buttonName }}</b-button>
        <b-button class="secondary-button" @click="cancel()">Close</b-button>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class ConfirmActionModal extends Vue {
  @Prop({ required: true }) modalId: string
  @Prop({ default: 'OK' }) buttonName: string
  @Prop({ default: 'primary-button' }) buttonClass: string

  confirmed () {
    this.$emit('confirmed', 'ok')
  }
}

</script>
