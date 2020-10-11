package isel.leic.ps.DAL.SqlQueries


class WeeklyScheduleQueries {
    companion object {
        val SELECT_ALL_WEEKDAY_SCHEDULES_OF_OBSERVABLE = "SELECT * FROM ScheduleDayOfWeek WHERE fk_Observable = ?;"
        val SELECT_WEEKDAY_SCHEDULE_BY_ID = "SELECT * FROM ScheduleDayOfWeek WHERE id = ?;"
        val SELECT_LAST_WEEKDAY_SCHEDULE_OF_OBSERVABLE = "SELECT * FROM ScheduleDayOfWeek WHERE id = (SELECT MAX(id) FROM ScheduleDayOfWeek WHERE fk_observable = ?);"
        val INSERT_WEEKDAY_SCHEDULE = "INSERT INTO ScheduleDayOfWeek (dayOfWeek, startAvailabilityHour, startAvailabilityMinute, endAvailabilityHour, endAvailabilityMinute, latitude, longitude, hasLocation, fk_Observable) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
        val DELETE_ALL_WEEKLY_SCHEDULES_OF_OBSERVABLE = "DELETE FROM ScheduleDayOfWeek WHERE fk_Observable = ?;"
        val DELETE_SCHEDULE_BY_ID = "DELETE FROM ScheduleDayOfWeek WHERE id = ?;"
        val UPDATE_WEEKLY_SCHEDULE_BY_ID = "UPDATE ScheduleDayOfWeek SET dayOfWeek = ?, startAvailabilityHour = ?, startAvailabilityMinute = ?, endAvailabilityHour = ?, endAvailabilityMinute = ?, " +
                "latitude = ?, longitude = ?, hasLocation = ? WHERE id = ?;"
    }
}