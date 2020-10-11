package isel.leic.ps.DAL.SqlQueries


class GlobalVisibilityRestrictionQueries {
    companion object {
        val SELECT_GLOBAL_RESTRICTION_OF_OBSERVABLE = "SELECT * FROM GlobalVisibilityRestriction WHERE fk_Observable = ?;"
        val UPDATE_GLOBAL_VISIBILITY_RESTRICTION_OF_OBSERVABLE_ON_OBSERVER = "UPDATE GlobalVisibilityRestriction SET canSeeWhenAvailable = ?, canSeeSchedule = ?, canSeeLocation = ? WHERE fk_Observable = ?;"
    }
}