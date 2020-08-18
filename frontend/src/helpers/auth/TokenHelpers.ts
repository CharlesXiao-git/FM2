import { getToken } from '@/helpers/auth/StorageHelpers'

export type DecodedToken = {
  userId: string;
  sub: string;
  email: string;
  preferredUnit: string;
  userRole: string;
  exp: number;
  iss: string;
}

/**
 * Decode the token : base64 decode and splitting
 */
export function decodeToken () {
  const token = getToken()
  if (token) {
    try {
      return JSON.parse(atob(token.split('.')[1]))
    } catch (e) {
      console.log(e)
    }
  }
  return null
}

/**
 * Determine if token is not expired
 */
export function isTokenValid (): boolean {
  const parsedToken = decodeToken()
  if (parsedToken && Date.now() < parsedToken.exp * 1000) {
    return true
  }
  return false
}
