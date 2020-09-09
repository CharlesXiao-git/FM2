import { Address } from '@/model/Address'
import { Suburb } from '@/model/Suburb'

export function prepareAddressData (address: Address): Address {
  const suburb: Suburb = {
    id: null,
    name: address.suburb.name,
    postcode: address.suburb.postcode,
    state: address.suburb.state
  }
  if (address.suburb.id) {
    suburb.id = address.suburb.id
  }

  const sendAddressData: Address = {
    id: null,
    userClientId: address.userClientId,
    addressType: 'DELIVERY',
    referenceId: address.referenceId,
    company: address.company,
    addressLine1: address.addressLine1,
    addressLine2: address.addressLine2,
    suburb: suburb,
    contactName: address.contactName,
    phoneNumber: address.phoneNumber,
    email: address.email,
    specialInstructions: address.specialInstructions
  }
  if (address.id) {
    sendAddressData.id = address.id
  }
  if (address.addressType) {
    sendAddressData.addressType = address.addressType
  }

  return sendAddressData
}
