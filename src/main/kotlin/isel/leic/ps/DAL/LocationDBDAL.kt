package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.ILocationDAL
import isel.leic.ps.DAL.SqlQueries.LocationQueries
import isel.leic.ps.DTO.LocationDTO
import isel.leic.ps.Entities.Location
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Types

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines implementations of the operations access Location
 * entites from the used DataSource
 */
@Repository
open class LocationDBDAL: ILocationDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun updateObservableLocation(observableId: Int, location: LocationDTO) {
        jdbcTemplate = getJdbcTemplate()
        val nrRowsAffected = jdbcTemplate.update(LocationQueries.UPDATE_LOCATION, arrayOf(location.latitude, location.longitude, observableId), intArrayOf(Types.DOUBLE, Types.DOUBLE, Types.INTEGER))
        if (nrRowsAffected == 0) throw IllegalArgumentException()
    }

    override fun getObservableLastLocation(observableId: Int): Location {
        jdbcTemplate = getJdbcTemplate()
        val location = jdbcTemplate.queryForObject(LocationQueries.SELECT_LOCATION_OF_OBSERVABLE, arrayOf(observableId), LocationRowMapper())
        return location
    }

    class LocationRowMapper : RowMapper<Location> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): Location {
            return Location(rs!!.getInt(1),
                            rs.getDouble(2),
                            rs.getDouble(3),
                            rs.getBoolean(4),
                            rs.getTimestamp(5))
        }
    }
}