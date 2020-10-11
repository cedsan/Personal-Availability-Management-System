package isel.leic.ps.Domain.Schedule

import com.fasterxml.jackson.databind.ObjectMapper
import isel.leic.ps.DTO.ScheduleDTO
import isel.leic.ps.DTO.WeeklyScheduleDTO

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines an implementation that obtains a specific type of schedule
 * instance of the type WeekdaySchedule.
 */
class WeekdayScheduleGetter: IScheduleGetter {
    override fun getSchedule(body: String): ScheduleDTO {
        return ObjectMapper().readValue(body, WeeklyScheduleDTO::class.java)
    }
}