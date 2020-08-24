import { getToken } from '@/helpers/auth/StorageHelpers'

/**
 * The client must send this token in the Authorization header when making requests to protected resources (Standard practice):
 * Authorization: Bearer <token>
 */
export function getAuthenticatedToken () {
  if (getToken()) {
    return {
      Authorization: 'Bearer ' + getToken()
    }
  }
}

/**
 * Default config for requests to the backend
 */
export function getDefaultConfig () {
  return {
    headers: getAuthenticatedToken()
  }
}
