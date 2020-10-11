package isel.leic.ps.Entities

import isel.leic.ps.Domain.Subscription.SubscriptionEnum

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Represents a concrete Subscription
 * entity that defines a day in week and hour
 * to be notified of the observable changes
 */
data class WeekDaySubscription(val id: Int, val dayOfWeek: Int, val startWatchHour: Int, val startWatchMinutes: Int, val endWatchHour: Int, val endWatchMinutes: Int): Subscription(SubscriptionEnum.WEEKDAY.label)