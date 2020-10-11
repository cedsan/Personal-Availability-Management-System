package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IScheduleDAL
import isel.leic.ps.DAL.SqlQueries.WeeklyScheduleQueries
import isel.leic.ps.DTO.ScheduleDTO
import isel.leic.ps.DTO.WeeklyScheduleDTO
import isel.leic.ps.Entities.Schedule
import isel.leic.ps.Entities.WeeklySchedule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Repository
import java.lang.reflect.Type
import java.sql.ResultSet
import java.sql.Types

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines implementations of the operations access Schedule
 * entites from the used DataSource
 */
@Repository
open class WeeklyScheduleDBDAL: IScheduleDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun getAllScheduleOfObservable(observableId: Int): Collection<Schedule> {
        jdbcTemplate = getJdbcTemplate()
        val collectionSchedule = jdbcTemplate.query(WeeklyScheduleQueries.SELECT_ALL_WEEKDAY_SCHEDULES_OF_OBSERVABLE, arrayOf(observableId), WeeklyScheduleRowMapper())
        return collectionSchedule
    }

    override fun getScheduleById(scheduleId: Int): Schedule {
        jdbcTemplate = getJdbcTemplate()
        return jdbcTemplate.queryForObject(WeeklyScheduleQueries.SELECT_WEEKDAY_SCHEDULE_BY_ID, arrayOf(scheduleId), WeeklyScheduleRowMapper())
    }

    override fun getLastScheduleAdded(observableId: Int): Schedule {
        jdbcTemplate = getJdbcTemplate()
        return jdbcTemplate.queryForObject(WeeklyScheduleQueries.SELECT_LAST_WEEKDAY_SCHEDULE_OF_OBSERVABLE, arrayOf(observableId), WeeklyScheduleRowMapper())
    }

    override fun addSchedule(observableId: Int, scheduleDTO: ScheduleDTO) {
        scheduleDTO as WeeklyScheduleDTO
        jdbcTemplate = getJdbcTemplate()
        // Location not specified for schedule
        val scheduleHasLocation = (scheduleDTO.availabilityLocationLatitude == null || scheduleDTO.availabilityLocationLongitude == null)
        val arrayOfArgs = arrayOf(scheduleDTO.dayOfWeek, scheduleDTO.startAvailabilityHour,
                scheduleDTO.startAvailabilityMinute, scheduleDTO.endAvailabilityHour, scheduleDTO.endAvailabilityMinute, scheduleDTO.availabilityLocationLatitude, scheduleDTO.availabilityLocationLongitude, scheduleHasLocation, observableId)
        val arrayOfTypes: IntArray = intArrayOf(Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.DOUBLE, Types.DOUBLE, Types.BOOLEAN, Types.INTEGER)
        val rowsAffected = jdbcTemplate.update(WeeklyScheduleQueries.INSERT_WEEKDAY_SCHEDULE, arrayOfArgs, arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    override fun removeAll(observableId: Int) {
        jdbcTemplate = getJdbcTemplate()
        jdbcTemplate.update(WeeklyScheduleQueries.DELETE_ALL_WEEKLY_SCHEDULES_OF_OBSERVABLE, arrayOf(observableId), intArrayOf(Types.INTEGER))
    }

    override fun removeScheduleById(scheduleID: Int) {
        jdbcTemplate = getJdbcTemplate()
        jdbcTemplate.update(WeeklyScheduleQueries.DELETE_SCHEDULE_BY_ID, arrayOf(scheduleID), intArrayOf(Types.INTEGER) )
    }

    override fun updateSchedule(observableId: Int, scheduleID: Int, scheduleDTO: ScheduleDTO) {
        scheduleDTO as WeeklyScheduleDTO
        jdbcTemplate = getJdbcTemplate()
        val scheduleHasLocation = (scheduleDTO.availabilityLocationLatitude == null || scheduleDTO.availabilityLocationLongitude == null)
        val arrayOfTypes: IntArray = intArrayOf(Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.DOUBLE, Types.DOUBLE,Types.BOOLEAN, Types.INTEGER)
        val arrayOfArgs = arrayOf(scheduleDTO.dayOfWeek, scheduleDTO.startAvailabilityHour, scheduleDTO.startAvailabilityMinute, scheduleDTO.endAvailabilityHour, scheduleDTO.endAvailabilityMinute, scheduleDTO.availabilityLocationLatitude, scheduleDTO.availabilityLocationLongitude, scheduleHasLocation, scheduleID)
        val rowsAffected = jdbcTemplate.update(WeeklyScheduleQueries.UPDATE_WEEKLY_SCHEDULE_BY_ID, arrayOfArgs, arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    class WeeklyScheduleRowMapper: RowMapper<WeeklySchedule> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): WeeklySchedule {
            return WeeklySchedule(rs!!.getInt(1),
                    rs.getInt(2),
                    rs.getInt(3),
                    rs.getInt(4),
                    rs.getInt(5),
                    rs.getInt(6),
                    rs.getBoolean(9),
                    rs.getDouble(7),
                    rs.getDouble(8)
                    )
        }
    }
}