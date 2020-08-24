/**
 * Check for a valid string
 * @param field
 */
export function validateStringField (field: string) {
  return field !== null && field.length > 1
}

/**
 * Validating email against the universal email regex
 * @param field
 */
export function validateEmailField (field: string) {
  const regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return regex.test(field)
}
