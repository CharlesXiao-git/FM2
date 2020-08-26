export class Consignment {
  id: string
  fromCompanyName: string
  toCompanyName: string
  connoteId: string | null
  dispatchDateAt: Date
  deliveryWindowStartAt: Date | null
  deliveryWindowEndAt: Date | null
  addressClass: string
  isAllowedToLeave: boolean
  isTailgateRequired: boolean
  carrier: string
  serviceType: string
  receiverSuburb: string
  senderSuburb: string

  constructor (id: string, fromCompanyName: string, toCompanyName: string, carrier: string, serviceType: string, receiverSuburb: string,
               senderSuburb: string, connoteId: string | null, dispatchDateAt: Date, deliveryWindowStartAt: Date | null,
               deliveryWindowEndAt: Date | null, addressClass: string, isAllowedToLeave: boolean, isTailgateRequired: boolean) {
    this.id = id
    this.fromCompanyName = fromCompanyName
    this.toCompanyName = toCompanyName
    this.connoteId = connoteId
    this.dispatchDateAt = dispatchDateAt
    this.deliveryWindowStartAt = deliveryWindowStartAt
    this.deliveryWindowEndAt = deliveryWindowEndAt
    this.addressClass = addressClass
    this.isAllowedToLeave = isAllowedToLeave
    this.isTailgateRequired = isTailgateRequired
    this.carrier = carrier
    this.serviceType = serviceType
    this.receiverSuburb = receiverSuburb
    this.senderSuburb = senderSuburb
  }
}
