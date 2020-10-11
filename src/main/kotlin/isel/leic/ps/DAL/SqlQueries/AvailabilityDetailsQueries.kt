package isel.leic.ps.DAL.SqlQueries


class AvailabilityDetailsQueries {

    companion object {
        val  SET_OBSERVABLE_AVAILABLE = "UPDATE AvailabilityDetails  SET isAvailable = TRUE WHERE fk_Observable = ?;"
        val  SET_OBSERVABLE_UNAVAILABLE = "UPDATE AvailabilityDetails  SET isAvailable = FALSE WHERE fk_Observable = ?;"
        val  SELECT_AVAILABILITY_OF_OBSERVABLE = "SELECT isAvailable FROM AvailabilityDetails WHERE fk_Observable = ?;"
    }
}