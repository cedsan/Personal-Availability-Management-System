package isel.leic.ps.Entities

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class is the representation of the the generalization Entity for the various
 * schedules.
 */
open class Schedule(val scheduleType: String, val availabilityLocationLatitude: Double?, val availabilityLocationLongitude: Double?) {
}