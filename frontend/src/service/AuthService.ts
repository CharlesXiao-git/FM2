import { User } from '@/model/User'

/**
 * Set token to local storage
 * @param token
 */
export function setToken (token: string) {
  localStorage.setItem('user-token', token)
}

/**
 * Get token from local storage
 * @return token | null
 */
export function getToken () {
  return localStorage.getItem('user-token') || null
}

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
 * Remove token from local storage
 */
export function removeToken () {
  localStorage.removeItem('user-token')
}

/**
 * Decode the token : base64 decode and splitting
 * @param token
 */
export function decodeToken (token: string) {
  try {
    return JSON.parse(atob(token.split('.')[1]))
  } catch (e) {
    console.log(e)
  }
}

/**
 * Extracting user details from the token
 * @param token
 */
export function extractUserFromToken (token: string): User {
  const parsedToken = decodeToken(token)
  const user = new User()
  if (parsedToken !== null) {
    if (parsedToken.sub !== undefined) {
      user.username = parsedToken.sub
    }
    if (parsedToken.userRole !== undefined) {
      user.role = parsedToken.userRole
    }
    if (parsedToken.email !== undefined) {
      user.email = parsedToken.email
    }
  }
  return user
}

/**
 * Validating if the token is valid
 */
export function isTokenValid () {
  const token = getToken()
  if (token) {
    const parsedToken = decodeToken(token)
    if (Date.now() < parsedToken.exp * 1000) {
      return true
    }
  }
  return false
}

export function getDefaultConfig () {
  return {
    headers: getAuthenticatedToken()
  }
}
