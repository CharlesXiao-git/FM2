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

export function userId (): string | undefined {
  return getTokenValue('userId')
}

export function userName (): string | undefined {
  return getTokenValue('sub')
}

export function userEmail (): string | undefined {
  return getTokenValue('email')
}

export function userRole (): string | undefined {
  return getTokenValue('userRole')
}

export function userPreferredUnit (): string | undefined {
  return getTokenValue('preferredUnit')
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
