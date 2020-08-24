import { ItemType } from '@/model/ItemType'

export class Item {
  id: number
  consignmentId: string
  quantity: number
  length: number
  width: number
  height: number
  weight: number
  totalWeight: number
  volume: number
  isHazardous: boolean
  type: ItemType

  constructor (id: number = null, consignmentId: string = null, quantity: number = null, length: number = null, width: number = null, height: number = null, weight: number = null, totalWeight: number = null, volume: number = null, isHazardous: boolean = null, type: ItemType = null) {
    this.id = id
    this.consignmentId = consignmentId
    this.quantity = quantity
    this.length = length
    this.width = width
    this.height = height
    this.weight = weight
    this.totalWeight = totalWeight
    this.volume = volume
    this.isHazardous = isHazardous
    this.type = type
  }
}
