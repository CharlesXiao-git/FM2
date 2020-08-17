import { userPreferredUnit } from '@/helpers/auth/UserHelpers'

export function convertFromPreferredUnits (dimension: number, preferredUnit: string = userPreferredUnit()) {
  if (preferredUnit === 'MM') {
    return dimension / 1000
  } else if (preferredUnit === 'CM') {
    return dimension / 100
  } else if (preferredUnit === 'M') {
    return dimension
  }
}

export function convertToPreferredUnits (dimension: number, preferredUnit: string = userPreferredUnit()) {
  if (preferredUnit === 'MM') {
    return dimension * 1000
  } else if (preferredUnit === 'CM') {
    return dimension * 100
  } else if (preferredUnit === 'M') {
    return dimension
  }
}
