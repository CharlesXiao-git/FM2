import { decodeToken } from '@/helpers/auth/StorageHelpers'
import { User } from '@/model/User'

export type DecodedToken = {
  userId: string;
  sub: string;
  email: string;
  preferredUnit: string;
  userRole: string;
  exp: number;
  iss: string;
}

const parsedToken: DecodedToken = decodeToken()
const user = new User()

export function currentUser (): User {
  if (parsedToken !== null) {
    user.id = parsedToken.userId
    user.username = parsedToken.sub
    user.email = parsedToken.email
    user.role = parsedToken.userRole
    user.preferredUnit = parsedToken.preferredUnit
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

export function isUserSessionValid (): boolean {
  const parsedToken: DecodedToken = decodeToken()
  if (parsedToken && Date.now() < parsedToken.exp * 1000) {
    return true
  }
  return false
}
