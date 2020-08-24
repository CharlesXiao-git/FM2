export class User {
  id: string
  username: string
  role: string
  email: string
  preferredUnit: string

  constructor (id: string, username: string, role: string, email: string, preferredUnit: string) {
    this.id = id
    this.username = username
    this.role = role
    this.email = email
    this.preferredUnit = preferredUnit
  }
}
