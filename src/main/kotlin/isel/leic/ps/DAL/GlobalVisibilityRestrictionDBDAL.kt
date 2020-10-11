package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IGlobalVisibilityRestrictionDAL
import isel.leic.ps.DAL.SqlQueries.GlobalVisibilityRestrictionQueries
import isel.leic.ps.DTO.GlobalRestrictionVisibilityUpdateDTO
import isel.leic.ps.Entities.GlobalVisibilityRestrictionConfig
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

@Repository
open class GlobalVisibilityRestrictionDBDAL: IGlobalVisibilityRestrictionDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))//todo verify if this should be true or false
    }

    override fun getGlobalVisibilityRestriction(observableID: Int): GlobalVisibilityRestrictionConfig {
        jdbcTemplate = getJdbcTemplate()
        var globalVisiRestriction: GlobalVisibilityRestrictionConfig
        try {
            globalVisiRestriction = jdbcTemplate.queryForObject<GlobalVisibilityRestrictionConfig>(GlobalVisibilityRestrictionQueries.SELECT_GLOBAL_RESTRICTION_OF_OBSERVABLE, arrayOf(observableID), GlobalVisibilityRestrictionRowMapper())
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("No GlobalVisibilityRestriction entity with specified observable_id")
        }
        return globalVisiRestriction
    }

    override fun updateGlobalVisibilityRestriction(observableID: Int, updatedGlobalRestriction: GlobalRestrictionVisibilityUpdateDTO) {
        jdbcTemplate = getJdbcTemplate()
        val arrayOfTypes: IntArray = intArrayOf(Types.BOOLEAN, Types.BOOLEAN, Types.BOOLEAN, Types.INTEGER)
        val arrayOfPreparedStatementArgs = arrayOf(updatedGlobalRestriction.canSeeWhenAvailable, updatedGlobalRestriction.canSeeSchedule, updatedGlobalRestriction.canSeeLocation, observableID)
        val rowsAffected = jdbcTemplate.update(GlobalVisibilityRestrictionQueries.UPDATE_GLOBAL_VISIBILITY_RESTRICTION_OF_OBSERVABLE_ON_OBSERVER,
                arrayOfPreparedStatementArgs,
                arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    class GlobalVisibilityRestrictionRowMapper: RowMapper<GlobalVisibilityRestrictionConfig> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): GlobalVisibilityRestrictionConfig {
            return GlobalVisibilityRestrictionConfig(rs!!.getInt(1),
                    rs.getBoolean(2),
                    rs.getBoolean(3),
                    rs.getBoolean(4))
        }
    }

}