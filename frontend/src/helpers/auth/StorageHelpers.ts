/**
 * Get token from local storage
 * @return token | null
 */
export function getToken () {
  return localStorage.getItem('user-token') || null
}

/**
 * Set token to local storage
 * @param token
 */
export function setToken (token: string) {
  localStorage.setItem('user-token', token)
}

/**
 * Remove token from local storage
 */
export function removeToken () {
  localStorage.removeItem('user-token')
}
