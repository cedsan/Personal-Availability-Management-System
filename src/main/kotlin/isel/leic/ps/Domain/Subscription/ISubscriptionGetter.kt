package isel.leic.ps.Domain.Subscription

import isel.leic.ps.DTO.SubscriptionDTO

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines the contract to obtain information about a subscription
 * and provide flexibility/multiple subscription
 */
interface ISubscriptionGetter {

    /** From the body of a subscription request
     *  create the SubscriptionData
     */
    fun getSubscription(body: String) : SubscriptionDTO
}