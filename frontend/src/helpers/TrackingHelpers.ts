import { Consignment } from '@/model/Consignment'
import { Offer } from '@/model/Offer'

/**
 * Extract external tracking url from a consignment data structure
 * @param consignment: Consignment
 */
export function getExternalTrackingUrl (consignment: Consignment): string | null {
  const connotePlaceholder = '%CONNOTE_NUMBER%'
  let trackingUrl = null

  if (consignment.connoteNumber && consignment.selectedOffer && consignment.selectedOffer.carrierAccount && consignment.selectedOffer.carrierAccount.carrier) {
    const carrierTrackingUrl: string | null = consignment.selectedOffer.carrierAccount.carrier.trackingUrl

    if (carrierTrackingUrl && carrierTrackingUrl.includes(connotePlaceholder)) {
      trackingUrl = carrierTrackingUrl.replace(connotePlaceholder, consignment.connoteNumber)
    }
  }

  return trackingUrl
}

/**
 * Determine type of tracking and extract tracking link from a consignment data structure
 * @param consignment: Consignment
 */
export function getTrackingUrl (consignment: Consignment): string | null {
  if (consignment.status) {
    return null // TODO: Internal tracking
  } else {
    return getExternalTrackingUrl(consignment)
  }
}
