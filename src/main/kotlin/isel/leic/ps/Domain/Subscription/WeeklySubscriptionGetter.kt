package isel.leic.ps.Domain.Subscription

import com.fasterxml.jackson.databind.ObjectMapper
import isel.leic.ps.DTO.SubscriptionDTO
import isel.leic.ps.DTO.WeekDaySubscriptionDTO

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Provides the implementation for obtaining a subscription types.
 *
 */
class WeeklySubscriptionGetter : ISubscriptionGetter{
    override fun getSubscription(body: String): SubscriptionDTO {
        return ObjectMapper().readValue(body, WeekDaySubscriptionDTO::class.java)
    }
}