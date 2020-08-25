export class Address {
  addressType: string
  id: string
  referenceId: string
  companyName: string
  addressLine1: string
  addressLine2: string
  town: string
  postcode: number
  state: string
  contactName: string
  contactNo: string
  contactEmail: string
  notes: string // Special Instructions
  clientId: number = null

  constructor (id: string, referenceId: string, companyName: string, addressLine1: string, addressLine2: string, town: string, postcode: number, state: string, contactName: string, contactNo: string, contactEmail: string, notes: string, clientId: number = null, addressType: string) {
    this.id = id
    this.referenceId = referenceId
    this.companyName = companyName
    this.addressLine1 = addressLine1
    this.addressLine2 = addressLine2
    this.town = town
    this.postcode = postcode
    this.state = state
    this.contactName = contactName
    this.contactNo = contactNo
    this.contactEmail = contactEmail
    this.notes = notes
    this.clientId = clientId
    this.addressType = addressType
  }
}
