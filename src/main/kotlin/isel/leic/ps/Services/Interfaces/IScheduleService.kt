package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.ScheduleDTO
import isel.leic.ps.Domain.Schedule.ScheduleEnum
import isel.leic.ps.Entities.Schedule


interface IScheduleService {
    fun getAllSchedulesOfObservable(id: Int): Collection<Schedule>
    fun getScheduleById(scheduleId: Int, scheduleType: ScheduleEnum): Schedule
    fun addObservableSchedule(observableId: Int, scheduleDTO: ScheduleDTO, scheduleType: ScheduleEnum)
    fun getLastAddedSchedule(observableId: Int, scheduleType: ScheduleEnum): Schedule
    fun removeAllSchedule(observableId: Int)
    fun removeSchedule(scheduleID: Int, scheduleType: ScheduleEnum)
    fun updateSchedule(id: Int, scheduleID: Int, scheduleDTO: ScheduleDTO, scheduleType: ScheduleEnum)
}