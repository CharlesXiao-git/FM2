<template>
    <div>
        <div v-for="item in items" :key="item.id">
            <ItemForm :item="item" :item-types="itemTypes" @delete-item="deleteItem" @emitted-item="emittedItem"></ItemForm>
        </div>
        <b-button class="primary-button m-3" v-on:click="calculate">Calculate</b-button>
        <b-button class="secondary-button ml-2" v-on:click="addItem" :disabled="disableAdd"><i class="fas mr-2 fa-plus" />Add Item</b-button>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import ItemForm from '@/components/Item/ItemForm.vue'
import { Item } from '@/model/Item'
import { getAuthenticatedToken } from '@/helpers/auth/RequestHelpers'
import { ItemType } from '@/model/ItemType'

@Component({
  components: { ItemForm }
})
export default class ItemPanel extends Vue {
  id = 0
  items: Item[] = [new Item(this.id)]
  disableAdd = false
  itemTypes: ItemType[] = null

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

  calculate () {
    this.$emit('calculate', true)
  }

  created () {
    const config = {
      headers: getAuthenticatedToken()
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
