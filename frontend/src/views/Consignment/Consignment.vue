<template>
    <div class="consignment col-11 m-auto p-0">
        <div class="consignment-content">
            <div class="consignment-sub-heading mt-5 pt-2 pb-1 pl-4">
                <h3>NEW CONSIGNMENT</h3>
            </div>
            <AddressSelect></AddressSelect>
        </div>
        <div class="consignment-content">
            <div class="consignment-sub-heading mt-5 pt-2 pb-1 pl-4">
                <h3>ITEM INFO</h3>
            </div>
            <div v-for="item in items" :key="item.id">
                <ItemPanel :item="item" :item-types="itemTypes" @delete-item="deleteItem" @emitted-item="emittedItem"></ItemPanel>
            </div>
            <b-button class="primary-button m-3">Calculate</b-button>
            <b-button class="secondary-button ml-2" v-on:click="addItem" :disabled="disableAdd"><i class="fas mr-2 fa-plus" />Add Item</b-button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import AddressSelect from '@/components/AddressSelect/AddressSelect.vue'
import ItemPanel from '@/components/ItemPanel/ItemPanel.vue'
import { Item } from '@/model/Item'
import { getAuthenticatedToken } from '@/helpers/auth/RequestHelpers'

@Component({
  components: { ItemPanel, AddressSelect }
})
export default class Consignment extends Vue {
  id = 0
  items: Item[] = [new Item(this.id)]
  disableAdd = false
  itemTypes: [] = null

  addItem () {
    if (this.items.length !== 0) {
      this.id = this.items[this.items.length - 1].id
    }
    this.items.push(new Item(this.id + 1))
  }

  deleteItem (item: Item) {
    this.items.splice(this.items.indexOf(item), 1)
    if (this.items.length === 0) {
      this.disableAdd = false
    }
    this.$forceUpdate()
  }

  emittedItem (item: Item, oldItem: Item, deleteTheItem = false) {
    this.disableAdd = false
    if (this.items.indexOf(oldItem) !== -1) {
      this.items[this.items.indexOf(oldItem)] = item
    }

    if (deleteTheItem) {
      if (this.items.length > 1) {
        this.items.splice(this.items.indexOf(item), 1)
      } else {
        this.disableAdd = true
      }
    }
    this.$forceUpdate()
  }

  created () {
    const config = {
      headers: getAuthenticatedToken(),
      params: {
        isCustom: false
      }
    }

    this.$axios.get('/api/v1/consignment/itemTypes', config)
      .then(response => {
        this.itemTypes = response.data
      }, error => {
        this.$log.error(error.response.data)
      })
  }
}
</script>

<style scoped lang="scss" src="./Consignment.scss">
</style>
