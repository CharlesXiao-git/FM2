export class Consignment {
  id: string
  clientId: string
  senderAddressId: string
  deliveryAddressId: string
  connoteId: string | null
  dispatchDateAt: Date
  deliveryWindowStartAt: Date | null
  deliveryWindowEndAt: Date | null
  addressClass: string
  isAllowedToLeave: boolean
  isTailgateRequired: boolean

  constructor (id: string, clientId: string, senderAddressId: string, deliveryAddressId: string, connoteId: string | null, dispatchDateAt: Date, deliveryWindowStartAt: Date | null, deliveryWindowEndAt: Date | null, addressClass: string, isAllowedToLeave: boolean, isTailgateRequired: boolean) {
    this.id = id
    this.clientId = clientId
    this.senderAddressId = senderAddressId
    this.deliveryAddressId = deliveryAddressId
    this.connoteId = connoteId
    this.dispatchDateAt = dispatchDateAt
    this.deliveryWindowStartAt = deliveryWindowStartAt
    this.deliveryWindowEndAt = deliveryWindowEndAt
    this.addressClass = addressClass
    this.isAllowedToLeave = isAllowedToLeave
    this.isTailgateRequired = isTailgateRequired
  }
}
