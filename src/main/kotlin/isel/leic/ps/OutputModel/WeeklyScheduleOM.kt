package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(entityClass = arrayOf("weekly_schedule", "schedule"))
class WeeklyScheduleOM(id: Int, val dayOfWeek: Int, val startAvailabilityHour: Int, val startAvailabilityMinute: Int, val endAvailabilityHour: Int, val endAvailabilityMinute: Int, val availabilityLocationLatitude: Double?, val availabilityLocationLongitude: Double?, val hasValidLocation: Boolean, scheduletType: String): ScheduleOM(id, scheduletType) {
}