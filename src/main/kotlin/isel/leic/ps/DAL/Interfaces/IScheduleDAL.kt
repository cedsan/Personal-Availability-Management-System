package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.ScheduleDTO
import isel.leic.ps.Entities.Schedule

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access Schedule entities
 * from a DataSource
 */
interface IScheduleDAL {
    fun getAllScheduleOfObservable(observableId: Int): Collection<Schedule>
    fun getScheduleById(scheduleId: Int): Schedule
    fun getLastScheduleAdded(observableId: Int): Schedule
    fun addSchedule(observableId: Int, scheduleDTO: ScheduleDTO)
    fun removeAll(observableId: Int)
    fun removeScheduleById(scheduleID: Int)
    fun updateSchedule(observableId: Int, scheduleID: Int, scheduleDTO: ScheduleDTO)
}