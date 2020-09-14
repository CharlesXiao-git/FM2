import { Suburb } from '@/model/Suburb'

export class Address {
  addressType: string
  id: number
  referenceId: string
  company: string
  addressLine1: string
  addressLine2: string
  suburb: Suburb
  contactName: string
  phoneNumber: string
  email: string
  specialInstructions: string
  userClientId: number = null

  constructor (id: number, referenceId: string, company: string, addressLine1: string, addressLine2: string, suburb: Suburb, contactName: string, phoneNumber: string, email: string, specialInstructions: string, userClientId: number = null, addressType: string) {
    this.id = id
    this.referenceId = referenceId
    this.company = company
    this.addressLine1 = addressLine1
    this.addressLine2 = addressLine2
    this.suburb = suburb
    this.contactName = contactName
    this.phoneNumber = phoneNumber
    this.email = email
    this.specialInstructions = specialInstructions
    this.userClientId = userClientId
    this.addressType = addressType
  }
}
