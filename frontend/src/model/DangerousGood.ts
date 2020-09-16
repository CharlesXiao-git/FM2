export class DangerousGood {
  id: number
  itemId: number
  unNumber: number
  hazardClass: string
  subRisk: string
  typeOfPackaging: string
  weight: number
  shippingName: string
  commonName: string

  constructor (id: number = null, singleItemId: number = null, unNumber: number = null, classNum: string = null, subRisk: string = null, typeOfPackaging: string = null, weight: number = null, shippingName: string = null, commonName: string = null) {
    this.id = id
    this.itemId = singleItemId
    this.unNumber = unNumber
    this.hazardClass = classNum
    this.subRisk = subRisk
    this.typeOfPackaging = typeOfPackaging
    this.weight = weight
    this.shippingName = shippingName
    this.commonName = commonName
  }
}
