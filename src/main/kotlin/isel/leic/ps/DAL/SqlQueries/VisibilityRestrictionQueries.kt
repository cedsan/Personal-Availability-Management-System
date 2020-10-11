package isel.leic.ps.DAL.SqlQueries


class VisibilityRestrictionQueries {

    companion object {
        val UPDATE_VISIBILITY_RESTRICTION_OF_OBSERVABLE_ON_OBSERVER = "UPDATE VisibilityRestriction SET canSeeWhenAvailable = ?, canSeeSchedule = ?, canSeeLocation = ? WHERE fk_Observer = ? AND fk_Observable = ?;"
        val SELECT_VISIBILITY_RESTRICTION_OF_OBSERVABLE_ON_OBSERVER = "SELECT * FROM VisibilityRestriction WHERE fk_Observable = ? AND fk_Observer = ?;"
    }
}