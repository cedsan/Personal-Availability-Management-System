package isel.leic.ps.DAL

import isel.leic.ps.DAL.Interfaces.ISubscriptionDAL
import isel.leic.ps.DAL.SqlQueries.DefaultSubscriptionQueries
import isel.leic.ps.DTO.DefaultSubscriptionDTO
import isel.leic.ps.DTO.SubscriptionDTO
import isel.leic.ps.Domain.Subscription.SubscriptionEnum
import isel.leic.ps.Entities.DefaultSubscription
import isel.leic.ps.Entities.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.sql.ResultSet
import java.sql.Types


open class DefaultSubscriptionDBDAL : ISubscriptionDAL {

    private var dataSourceTransactionManager : DataSourceTransactionManager
    lateinit private var jdbcTemplate: JdbcTemplate

    @Autowired
    constructor(_dataSourceTransactionManager : DataSourceTransactionManager) {
        dataSourceTransactionManager = _dataSourceTransactionManager
    }

    private fun getJdbcTemplate() : JdbcTemplate {
        return JdbcTemplate(SingleConnectionDataSource(dataSourceTransactionManager.dataSource.connection, false))//todo verify if this should be true or false
    }

    override fun getAllSubscriptionsOfObserverOnObservable(observerID: Int, observedID: Int): Collection<Subscription> {
        jdbcTemplate = getJdbcTemplate()
        // Actually gets a single entry in the DefaultSubscription DB table if exists
        val collectionSubscription = jdbcTemplate.query(DefaultSubscriptionQueries.SELECT_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE, arrayOf(observerID, observedID), DefaultSubscriptionRowMapper())
        return collectionSubscription
    }

    override fun getIsObserverSubscribedToObservable(observerID: Int, observedID: Int): Boolean {
        jdbcTemplate = getJdbcTemplate()
        var nrRows: Int = jdbcTemplate.queryForObject(DefaultSubscriptionQueries.SELECT_1_IF_OBSERVER_HAS_SUBSCRIPTION_ON_OBSERVABLE, arrayOf(observerID, observedID), Int::class.java)
        return nrRows > 0
    }

    override fun addSubscriptionFromObserverOnObservable(subscription: SubscriptionDTO, observerId: Int, observableId: Int) {
        jdbcTemplate = getJdbcTemplate()
        val arrayOfArgs: Array<Any> = arrayOf<Any>(observerId, observableId)
        val arrayOfTypes: IntArray = intArrayOf(Types.INTEGER, Types.INTEGER)
        val rowsAffected = jdbcTemplate.update(DefaultSubscriptionQueries.INSERT_SUBSCRIPTION, arrayOfArgs, arrayOfTypes)
        if (rowsAffected == 0) throw IllegalArgumentException()
    }

    override fun updateSubscriptionDetails(id: Int, subscription: SubscriptionDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSubscriptionById(subscriptionID: Int): Subscription {
        jdbcTemplate = getJdbcTemplate()
        return jdbcTemplate.queryForObject(DefaultSubscriptionQueries.SELECT_SUBSCRIPTION_BY_ID, arrayOf(subscriptionID), DefaultSubscriptionRowMapper())
    }

    override fun removeSubscriptionOfObserverOnObservableById(subscriptionID: Int, observerID: Int, observableID: Int) {
        jdbcTemplate = getJdbcTemplate()
        jdbcTemplate.update(DefaultSubscriptionQueries.DELETE_SUBSCRIPTION_OF_OBSERVER_ON_OBSERVABLE_BY_ID, arrayOf(observerID, observableID, subscriptionID), intArrayOf(Types.INTEGER, Types.INTEGER, Types.INTEGER) )
    }

    override fun removeSubscriptionsOfObserverOnObservable(observerID: Int, observableId: Int) {
        jdbcTemplate = getJdbcTemplate()
        jdbcTemplate.update(DefaultSubscriptionQueries.DELETE_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE, arrayOf(observerID, observableId), intArrayOf(Types.INTEGER, Types.INTEGER))
    }

    override fun getSubscriptionOfObserverOnObservableEqualsToReceived(subscriptionDTO: SubscriptionDTO, observerId: Int, observableId: Int): Subscription {
        if(subscriptionDTO is DefaultSubscriptionDTO)
            return getAllSubscriptionsOfObserverOnObservable(observerId, observableId).first()
        throw IllegalArgumentException("Subscription of invalid type")
    }

    class DefaultSubscriptionRowMapper: RowMapper<DefaultSubscription> {
        override fun mapRow(rs: ResultSet?, rowNum: Int): DefaultSubscription {
            return DefaultSubscription(rs!!.getInt(1))
        }
    }
}