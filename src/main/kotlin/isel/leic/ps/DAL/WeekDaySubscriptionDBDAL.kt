package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.ISubscriptionDAL
import isel.leic.ps.DAL.SqlQueries.WeekDaySubscriptionQueries
import isel.leic.ps.DTO.SubscriptionDTO
import isel.leic.ps.DTO.WeekDaySubscriptionDTO
import isel.leic.ps.Entities.Subscription
import isel.leic.ps.Entities.WeekDaySubscription
import org.apache.http.HttpEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Types
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines implementations of the operations access WeekDaySubscription
 * entites from the used DataSource
 */
@Repository
open class WeekDaySubscriptionDBDAL : ISubscriptionDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))
    }

    override fun getAllSubscriptionsOfObserverOnObservable(observerID: Int, observedID: Int): Collection<Subscription> {
        jdbcTemplate = getJdbcTemplate()
        val collectionSubscription = jdbcTemplate.query(WeekDaySubscriptionQueries.SELECT_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE, arrayOf(observerID, observedID), SubscriptionRowMapper())
        return collectionSubscription
    }

    override fun getIsObserverSubscribedToObservable(observerID: Int, observedID: Int): Boolean {
        jdbcTemplate = getJdbcTemplate()
        var nrRows: Int = jdbcTemplate.queryForObject(WeekDaySubscriptionQueries.SELECT_1_IF_OBSERVER_HAS_SUBSCRIPTION_ON_OBSERVABLE, arrayOf(observerID, observedID), Int::class.java)
        return nrRows > 0
    }

    override fun addSubscriptionFromObserverOnObservable(subscription: SubscriptionDTO, observerId: Int, observableId: Int) {
        subscription as WeekDaySubscriptionDTO
        jdbcTemplate = getJdbcTemplate()
        val arrayOfArgs: Array<Any> = arrayOf<Any>(subscription.dayOfWeek, subscription.startWatchHour,
                subscription.startWatchMinutes, subscription.endWatchHour, subscription.endWatchMinutes, observerId, observableId)
        val arrayOfTypes: IntArray = intArrayOf(Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.INTEGER, Types.INTEGER)
        val rowsAffected = jdbcTemplate.update(WeekDaySubscriptionQueries.INSERT_SUBSCRIPTION, arrayOfArgs, arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    override fun updateSubscriptionDetails(id: Int, subscription: SubscriptionDTO) {
        subscription as WeekDaySubscriptionDTO
        jdbcTemplate = getJdbcTemplate()
        val arrayOfTypes: IntArray = intArrayOf(Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.SMALLINT, Types.INTEGER)
        val rowsAffected = jdbcTemplate.update(WeekDaySubscriptionQueries.UPDATE_SUBSCRIPTION, arrayOf(subscription.dayOfWeek, subscription.startWatchHour,
                subscription.startWatchMinutes, subscription.endWatchHour, subscription.endWatchMinutes, id), arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    override fun getSubscriptionById(subscriptionID: Int): Subscription {
        jdbcTemplate = getJdbcTemplate()
        return jdbcTemplate.queryForObject(WeekDaySubscriptionQueries.SELECT_SUBSCRIPTION_BY_ID, arrayOf(subscriptionID), SubscriptionRowMapper())
    }

    override fun getSubscriptionOfObserverOnObservableEqualsToReceived(subscriptionDTO: SubscriptionDTO, observerId: Int, observableId: Int): Subscription {
        subscriptionDTO as WeekDaySubscriptionDTO
        jdbcTemplate = getJdbcTemplate()
        return jdbcTemplate.queryForObject(WeekDaySubscriptionQueries.SELECT_SUBSCRIPTION_WITH_SPECIFIED_ATTRS, arrayOf(observerId, observableId, subscriptionDTO.dayOfWeek, subscriptionDTO.startWatchHour,
                subscriptionDTO.startWatchMinutes, subscriptionDTO.endWatchHour, subscriptionDTO.endWatchMinutes), SubscriptionRowMapper())
    }

    override fun removeSubscriptionOfObserverOnObservableById(subscriptionID: Int, observerID: Int, observableID: Int) {
        jdbcTemplate = getJdbcTemplate()
        jdbcTemplate.update(WeekDaySubscriptionQueries.DELETE_SUBSCRIPTION_OF_OBSERVER_ON_OBSERVABLE_BY_ID, arrayOf(observerID, observableID, subscriptionID), intArrayOf(Types.INTEGER, Types.INTEGER, Types.INTEGER) )
    }

    override fun removeSubscriptionsOfObserverOnObservable(observerID: Int, observableId: Int) {
        jdbcTemplate = getJdbcTemplate()
        jdbcTemplate.update(WeekDaySubscriptionQueries.DELETE_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE, arrayOf(observerID, observableId), intArrayOf(Types.INTEGER, Types.INTEGER))
    }

    class SubscriptionRowMapper: RowMapper<Subscription> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): Subscription {
            return WeekDaySubscription(rs!!.getInt(1),
                                rs.getInt(2),
                                rs.getInt(3),
                                rs.getInt(4),
                                rs.getInt(5),
                                rs.getInt(6))
        }
    }
}