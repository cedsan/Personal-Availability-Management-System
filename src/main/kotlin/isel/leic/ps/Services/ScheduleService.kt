package isel.leic.ps.Services

import isel.leic.ps.DAL.Interfaces.IScheduleDAL
import isel.leic.ps.DTO.ScheduleDTO
import isel.leic.ps.Domain.Schedule.ScheduleEnum
import isel.leic.ps.Entities.Schedule
import isel.leic.ps.Services.Interfaces.IScheduleService
import org.springframework.stereotype.Service

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the business layer operations for the Visibility resource.
 */
@Service
class ScheduleService: IScheduleService {
    val scheduleDALMap: HashMap<ScheduleEnum, IScheduleDAL>


    constructor(_scheduleDALMap: HashMap<ScheduleEnum, IScheduleDAL>) {
        scheduleDALMap = _scheduleDALMap
    }

    override fun getAllSchedulesOfObservable(id: Int): Collection<Schedule> {
        var schedules = arrayListOf<Schedule>()
        for(scheduleDAL in scheduleDALMap.values) {
            schedules.addAll(scheduleDAL.getAllScheduleOfObservable(id))
        }
        return schedules
    }

    override fun getScheduleById(scheduleId: Int, scheduleType: ScheduleEnum): Schedule {
        return scheduleDALMap[scheduleType]!!.getScheduleById(scheduleId)
    }

    override fun getLastAddedSchedule(observableId: Int, scheduleType: ScheduleEnum): Schedule {
        return scheduleDALMap[scheduleType]!!.getLastScheduleAdded(observableId)
    }

    override fun addObservableSchedule(observableId: Int, scheduleDTO: ScheduleDTO, scheduleType: ScheduleEnum) {
        scheduleDALMap[scheduleType]!!.addSchedule(observableId, scheduleDTO)
    }

    override fun removeAllSchedule(observableId: Int) {
        for(scheduleDAL in scheduleDALMap.values) {
            scheduleDAL.removeAll(observableId)
        }
    }

    override fun removeSchedule(scheduleID: Int, scheduleType: ScheduleEnum) {
        scheduleDALMap[scheduleType]!!.removeScheduleById(scheduleID)
    }

    override fun updateSchedule(id: Int, scheduleID: Int, scheduleDTO: ScheduleDTO, scheduleType: ScheduleEnum) {
        scheduleDALMap[scheduleType]!!.updateSchedule(id, scheduleID, scheduleDTO)
    }
}