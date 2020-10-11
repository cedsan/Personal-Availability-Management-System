package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IVisibilityRestrictionDAL
import isel.leic.ps.DAL.SqlQueries.VisibilityRestrictionQueries
import isel.leic.ps.DTO.RestrictionVisibilityDTO
import isel.leic.ps.Entities.VisibilityRestriction
import isel.leic.ps.Exception.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
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
 * Defines implementations of the operations access VisibilityRestriction
 * entites from the used DataSource
 */
@Repository
open class VisibilityRestrictionDBDAL: IVisibilityRestrictionDAL {
    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun updateVisibilityRestriction(observableID: Int, observerID: Int, updatedRestriction: RestrictionVisibilityDTO?) {
        jdbcTemplate = getJdbcTemplate()
        val arrayOfTypes: IntArray = intArrayOf(Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN, Types.INTEGER, Types.INTEGER)
        val arrayOfPreparedStatementArgs = arrayOf(updatedRestriction!!.canSeeWhenAvailable, updatedRestriction.canSeeSchedule, updatedRestriction.canSeeLocation, observerID, observableID)
        val rowsAffected = jdbcTemplate.update(VisibilityRestrictionQueries.UPDATE_VISIBILITY_RESTRICTION_OF_OBSERVABLE_ON_OBSERVER,
                arrayOfPreparedStatementArgs,
                arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    override fun getVisibilityRestrictionOfObserverOnObservable(observerID: Int, observableID: Int): VisibilityRestriction {
        jdbcTemplate = getJdbcTemplate()
        var visibRestriction: VisibilityRestriction
        try {
            visibRestriction = jdbcTemplate.queryForObject<VisibilityRestriction>(VisibilityRestrictionQueries.SELECT_VISIBILITY_RESTRICTION_OF_OBSERVABLE_ON_OBSERVER, arrayOf(observableID, observerID), VisibilityRestrictionRowMapper())
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("No Observer entity found with query for specified id")
        }

        return visibRestriction
    }

    class VisibilityRestrictionRowMapper: RowMapper<VisibilityRestriction> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): VisibilityRestriction {
            return VisibilityRestriction(rs!!.getInt(1),
                                         rs.getBoolean(2),
                                         rs.getBoolean(3),
                                         rs.getBoolean(4))
        }
    }
}