package isel.leic.ps.DAL.SqlQueries


class VisibilityQueries {

    companion object {
        val INSERT_VISIBILITY_ASSOC = "INSERT INTO VISIBILITY (fk_Observer, fk_Observable) "+
                "VALUES (?, ?);"

        val DELETE_VISIBILITY_ASSOC = "DELETE FROM VISIBILITY WHERE fk_Observer = ? AND fk_Observable = ?;"

        val SELECT_1_IF_EXISTS_ASSOC_BETWEEN_OBSERVER_OBSERVABLE = "SELECT COUNT(1) FROM VISIBILITY WHERE fk_Observer = ? AND fk_Observable = ?"
    }
}