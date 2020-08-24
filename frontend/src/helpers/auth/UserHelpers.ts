import { User } from '@/model/User'
import { getToken } from '@/helpers/auth/StorageHelpers'
import { DecodedToken, decodeToken } from '@/helpers/auth/TokenHelpers'

let token: string = null
let parsedToken: DecodedToken = null
let user: User = null

function createNewUser (): void {
  token = getToken() // Get new value
  parsedToken = decodeToken() // Decode new value
  if (parsedToken) {
    user = new User(parsedToken.userId, parsedToken.sub, parsedToken.userRole, parsedToken.email, parsedToken.preferredUnit) // Set new user details
  }
}

export function currentUser (): User {
  if (!parsedToken || !user || getToken() !== token) {
    createNewUser()
  }

  return user
}

export function userId (): string | undefined {
  return currentUser().id
}

export function userName (): string | undefined {
  return currentUser().username
}

export function userEmail (): string | undefined {
  return currentUser().email
}

export function userRole (): string | undefined {
  return currentUser().role
}

export function userPreferredUnit (): string | undefined {
  return currentUser().preferredUnit
}

export function isUserBroker (): boolean {
  return userRole() !== undefined && userRole().toUpperCase() === 'BROKER'
}

export function isUserCustomer (): boolean {
  return userRole() !== undefined && userRole().toUpperCase() === 'CUSTOMER'
}

export function isUserClient (): boolean {
  return userRole() !== undefined && userRole().toUpperCase() === 'CLIENT'
}

export function isUserAdmin (): boolean {
  return userRole() !== undefined && userRole().toUpperCase() === 'ADMIN'
}
