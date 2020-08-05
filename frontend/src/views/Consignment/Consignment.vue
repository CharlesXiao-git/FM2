<template>
    <div class="consignment col-11 m-auto p-0">
        <div class="new-consignment mt-5">
            <div class="new-consignment-header pt-2 pb-1 pl-4">
                <h3>NEW CONSIGNMENT</h3>
            </div>
            <AddressSelect></AddressSelect>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import AddressSelect from '@/components/AddressSelect/AddressSelect.vue'

@Component({
  components: { AddressSelect }
})
export default class Consignment extends Vue {

}
</script>

<style scoped lang="scss" src="./Consignment.scss">
</style>
