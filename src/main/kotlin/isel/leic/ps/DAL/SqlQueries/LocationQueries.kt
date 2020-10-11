package isel.leic.ps.DAL.SqlQueries

class LocationQueries {
    companion object {
        val SELECT_LOCATION_OF_OBSERVABLE = "SELECT * FROM LOCATION WHERE fk_Observable = ?;"
        val  UPDATE_LOCATION = "UPDATE LOCATION SET latitude = ?, longitude = ?, isValid = TRUE, lastUpdateDateTime = current_timestamp WHERE fk_Observable = ?;"
    }
}