import { CarrierAccount } from '@/model/CarrierAccount'

export class Offer {
  id: string
  selected: boolean
  carrierAccount: CarrierAccount

  constructor (id: string, selected: boolean, carrierAccount: CarrierAccount) {
    this.id = id
    this.selected = selected
    this.carrierAccount = carrierAccount
  }
}
