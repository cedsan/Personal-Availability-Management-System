package isel.leic.ps.DAL.SqlQueries


class DefaultSubscriptionQueries {

    companion object {
        val SELECT_SUBSCRIPTION_BY_ID = "SELECT * FROM DefaultSubscription WHERE id = ?"
        val SELECT_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE = "SELECT * FROM DefaultSubscription WHERE fk_Observer = ? AND fk_Observable = ?"

        val INSERT_SUBSCRIPTION = "INSERT INTO DefaultSubscription (fk_Observer, fk_Observable) " +
                "VALUES (?, ?);"
        val SELECT_1_IF_OBSERVER_HAS_SUBSCRIPTION_ON_OBSERVABLE = "SELECT COUNT(1) FROM DefaultSubscription WHERE fk_Observer = ? AND fk_Observable = ?"
        //val  DELETE_SUBSCRIPTION_BY_ID = "DELETE FROM DefaultSubscription WHERE id = ?"
        val DELETE_SUBSCRIPTION_OF_OBSERVER_ON_OBSERVABLE_BY_ID = "DELETE FROM DefaultSubscription WHERE fk_Observer = ? AND fk_Observable = ? AND id = ?;"
        val DELETE_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE = "DELETE FROM DefaultSubscription WHERE fk_Observer = ? AND fk_Observable = ?;"
    }
}