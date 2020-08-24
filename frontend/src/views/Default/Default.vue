<template>
    <div class="default">
        <slot />
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class Default extends Vue {}
</script>

<style lang="scss" src="./Default.scss"></style>
