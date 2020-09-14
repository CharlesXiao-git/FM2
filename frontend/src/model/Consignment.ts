import { Offer } from '@/model/Offer'
import { Item } from '@/model/Item'

export class Consignment {
  id: string = null
  userClientId: number = null
  connoteNumber: string = null
  dispatchedAt: Date = null
  deliveryWindowBegin: Date = null
  deliveryWindowEnd: Date = null
  deliveryAddressClass: string = null
  authorityToLeave: boolean = null
  isTailgateRequired: boolean = null
  senderAddressId: number = null
  deliveryAddressId: number = null
  items: Item[]
  status: string
  selectedOffer: Offer

  constructor (id: string = null, userClientId: number = null, connoteNumber: string = null, dispatchedAt: Date = null, deliveryWindowBegin: Date = null,
    deliveryWindowEnd: Date = null, deliveryAddressClass: string = null, authorityToLeave: boolean = null, isTailgateRequired: boolean = null,
    senderAddressId: number = null, deliveryAddressId: number = null, items: Item[] = null, status: string = null,
    selectedOffer: Offer = null) {
    this.id = id
    this.userClientId = userClientId
    this.connoteNumber = connoteNumber
    this.dispatchedAt = dispatchedAt
    this.deliveryWindowBegin = deliveryWindowBegin
    this.deliveryWindowEnd = deliveryWindowEnd
    this.deliveryAddressClass = deliveryAddressClass
    this.authorityToLeave = authorityToLeave
    this.isTailgateRequired = isTailgateRequired
    this.senderAddressId = senderAddressId
    this.deliveryAddressId = deliveryAddressId
    this.items = items
    this.status = status
    this.selectedOffer = selectedOffer
  }
}
