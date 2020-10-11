package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IVisibilityDAL
import isel.leic.ps.DAL.SqlQueries.VisibilityQueries
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Repository
import java.sql.Types

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines implementations of the operations access Visibility
 * entites from the used DataSource
 */
@Repository
open class VisibilityDBDAL: IVisibilityDAL {
    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun createVisibilityAssociation(observerID: Int, observableID: Int) {
        jdbcTemplate = getJdbcTemplate()
        var arrayOfArgs = arrayOf(observerID, observableID)
        var arrayOfArgTypes  = intArrayOf(Types.INTEGER, Types.INTEGER)
        var rowsAffected = jdbcTemplate.update(VisibilityQueries.INSERT_VISIBILITY_ASSOC, arrayOfArgs, arrayOfArgTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    override fun deleteVisibilityAssociation(observerID: Int, observableID: Int) {
        jdbcTemplate = getJdbcTemplate()
        jdbcTemplate.update(VisibilityQueries.DELETE_VISIBILITY_ASSOC, arrayOf(observerID, observableID), intArrayOf(Types.INTEGER, Types.INTEGER))
    }

    override fun creatingPendingVisibilityAssocRequest(observerID: Int, observableID: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hasAssocBetween(observerID: Int, observableID: Int): Boolean {
        jdbcTemplate = getJdbcTemplate()
        var nrRows: Int = jdbcTemplate.queryForObject(VisibilityQueries.SELECT_1_IF_EXISTS_ASSOC_BETWEEN_OBSERVER_OBSERVABLE, arrayOf(observerID, observableID), Int::class.java)
        return nrRows > 0
    }
}