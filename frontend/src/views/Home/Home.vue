<template>
    <div class="home">
        <Header />
        <Sidebar />
        <div class="content">
            <slot />
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import Header from '@/components/Header/Header.vue'
import Sidebar from '@/components/Sidebar/Sidebar.vue'

    @Component({
      components: {
        Header,
        Sidebar
      }
    })
export default class Home extends Vue {}
</script>

<style lang="scss" src="./Home.scss"></style>
