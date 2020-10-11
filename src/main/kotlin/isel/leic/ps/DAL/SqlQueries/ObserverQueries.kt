package isel.leic.ps.DAL.SqlQueries

class ObserverQueries {

    companion object {
        val SELECT_ALL_OBSERVERS = "SELECT *  FROM Observer"
        val SELECT_OBSERVER_BY_ID = "SELECT * FROM Observer WHERE id = ?"
        val SELECT_OBSERVER_BY_EMAIL = "SELECT * FROM Observer WHERE email = ?"
        val SELECT_OBSERVER_BY_NUMBER = "SELECT * FROM Observer WHERE number = ?"


        val INSERT_OBSERVER = "INSERT INTO OBSERVER (number, email, name, avatar_url) "+
                              "VALUES (?, ?, ?, ?);"

        // Obtain all the visibility with fk_observer equals to this one
        // with the list of
        val SELECT_ALL_OBSERVABLE_OF_OBSERVER_BY_OBSERVED_ID = "SELECT OBSERVABLE.*, AvailabilityDetails.isAvailable " +
                                                               "FROM OBSERVABLE INNER JOIN AvailabilityDetails ON (Observable.id = AvailabilityDetails.fk_Observable) " +
                                                               "WHERE ID IN (SELECT fk_Observable FROM VISIBILITY WHERE fk_Observer = ?);"

        val SELECT_OBSERVABLE_VISIBLE_TO_OBSERVER_BY_OBSERVER_ID = "SELECT OBSERVABLE.*, AvailabilityDetails.isAvailable " +
                                                                   "FROM OBSERVABLE INNER JOIN AvailabilityDetails ON (Observable.id = AvailabilityDetails.fk_Observable) " +
                                                                   "WHERE ID IN (SELECT fk_Observable FROM VISIBILITY WHERE fk_Observer = ? AND fk_Observable = ?);"

        val UPDATE_OBSERVER_REGISTRATION_TOKEN_BY_ID = "UPDATE OBSERVER SET registration_token = ? WHERE id = ?;"
        val  SELECT_ALL_OBSERVABLE_SUBSCRIBED_OF_OBSERVER_BY_OBSERVED_ID = "SELECT OBSERVABLE.*, AvailabilityDetails.isAvailable " +
                                                                           "FROM OBSERVABLE INNER JOIN AvailabilityDetails ON (Observable.id = AvailabilityDetails.fk_Observable) " +
                                                                           "WHERE ID IN (SELECT fk_observable FROM " +
                                                                           "((SELECT fk_observable FROM DEFAULTSUBSCRIPTION where fk_Observer = ?) UNION " +
                                                                           "(SELECT fk_observable FROM WEEKDAYSUBSCRIPTION where fk_Observer = ?)) AS OBSERVABLE_SUBSCRIBED_ID);"

    }
}