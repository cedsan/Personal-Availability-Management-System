package isel.leic.ps.Register.DAL

import isel.leic.ps.Register.DataSource.IdProviderUserDbDataSourceTransactionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.sql.Types

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies an implementation to create a new user to the MitreID Connect data source.
 */
class MitreIdUserCreater: IIDProviderUserCreater {

    private var dataSourceTransactionManager : IdProviderUserDbDataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : IdProviderUserDbDataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSourceTransManager.dataSource.connection, false))//todo verify if this should be true or false
    }


    override fun createUser(username: String, password: String, name: String) {
        insertUser(username, password)
        insertUserUserRoleAuthorityAssoc(username)
        insertUserInfo(username, password)
    }

    private fun insertUser(username: String, password: String) {
        jdbcTemplate = getJdbcTemplate()
        val arrayOfArgs: Array<Any> = arrayOf<Any>(username, password)
        val arrayOfTypes: IntArray = intArrayOf(Types.VARCHAR, Types.VARCHAR)
        val rowsAffected = jdbcTemplate.update(MitreIdUserSqlQueries.INSERT_USER, arrayOfArgs, arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    private fun insertUserUserRoleAuthorityAssoc(username: String) {
        jdbcTemplate = getJdbcTemplate()
        val arrayOfArgs: Array<Any> = arrayOf<Any>(username)
        val arrayOfTypes: IntArray = intArrayOf(Types.VARCHAR)
        val rowsAffected = jdbcTemplate.update(MitreIdUserSqlQueries.INSERT_USER_AUTHORITY, arrayOfArgs, arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    private fun insertUserInfo(username: String, name: String) {
        jdbcTemplate = getJdbcTemplate()
        val arrayOfArgs: Array<Any> = arrayOf<Any>(username, name, username)
        val arrayOfTypes: IntArray = intArrayOf(Types.VARCHAR, Types.VARCHAR, Types.VARCHAR)
        val rowsAffected = jdbcTemplate.update(MitreIdUserSqlQueries.INSERT_USER_INFO, arrayOfArgs, arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }
}