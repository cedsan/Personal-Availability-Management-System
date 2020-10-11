package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IObservableDAL
import isel.leic.ps.DAL.SqlQueries.ObservableQueries
import isel.leic.ps.DAL.SqlQueries.ObserverQueries
import isel.leic.ps.DTO.ObservableDTO
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer
import isel.leic.ps.Exception.UserAlreadyExistsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.dao.DuplicateKeyException
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
 * Defines implementations of the operations access Observable
 * entites from the used DataSource
 */
@Repository
open class ObservableDBDAL: IObservableDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun getAllObservable(): Collection<Observable> {
        jdbcTemplate = getJdbcTemplate()
        val observables = jdbcTemplate.query(ObservableQueries.SELECT_ALL_OBSERVABLES, ObservableRowMapper())
        return observables
    }

    override fun getObservableById(id: Int): Observable {
        jdbcTemplate = getJdbcTemplate()
        var observable = jdbcTemplate.queryForObject<Observable>(ObservableQueries.SELECT_OBSERVABLE_BY_ID, arrayOf(id), ObservableRowMapper())
        return observable
    }

    override fun getObservableByNumber(number: Int): Observable {
        jdbcTemplate = getJdbcTemplate()
        var observable = jdbcTemplate.queryForObject<Observable>(ObservableQueries.SELECT_OBSERVABLE_BY_NUMBER, arrayOf(number), ObservableRowMapper())
        return observable
    }

    override fun createObservable(observableDTO: ObservableDTO) {
        jdbcTemplate = getJdbcTemplate()
        var arrayOfArgs = arrayOf(observableDTO.number, observableDTO.email, observableDTO.name, observableDTO.avatar_url)
        var arrayOfArgTypes  = intArrayOf(Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR)
        try {
            var rowsAffected = jdbcTemplate.update(ObservableQueries.INSERT_OBSERVABLE, arrayOfArgs, arrayOfArgTypes)
            if (rowsAffected == 0) throw IllegalArgumentException()
        }catch (e: DuplicateKeyException) {
            throw UserAlreadyExistsException()
        }
    }

    override fun getAllObserversOfObservableById(observableId: Int): Collection<Observer> {
        jdbcTemplate = getJdbcTemplate()
        val observers= jdbcTemplate.query(ObservableQueries.SELECT_ALL_OBSERVERS_OF_OBSERVABLE_BY_OBSERVABLE_ID, arrayOf(observableId), ObserverDBDAL.ObserverRowMapper())
        return observers
    }

    override fun getObservableByEmail(username: String): Observable {
        jdbcTemplate = getJdbcTemplate()
        var observable = jdbcTemplate.queryForObject<Observable>(ObservableQueries.SELECT_OBSERVABLE_BY_EMAIL, arrayOf(username), ObservableRowMapper())
        return observable
    }

    override fun isIdOfObservable(observableId: Int): Boolean = getObservableById(observableId) != null

    class ObservableRowMapper : RowMapper<Observable> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): Observable {
            return Observable(rs!!.getInt(1),
                    rs.getString(4),
                    rs.getInt(2),
                    rs.getString(3),
                    rs.getString(5),
                    rs.getBoolean(6)
            )
        }
    }
}