package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.IObserverDAL
import isel.leic.ps.DAL.SqlQueries.ObserverQueries
import isel.leic.ps.DTO.FirebaseNotifcationRegistrationParamsDTO
import isel.leic.ps.DTO.ObserverDTO
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer
import isel.leic.ps.Exception.EntityNotFoundException
import isel.leic.ps.Exception.UserAlreadyExistsException
import org.springframework.beans.factory.annotation.Autowired
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
 * Defines implementations of the operations access Observer
 * entites from the used DataSource
 */
@Repository
open class ObserverDBDAL : IObserverDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun getAllObservers(): Collection<Observer> {
        jdbcTemplate = getJdbcTemplate()
        var observer = jdbcTemplate.query<Observer>(ObserverQueries.SELECT_ALL_OBSERVERS, ObserverRowMapper())
        return observer
    }

    override fun getObserverById(id: Int): Observer {
        jdbcTemplate = getJdbcTemplate()
        var observer: Observer
        try {
            observer = jdbcTemplate.queryForObject<Observer>(ObserverQueries.SELECT_OBSERVER_BY_ID, arrayOf(id), ObserverRowMapper())
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("No Observer entity found with query for specified id")
        }

        return observer
    }

    override fun getObserverByNumber(number: Int): Observer {
        jdbcTemplate = getJdbcTemplate()
        var observer = jdbcTemplate.queryForObject<Observer>(ObserverQueries.SELECT_OBSERVER_BY_NUMBER, arrayOf(number), ObserverRowMapper())
        return observer
    }

    override fun createObserver(observerDTO: ObserverDTO) {
        jdbcTemplate = getJdbcTemplate()
        var arrayOfQueryArgs = arrayOf(observerDTO.number, observerDTO.email, observerDTO.name, observerDTO.avatar_url)
        var arrayOfArgTypes  = intArrayOf(Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR)
        try {
            var rowsAffected = jdbcTemplate.update(ObserverQueries.INSERT_OBSERVER, arrayOfQueryArgs, arrayOfArgTypes)
            if (rowsAffected == 0) throw IllegalArgumentException()
        }catch (e: DuplicateKeyException) {
            throw UserAlreadyExistsException()
        }
    }

    override fun getAllObservedOfObserverByObserverID(observerID: Int): Collection<Observable> {
        jdbcTemplate = getJdbcTemplate()
        var observables = jdbcTemplate.query<Observable>(ObserverQueries.SELECT_ALL_OBSERVABLE_OF_OBSERVER_BY_OBSERVED_ID, arrayOf(observerID) ,ObservableDBDAL.ObservableRowMapper())
        return observables
    }

    override fun getObservableOfObserverById(observerID: Int, observedID: Int): Observable {
        jdbcTemplate = getJdbcTemplate()
        var observable = jdbcTemplate.queryForObject<Observable>(ObserverQueries.SELECT_OBSERVABLE_VISIBLE_TO_OBSERVER_BY_OBSERVER_ID, arrayOf(observerID, observedID), ObservableDBDAL.ObservableRowMapper())
        return observable
    }

    override fun updateObserverRegistrationToken(registrationParams: FirebaseNotifcationRegistrationParamsDTO?) {
        jdbcTemplate = getJdbcTemplate()
        val rowsAffected = jdbcTemplate.update(ObserverQueries.UPDATE_OBSERVER_REGISTRATION_TOKEN_BY_ID, arrayOf(registrationParams?.registration_token, registrationParams?.user_id),
                intArrayOf(Types.VARCHAR, Types.INTEGER))
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    override fun getObsevablesObserverAlreadyHasSubscription(observerID: Int): Collection<Observable> {
        jdbcTemplate = getJdbcTemplate()
        var observablesSubscribed = jdbcTemplate.query<Observable>(ObserverQueries.SELECT_ALL_OBSERVABLE_SUBSCRIBED_OF_OBSERVER_BY_OBSERVED_ID, arrayOf(observerID, observerID) ,ObservableDBDAL.ObservableRowMapper())
        return observablesSubscribed
    }

    override fun getObserverByEmail(username: String): Observer? {
        jdbcTemplate = getJdbcTemplate()
        var observer: Observer? = null
        try {
            observer = jdbcTemplate.queryForObject<Observer>(ObserverQueries.SELECT_OBSERVER_BY_EMAIL, arrayOf(username), ObserverRowMapper())
        } catch (e: EmptyResultDataAccessException) {
            throw EntityNotFoundException("No Observer entity found with query for specified email")
        }
        return observer
    }

    class ObserverRowMapper: RowMapper<Observer> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): Observer {
            return Observer(rs!!.getInt(1),
                            rs.getString(4),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getString(5),
                            rs.getString(6)
                            )
        }
    }
}