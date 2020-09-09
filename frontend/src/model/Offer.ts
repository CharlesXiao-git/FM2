import { CarrierAccount } from '@/model/CarrierAccount'

export class Offer {
  id: number
  carrierAccount: CarrierAccount
  category1Fees: number
  category2Fees: number
  eta: number
  freightCost: number
  fuelSurcharge: number
  gst: number
  totalCost: number
  selected: boolean

  constructor (id: number, selected: boolean, carrierAccount: CarrierAccount) {
    this.id = id
    this.selected = selected
    this.carrierAccount = carrierAccount
  }
}
