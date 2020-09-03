import { Address } from '@/model/Address'

export function prepareAddressData (address: Address): Address {
  const sendAddressData: Address = {
    id: null,
    clientId: address.clientId,
    addressType: 'DELIVERY',
    referenceId: address.referenceId,
    companyName: address.companyName,
    addressLine1: address.addressLine1,
    addressLine2: address.addressLine2,
    town: address.town,
    postcode: address.postcode,
    state: address.state,
    contactName: address.contactName,
    contactNo: address.contactNo,
    contactEmail: address.contactEmail,
    notes: address.notes
  }
  if (address.id) {
    sendAddressData.id = address.id
  }
  if (address.addressType) {
    sendAddressData.addressType = address.addressType
  }

  return sendAddressData
}