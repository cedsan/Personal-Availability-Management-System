package isel.leic.ps.Domain.Subscription

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines the different subscription strategies
 * available.
 */
enum class SubscriptionEnum(val label: String) {
    DEFAULT("default"), /* Always on, whenever Observable available gets signaled */
    WEEKDAY("weekday")
}