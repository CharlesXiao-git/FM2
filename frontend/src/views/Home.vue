<template>
    <div class="home">
        <Header :username="username"/>
        <Sidebar :role="role"/>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import Header from '@/components/Header/Header.vue'
import Sidebar from '@/components/Sidebar/Sidebar.vue'

    @Component({
      components: {
        Header,
        Sidebar
      }
    })
export default class Home extends Vue {
    @Prop()
    username!: string

    @Prop()
    role!: string
}
</script>

<style>
    .home {
        background-color: white;
        height: 100%;
    }
</style>
