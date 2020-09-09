import { Offer } from '@/model/Offer'
import { Address } from '@/model/Address'
import { Item } from '@/model/Item'

export class Consignment {
  id: string = null
  connoteNumber: string = null
  dispatchedAt: Date = null
  deliveryWindowBegin: Date = null
  deliveryWindowEnd: Date = null
  deliveryAddressClass: string = null
  authorityToLeave: boolean = null
  isTailgateRequired: boolean = null
  ownerId: number = null
  senderAddress: Address
  deliveryAddress: Address
  items: Item[]
  status: string
  selectedOffer: Offer

  constructor (id: string = null, connoteNumber: string = null, dispatchedAt: Date = null, deliveryWindowBegin: Date | null,
    deliveryWindowEnd: Date | null, deliveryAddressClass: string, authorityToLeave: boolean, isTailgateRequired: boolean,
    ownerId: number, senderAddress: Address, deliveryAddress: Address, items: Item[], status: string,
    selectedOffer: Offer) {
    this.id = id
    this.connoteNumber = connoteNumber
    this.dispatchedAt = dispatchedAt
    this.deliveryWindowBegin = deliveryWindowBegin
    this.deliveryWindowEnd = deliveryWindowEnd
    this.deliveryAddressClass = deliveryAddressClass
    this.authorityToLeave = authorityToLeave
    this.isTailgateRequired = isTailgateRequired
    this.ownerId = ownerId
    this.senderAddress = senderAddress
    this.deliveryAddress = deliveryAddress
    this.items = items
    this.status = status
    this.selectedOffer = selectedOffer
  }
}
