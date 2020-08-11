import { User } from '@/model/User'
import { decodeToken } from '@/helpers/auth/StorageHelpers'

export type DecodedToken = {
  userId: string;
  sub: string;
  email: string;
  preferredUnit: string;
  userRole: string;
  exp: number;
  iss: string;
}

export type DecodedTokenStringValue = 'userId' | 'sub' | 'email' | 'preferredUnit' | 'userRole' | 'iss'

function getTokenValue (value: DecodedTokenStringValue) {
  const parsedToken: DecodedToken = decodeToken()
  return (parsedToken !== null && parsedToken[value] !== undefined) ? parsedToken[value] : undefined
}

export function userId () {
  return getTokenValue('userId')
}

export function userName () {
  return getTokenValue('sub')
}

export function userEmail () {
  return getTokenValue('email')
}

export function userRole () {
  return getTokenValue('userRole')
}

export function userIsBroker () {
  return userRole().toUpperCase() === 'BROKER'
}

export function userIsCustomer () {
  return userRole().toUpperCase() === 'CUSTOMER'
}

export function userIsClient () {
  return userRole().toUpperCase() === 'CLIENT'
}

export function userIsAdmin () {
  return userRole().toUpperCase() === 'ADMIN'
}

export function userPreferredUnit () {
  return getTokenValue('preferredUnit')
}

export function currentUser () {
  const parsedToken: DecodedToken = decodeToken()
  const user = new User()

  if (parsedToken !== null) {
    user.id = parsedToken.userId
    user.username = parsedToken.sub
    user.email = parsedToken.email
    user.role = parsedToken.userRole
    user.preferredUnit = parsedToken.preferredUnit
  }

  return user
}

export function userSessionValid () {
  const parsedToken: DecodedToken = decodeToken()
  if (parsedToken && Date.now() < parsedToken.exp * 1000) {
    return true
  }
  return false
}
