package isel.leic.ps.DAL.SqlQueries

class WeekDaySubscriptionQueries {
    companion object {
        val SELECT_SUBSCRIPTION_BY_ID = "SELECT * FROM WeekdaySubscription WHERE id = ?"
        val SELECT_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE = "SELECT * FROM WeekdaySubscription WHERE fk_Observer = ? AND fk_Observable = ?"

        val INSERT_SUBSCRIPTION = "INSERT INTO WeekdaySubscription (dayOfWeek, startNotificableHour, startNotificableMinute, endNotificableHour, endNotificableMinute, fk_Observer, fk_Observable) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?);"
        val UPDATE_SUBSCRIPTION = "UPDATE WeekdaySubscription SET dayOfWeek = ?, startNotificableHour = ?, startNotificableMinute = ?, endNotificableHour = ?, endNotificableMinute = ? " +
                                  "WHERE id = ?;"
        val SELECT_1_IF_OBSERVER_HAS_SUBSCRIPTION_ON_OBSERVABLE = "SELECT COUNT(1) FROM WeekdaySubscription WHERE fk_Observer = ? AND fk_Observable = ?"
        val  DELETE_SUBSCRIPTION_BY_ID = "DELETE FROM WeekdaySubscription WHERE id = ?"
        val  SELECT_SUBSCRIPTION_WITH_SPECIFIED_ATTRS: String? = "SELECT * FROM WeekdaySubscription WHERE fk_Observer = ? AND fk_Observable = ? AND dayOfWeek = ? AND startNotificableHour = ? AND startNotificableMinute = ? " +
                                                                 "AND endNotificableHour = ? AND endNotificableMinute = ?"
        val DELETE_SUBSCRIPTION_OF_OBSERVER_ON_OBSERVABLE_BY_ID = "DELETE FROM WeekdaySubscription WHERE fk_Observer = ? AND fk_Observable = ? AND id = ?;"
        val DELETE_ALL_SUBSCRIPTIONS_OF_OBSERVER_ON_OBSERVABLE = "DELETE FROM WeekdaySubscription WHERE fk_Observer = ? AND fk_Observable = ?;"
    }

}