/**
 * Apply time to base date
 * @param baseDate: Date
 * @param time: string in format 'HH:mm:ss'
 */
export function applyTimeToDate (baseDate: Date, time: string) {
  const timeParts = time.split(':')
  return new Date(
    baseDate.getFullYear(),
    baseDate.getMonth(),
    baseDate.getDate(),
    parseInt(timeParts[0]),
    parseInt(timeParts[1]),
    parseInt(timeParts[2])
  )
}
