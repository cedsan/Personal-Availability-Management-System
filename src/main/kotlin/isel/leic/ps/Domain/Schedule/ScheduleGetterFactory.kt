package isel.leic.ps.Domain.Schedule

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Factory that returns mappers to schedule specific entities.
 */
class ScheduleGetterFactory {

    companion object {
        var scheduleGetterMap : HashMap<ScheduleEnum, IScheduleGetter?> = HashMap()

        init {
            scheduleGetterMap.put(ScheduleEnum.WEEKDAY, WeekdayScheduleGetter())
        }

        /**
         * Factory method that according to the type of
         * the Subscription passed, returns the instance to create it
         */
        fun getScheduleGetter(type: ScheduleEnum): IScheduleGetter{
            return scheduleGetterMap[type]!!
        }
    }
}