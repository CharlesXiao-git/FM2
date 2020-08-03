export class User {
  username: string
  role: string
  email: string

  constructor (username = '') {
    this.username = username
  }
}
