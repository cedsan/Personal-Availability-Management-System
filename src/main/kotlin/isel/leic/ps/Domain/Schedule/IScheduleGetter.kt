package isel.leic.ps.Domain.Schedule

import isel.leic.ps.DTO.ScheduleDTO

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines the contract to parse a Schedule from a String in
 * order to get the specific schedule representation according
 * to the type
 */
interface IScheduleGetter {

    fun getSchedule(body: String): ScheduleDTO
}