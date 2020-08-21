<template>
    <div>
        <b-form-timepicker
            v-model="time"
            :name="name"
            :hide-header="true"
            :placeholder="placeholder"
            :disabled="disabled"
            no-close-button
            minutes-step="15"
            class="timepicker"
            locale="en"
            @input="inputChange" />
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'

@Component
export default class TimePicker extends Vue {
    @Prop({ required: true }) name: string
    @Prop({ default: 'Time' }) private placeholder: string
    @Prop({ default: false }) private disabled: boolean
    time = ''

    inputChange () {
      this.$emit('selected-time', { time: this.time, name: this.name })
    }
}
</script>

<style scoped lang="scss" src="./TimePicker.scss" />
