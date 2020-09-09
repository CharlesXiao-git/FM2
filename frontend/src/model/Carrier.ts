export class Carrier {
  id: string
  name: string
  trackingUrl: string

  constructor (id: string, name: string, trackingUrl: string) {
    this.id = id
    this.name = name
    this.trackingUrl = trackingUrl
  }
}
