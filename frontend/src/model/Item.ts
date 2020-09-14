import { DangerousGood } from '@/model/DangerousGood'

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
  itemTypeId: number
  dangerousGoods: DangerousGood []

  constructor (id: number = null, consignmentId: string = null, quantity: number = null, length: number = null, width: number = null, height: number = null, weight: number = null, totalWeight: number = null, volume: number = null, isHazardous: boolean = null, itemTypeId: number = null, dangerousGoods: DangerousGood [] = null) {
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
    this.itemTypeId = itemTypeId
    this.dangerousGoods = dangerousGoods
  }
}
