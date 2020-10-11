package isel.leic.ps.DAL.SqlQueries

class ObservableQueries {
    companion object {
        val SELECT_ALL_OBSERVABLES = "SELECT OBSERVABLE.*, AvailabilityDetails.isAvailable FROM OBSERVABLE INNER JOIN AvailabilityDetails ON (Observable.id = AvailabilityDetails.fk_Observable);"
        val SELECT_OBSERVABLE_BY_ID = "SELECT OBSERVABLE.*, AvailabilityDetails.isAvailable FROM OBSERVABLE INNER JOIN AvailabilityDetails ON (Observable.id = AvailabilityDetails.fk_Observable) WHERE id = ?"
        val SELECT_OBSERVABLE_BY_EMAIL = "SELECT OBSERVABLE.*, AvailabilityDetails.isAvailable FROM OBSERVABLE INNER JOIN AvailabilityDetails ON (Observable.id = AvailabilityDetails.fk_Observable) WHERE email = ?"
        val  SELECT_OBSERVABLE_BY_NUMBER = "SELECT OBSERVABLE.*, AvailabilityDetails.isAvailable FROM OBSERVABLE INNER JOIN AvailabilityDetails ON (Observable.id = AvailabilityDetails.fk_Observable) WHERE number = ?"
        val  INSERT_OBSERVABLE = "INSERT INTO OBSERVABLE (number, email, name, avatar_url) "+
                                 "VALUES (?, ?, ?, ?);"
        val SELECT_ALL_OBSERVERS_OF_OBSERVABLE_BY_OBSERVABLE_ID = "SELECT * FROM OBSERVER WHERE ID IN (SELECT fk_Observer FROM VISIBILITY WHERE fk_Observable = ?);"
    }
}