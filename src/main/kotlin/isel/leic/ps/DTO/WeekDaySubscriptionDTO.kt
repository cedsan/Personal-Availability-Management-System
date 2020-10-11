package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WeekDaySubscriptionDTO(val dayOfWeek: Int = 0, val startWatchHour: Int = 0, val startWatchMinutes: Int = 0, var endWatchHour: Int = 0, var endWatchMinutes: Int = 0, subscriptionType: String): SubscriptionDTO(subscriptionType) {
    // if doesnt work change it to data class and have hardcoded the Subscription type name
    constructor(): this(subscriptionType = "") {

    }
}