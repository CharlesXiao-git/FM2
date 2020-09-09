<template>
    <div class="p-4">
        <div class="form-control-box">
            <div class="row py-3">
                <div class="col-6 col-sm-4 col-lg-3 col-xl-1 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Quantity</h5>
                    <b-form-input type="number" :min="1" v-model="quantity" :disabled="disabledDimensions" @change="calculateAndValidate"></b-form-input>
                </div>
                <div class="col-6 col-sm-4 col-lg-3 col-xl-3 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Item type</h5>
                    <b-form-select v-model="selectedItemType" :options="getItemTypesOption()" @change="getDimensions"></b-form-select>
                </div>
                <div class="col-6 col-sm-4 col-lg-3 col-xl-1 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Length</h5>
                    <b-input-group :append="preferredUnit">
                        <b-form-input type="number" :min="0" v-model="length" :disabled="disabledDimensions" @change="calculateAndValidate"></b-form-input>
                    </b-input-group>
                </div>
                <div class="col-6 col-sm-4 col-lg-3 col-xl-1 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Width</h5>
                    <b-input-group :append="preferredUnit">
                        <b-form-input type="number" :min="0" v-model="width" :disabled="disabledDimensions" @change="calculateAndValidate"></b-form-input>
                    </b-input-group>
                </div>
                <div class="col-6 col-sm-4 col-lg-3 col-xl-1 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Height</h5>
                    <b-input-group :append="preferredUnit">
                        <b-form-input type="number" :min="0" v-model="height" :disabled="disabledDimensions" @change="calculateAndValidate"></b-form-input>
                    </b-input-group>
                </div>
                <div class="col-6 col-sm-4 col-lg-3 col-xl-1 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Weight</h5>
                    <b-input-group append="kg">
                        <b-form-input type="number" :min="0" v-model="weight" @change="calculateAndValidate"></b-form-input>
                    </b-input-group>
                </div>
                <div class="col-6 col-sm-4 col-lg-3 col-xl-2 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Total Weight</h5>
                    <b-input-group append="kg">
                        <b-form-input :disabled="true" type="number" v-model="totalWeight"></b-form-input>
                    </b-input-group>
                </div>
                <div class="col-6 col-sm-4 col-lg-3 col-xl-2 m-auto">
                    <h5 class="mt-4 mt-lg-3 mt-xl-0">Volume</h5>
                    <b-input-group>
                        <template v-slot:append>
                            <b-input-group-text>{{ preferredUnit }}<sup>3</sup></b-input-group-text>
                        </template>
                        <b-form-input :disabled="true" type="number" v-model="volume"></b-form-input>
                    </b-input-group>
                </div>
                <div class="col-6 col-sm-4 col-lg-6 col-xl-1">
                    <h5 class="mt-4 mt-lg-3 mb-2">Hazardous</h5>
                    <b-form-checkbox v-model="isHazardous"></b-form-checkbox>
                </div>
                <div class="col-6 col-sm-4 col-lg-6 col-xl-1 mt-4">
                    <b-button class="item-delete" v-on:click="deleteItem" ><i class="fas fa-trash-alt fa-lg mt-2" /></b-button>
                </div>
            </div>
            <template v-if="errorDisplayMessages.length > 0">
                <div class="error" v-for="error in errorDisplayMessages" v-bind:key="error">
                    {{ error }}
                </div>
            </template>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator'
import { userPreferredUnit } from '@/helpers/auth/UserHelpers'
import { convertFromPreferredUnits, convertToPreferredUnits } from '@/helpers/PreferredUnitHelpers'
import { ItemType } from '@/model/ItemType'
import { Item } from '@/model/Item'

type itemTypeOption = {
  value: ItemType;
  text: string;
}

@Component
export default class ItemForm extends Vue {
  @Prop() item: Item
  @Prop() itemTypes: ItemType[]
  quantity = 1
  selectedItemType: ItemType = null
  length: number = null
  width: number = null
  height: number = null
  weight: number = null
  totalWeight: number = null
  volume: number = null
  isHazardous = false
  preferredUnit = userPreferredUnit()
  disabledDimensions = false
  errorDisplayMessages: string[] = []
  hardLimit = {
    length: 10,
    width: 2.5,
    height: 2.4,
    weight: 100000
  }

  errorMessages = {
    quantity: 'Invalid Dimension: Quantity must be greater than 0',
    length: 'Invalid Dimension: The length must be between 0 and ' + convertToPreferredUnits(this.hardLimit.length) + this.preferredUnit.toLowerCase(),
    width: 'Invalid Dimension: The width must be between 0 and ' + convertToPreferredUnits(this.hardLimit.width) + this.preferredUnit.toLowerCase(),
    height: 'Invalid Dimension: The height must be between 0 and ' + convertToPreferredUnits(this.hardLimit.height) + this.preferredUnit.toLowerCase(),
    weightMutable: 'Invalid Dimension: The weight must be between 0 and ' + this.hardLimit.weight + 'kg',
    weightImmutable: ''
  }

  calculateAndValidate () {
    this.validateLength()
    this.validateWeight()
    this.validateWidth()
    this.validateHeight()
    this.validateQuantity()
    if (!this.validateValuesArePresent()) return
    if (this.errorDisplayMessages.length !== 0) return
    this.calculateTotalWeight()
    this.calculateVolume()
    this.emitItem()
  }

  calculateTotalWeight () {
    this.totalWeight = this.quantity * this.weight
  }

  calculateVolume () {
    this.volume = Math.round(this.length * this.width * this.height * this.quantity * 1000) / 1000
  }

  validateValuesArePresent () {
    if (this.length && this.width && this.height && this.weight && this.quantity) {
      return true
    }
  }

  validateLength () {
    // Early exit : if the length into entered
    if (!this.length) {
      return false
    }

    // Clear any existing error related to length
    this.clearError(this.errorMessages.length)

    // Validate that the non mutable dimension for the selected Item Type are not tampered and the mutable ones are within the hard limit
    if (this.selectedItemType && !this.selectedItemType.isMutable && convertFromPreferredUnits(this.length) === this.selectedItemType.itemTemplate.length) {
      return true
    } else if (this.length > 0 && convertFromPreferredUnits(this.length) <= this.hardLimit.length) {
      return true
    }
    this.errorDisplayMessages.push(this.errorMessages.length)
    return false
  }

  validateWidth () {
    if (!this.width) {
      return false
    }

    this.clearError(this.errorMessages.width)
    if (this.selectedItemType && !this.selectedItemType.isMutable && convertFromPreferredUnits(this.width) === this.selectedItemType.itemTemplate.width) {
      return true
    } else if (this.width > 0 && convertFromPreferredUnits(this.width) <= this.hardLimit.width) {
      return true
    }
    this.errorDisplayMessages.push(this.errorMessages.width)
    return false
  }

  validateQuantity () {
    if (!this.quantity) {
      return false
    }
    this.clearError(this.errorMessages.quantity)
    if (this.selectedItemType && !this.selectedItemType.isMutable && convertFromPreferredUnits(this.quantity) === this.selectedItemType.itemTemplate.quantity) {
      return true
    } else if (this.quantity > 0) {
      return true
    }
    this.errorDisplayMessages.push(this.errorMessages.quantity)
    return false
  }

  validateHeight () {
    if (!this.height) {
      return false
    }
    this.clearError(this.errorMessages.height)
    if (this.selectedItemType && !this.selectedItemType.isMutable && convertFromPreferredUnits(this.height) === this.selectedItemType.itemTemplate.height) {
      return true
    } else if (this.height > 0 && convertFromPreferredUnits(this.height) <= this.hardLimit.height) {
      return true
    }
    this.errorDisplayMessages.push(this.errorMessages.height)
    return false
  }

  validateWeight () {
    if (!this.weight) {
      return false
    }

    this.clearError(this.errorMessages.weightMutable)
    if (this.selectedItemType && !this.selectedItemType.isMutable) {
      this.errorMessages.weightImmutable = 'Invalid Dimensions: The weight must be between 0 and ' + this.selectedItemType.itemTemplate.weight + 'kg for a ' + this.selectedItemType.type
      this.clearError(this.errorMessages.weightImmutable)
      if (this.weight > 0 && this.weight <= this.selectedItemType.itemTemplate.weight) {
        return true
      } else {
        this.errorDisplayMessages.push(this.errorMessages.weightImmutable)
        return false
      }
    } else if (this.weight > 0 && this.weight <= this.hardLimit.weight) {
      return true
    }
    this.errorDisplayMessages.push(this.errorMessages.weightMutable)
    return false
  }

  deleteItem () {
    this.$emit('delete-item', this.item)
  }

  getItemTypesOption () {
    const option: itemTypeOption[] = [{ value: null, text: 'Please select an item type' }]
    if (this.itemTypes) {
      this.itemTypes.forEach(function (itemType: ItemType) {
        option.push({ value: itemType, text: itemType.type })
      })
    }
    return option
  }

  getDimensions () {
    if (this.selectedItemType && this.selectedItemType.itemTemplate && !this.selectedItemType.isMutable) {
      this.errorDisplayMessages = []
      this.length = convertToPreferredUnits(this.selectedItemType.itemTemplate.length)
      this.width = convertToPreferredUnits(this.selectedItemType.itemTemplate.width)
      this.height = convertToPreferredUnits(this.selectedItemType.itemTemplate.height)
      this.quantity = this.selectedItemType.itemTemplate.quantity
      this.weight = this.selectedItemType.itemTemplate.weight
      this.disabledDimensions = true
      this.calculateAndValidate()
    } else {
      this.disabledDimensions = false
    }
  }

  emitItem () {
    if (this.validateLength() && this.validateWidth() && this.validateHeight() && this.validateWeight() && this.validateQuantity() && this.selectedItemType) {
      if (!this.selectedItemType.isMutable) {
        this.$emit('emitted-item', this.getItem(), this.item, true)
      } else {
        this.$emit('emitted-item', this.getItem(), this.item)
      }
    }
  }

  getItem () {
    return new Item(
      this.item.id,
      null,
      this.quantity,
      convertFromPreferredUnits(this.length),
      convertFromPreferredUnits(this.width),
      convertFromPreferredUnits(this.height),
      this.weight,
      this.totalWeight,
      convertFromPreferredUnits(this.volume),
      this.isHazardous,
      this.selectedItemType
    )
  }

  clearError (error: string) {
    const errorIndex = this.errorDisplayMessages.indexOf(error)
    if (errorIndex !== -1) {
      this.errorDisplayMessages.splice(errorIndex, 1)
    }
  }
}
</script>

<style scoped lang="scss" src="./ItemForm.scss"></style>
