package isel.leic.ps.DAL

import isel.leic.ps.ControlAccess.Role
import isel.leic.ps.DAL.Interfaces.IUserRoleDAL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Repository
import java.sql.ResultSet

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines implementations of the operations access UserRole
 * entites from the used DataSource
 */
@Repository
open class UserRoleDAL: IUserRoleDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate : JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun getUserRoles(user_id: String): Collection<Role> {
        jdbcTemplate = getJdbcTemplate()
        var roles = jdbcTemplate.query("SELECT * FROM User_Role WHERE user_id = (SELECT id FROM Observable WHERE email = ?) AND observer_or_observable = 1;", arrayOf(user_id), UserRoleRowMapper())
        if(roles.size == 0)
            roles = jdbcTemplate.query("SELECT * FROM User_Role WHERE user_id = (SELECT id FROM Observer WHERE email = ?) AND observer_or_observable = 0;", arrayOf(user_id), UserRoleRowMapper())
        return roles
    }

    class UserRoleRowMapper: RowMapper<Role> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): Role {
            return Role.valueOf(rs!!.getString(2))
        }
    }

}