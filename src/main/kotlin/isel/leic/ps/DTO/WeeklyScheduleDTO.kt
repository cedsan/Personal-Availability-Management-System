package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WeeklyScheduleDTO(val dayOfWeek: Int = 0, val startAvailabilityHour: Int = 0, val startAvailabilityMinute: Int = 0, val endAvailabilityHour: Int = 0, val endAvailabilityMinute: Int = 0, val availabilityLocationLatitude: Double? = null, val availabilityLocationLongitude: Double? = null, scheduleType: String): ScheduleDTO(scheduleType) {
    constructor(): this(scheduleType = "") {

    }
}