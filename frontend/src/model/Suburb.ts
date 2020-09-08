export class Suburb {
  id: number
  name: string
  postcode: number
  state: string

  constructor (id: number, name: string, postcode: number, state: string) {
    this.id = id
    this.name = name
    this.postcode = postcode
    this.state = state
  }
}
