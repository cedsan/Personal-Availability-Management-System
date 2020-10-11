package isel.leic.ps.DAL


import isel.leic.ps.DAL.Interfaces.IRequestControlAccessParamsDAL
import isel.leic.ps.Entities.RequestControlAccessParams
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
 * Defines implementations of the operations access PathControlAccesParams
 * entites from the used DataSource
 */
@Repository
open class RequestControlAccessParamsDAL: IRequestControlAccessParamsDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate : JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun getAll(): Collection<RequestControlAccessParams> {
        jdbcTemplate = getJdbcTemplate()
        var requestControlAccessParams = jdbcTemplate.query("SELECT * FROM PathControlAccessParams;", RequestControlAccessParamsRowMapper())
        return requestControlAccessParams
    }

    class RequestControlAccessParamsRowMapper: RowMapper<RequestControlAccessParams> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): RequestControlAccessParams {
            return RequestControlAccessParams(rs!!.getString(1),
                    rs.getString(2),
                    rs.getString(3).split(' '))
        }
    }
}