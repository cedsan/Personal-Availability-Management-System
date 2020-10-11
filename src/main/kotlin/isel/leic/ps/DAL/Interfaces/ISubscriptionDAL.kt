package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.SubscriptionDTO
import isel.leic.ps.Entities.Subscription

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access Subscription entities
 * from a DataSource
 */
interface ISubscriptionDAL {
    fun getAllSubscriptionsOfObserverOnObservable(observerID: Int, observedID: Int): Collection<Subscription>
    fun getIsObserverSubscribedToObservable(observerID: Int, observedID: Int): Boolean
    fun addSubscriptionFromObserverOnObservable(subscription: SubscriptionDTO, observerId: Int, observableId: Int)
    fun updateSubscriptionDetails(id: Int, subscription: SubscriptionDTO)
    fun getSubscriptionById(subscriptionID: Int): Subscription
    fun removeSubscriptionOfObserverOnObservableById(subscriptionID: Int, observerID: Int, observableID: Int)
    fun removeSubscriptionsOfObserverOnObservable(observerID: Int, observableId: Int)
    fun getSubscriptionOfObserverOnObservableEqualsToReceived(subscriptionDTO: SubscriptionDTO, observerId: Int, observableId: Int): Subscription
}