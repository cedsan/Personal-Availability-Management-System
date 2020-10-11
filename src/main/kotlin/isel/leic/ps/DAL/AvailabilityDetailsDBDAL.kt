package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IAvailabityDetailsDAL
import isel.leic.ps.DAL.SqlQueries.AvailabilityDetailsQueries
import isel.leic.ps.Entities.AvailabilityDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Types

@Repository
open class AvailabilityDetailsDBDAL: IAvailabityDetailsDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))//todo verify if this should be true or false
    }

    override fun setUnavailable(observableId: Int) {
        jdbcTemplate = getJdbcTemplate()
        val nrRowsAffected = jdbcTemplate.update(AvailabilityDetailsQueries.SET_OBSERVABLE_UNAVAILABLE, arrayOf(observableId), intArrayOf(Types.INTEGER))
        if (nrRowsAffected == 0) throw IllegalArgumentException()
    }

    override fun setAvailable(observableId: Int) {
        jdbcTemplate = getJdbcTemplate()
        val nrRowsAffected = jdbcTemplate.update(AvailabilityDetailsQueries.SET_OBSERVABLE_AVAILABLE, arrayOf(observableId), intArrayOf(Types.INTEGER))
        if (nrRowsAffected == 0) throw IllegalArgumentException()
    }

    override fun getAvailability(observableId: Int): AvailabilityDetail {
        jdbcTemplate = getJdbcTemplate()
        val availabilityDetails = jdbcTemplate.queryForObject(AvailabilityDetailsQueries.SELECT_AVAILABILITY_OF_OBSERVABLE, arrayOf(observableId), AvailabilityDetailRowMapper())
        return availabilityDetails
    }

    class AvailabilityDetailRowMapper : RowMapper<AvailabilityDetail> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): AvailabilityDetail {
            return AvailabilityDetail(rs!!.getBoolean(1))
        }
    }
}