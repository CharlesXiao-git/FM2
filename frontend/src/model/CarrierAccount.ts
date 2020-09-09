import { Carrier } from '@/model/Carrier'

export class CarrierAccount {
  id: string
  carrier: Carrier

  constructor (id: string, carrier: Carrier) {
    this.id = id
    this.carrier = carrier
  }
}
