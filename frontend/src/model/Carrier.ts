export class Carrier {
  id: string
  name: string
  trackingUrl: string
  displayName: string

  constructor (id: string, name: string, trackingUrl: string, displayName: string) {
    this.id = id
    this.name = name
    this.trackingUrl = trackingUrl
    this.displayName = displayName
  }
}
