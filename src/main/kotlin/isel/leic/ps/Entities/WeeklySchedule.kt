package isel.leic.ps.Entities

import isel.leic.ps.Domain.Schedule.ScheduleEnum

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the model representation  for the WeeklyScheule entity.
 */
class WeeklySchedule(val id: Int, val dayOfWeek: Int, val startAvailabilityHour: Int, val startAvailabilityMinute: Int, val endAvailabilityHour: Int, val endAvailabilityMinute: Int, val isLocationValid: Boolean, availabilityLocationLatitude: Double?, availabilityLocationLongitude: Double?): Schedule(ScheduleEnum.WEEKDAY.label, availabilityLocationLatitude, availabilityLocationLongitude) {
}