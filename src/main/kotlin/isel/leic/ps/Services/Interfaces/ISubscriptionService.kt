package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.SubscriptionDTO
import isel.leic.ps.Domain.Subscription.SubscriptionEnum
import isel.leic.ps.Entities.Subscription

interface ISubscriptionService {
    fun getIsObserverSubscribedToObservable(observerID: Int, observedID: Int): Boolean
    fun addSubscriptionFromObserverOnObservable(subscription: SubscriptionDTO, observerId: Int, observableId: Int, subscriptionType: SubscriptionEnum)
    fun getAllSubscriptionsOfObserverOnObservable(observerId: Int, observableId: Int, subscriptionType: SubscriptionEnum? = null): Collection<Subscription>
    fun getSubscriptionById(subscriptionID: Int, subscriptionType: SubscriptionEnum): Subscription
    fun getSubscriptionOfObserverOnObservableEqualsToReceived(subscription: SubscriptionDTO, observerId: Int, observableId: Int, subscriptionType: SubscriptionEnum): Subscription
    fun removeSubscriptionOfObserverOnObservableById(subscriptionID: Int, observerID: Int, observableID: Int, subscriptionType: SubscriptionEnum)
    fun removeAllSubscriptionsOfObservableOnObservable(observerID: Int, observableId: Int)
    fun updateSubscription(subscription: SubscriptionDTO, subscriptionId: Int, subscriptionType: SubscriptionEnum)
}