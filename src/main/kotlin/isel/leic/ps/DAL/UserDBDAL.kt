package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IUserDAL
import isel.leic.ps.DTO.UserDTO
import isel.leic.ps.DTO.UserInfoDto
import isel.leic.ps.Domain.User.UserType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.ResultSetExtractor
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Repository

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines implementations of the operations access User
 * entites from the used DataSource
 */
@Repository
open class UserDBDAL @Autowired constructor(val dataSourceTransactionManager: DataSourceTransactionManager) : IUserDAL {

    lateinit private var jdbcTemplate : JdbcTemplate

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun getUserType(userMail: String): UserInfoDto? {
        jdbcTemplate = getJdbcTemplate()
        var userId: Int? = null
        try {
            userId = jdbcTemplate.queryForObject("SELECT id FROM Observable WHERE  email = ?;", arrayOf(userMail), Int::class.java)
            if(userId != null) {
                return UserInfoDto(UserType.OBSERVABLE.toString().toLowerCase(), userId)
            }
        } catch (e: IncorrectResultSizeDataAccessException) { }

        try {
            userId = jdbcTemplate.queryForObject("SELECT id FROM Observer WHERE  email = ?;", arrayOf(userMail), Int::class.java)
            if (userId != null) {
                return UserInfoDto(UserType.OBSERVER.toString().toLowerCase(), userId)
            }
        } catch (e: IncorrectResultSizeDataAccessException) { }

        return null
    }

    override fun hasUserWithEmail(userMail: String): Boolean {
        return getUserType(userMail) != null
    }

    override fun createUser(userDto: UserDTO) {
        TODO("not implemented")
    }

}