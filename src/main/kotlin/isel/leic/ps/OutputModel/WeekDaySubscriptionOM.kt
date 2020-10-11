package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(entityClass = arrayOf("subscription", "weekday_subscription"))
class WeekDaySubscriptionOM(id: Int, val dayOfWeek: Int, val startWatchHour: Int, val startWatchMinutes: Int, var endWatchHour: Int, var endWatchMinutes: Int, subscriptionType: String) : SubscriptionOM(id, subscriptionType)