package isel.leic.ps.Services

import isel.leic.ps.DAL.Interfaces.ISubscriptionDAL
import isel.leic.ps.DAL.Interfaces.IVisibilityDAL
import isel.leic.ps.DTO.SubscriptionDTO
import isel.leic.ps.DTO.WeekDaySubscriptionDTO
import isel.leic.ps.Domain.Subscription.SubscriptionEnum
import isel.leic.ps.Entities.Subscription
import isel.leic.ps.Entities.WeekDaySubscription
import isel.leic.ps.Services.Interfaces.ISubscriptionService
import isel.leic.ps.Services.Interfaces.IVisibilityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the the business layer operations for the Subscription resource.
 * Since the Subscription entity is the general entity, we map the specializations to
 * their specific business layer.
 */
@Service
open class SubscriptionService: ISubscriptionService {
    val subscriptionDALMap: HashMap<SubscriptionEnum, ISubscriptionDAL>
    private val visibilityDAL: IVisibilityDAL
    val subscriptionEqualsPredicateMap: HashMap<SubscriptionEnum, (Subscription, SubscriptionDTO) -> Boolean>

    @Autowired
    constructor(_subscriptionDAL: HashMap<SubscriptionEnum, ISubscriptionDAL>, _visibilityDAL: IVisibilityDAL) {
        subscriptionDALMap = _subscriptionDAL
        visibilityDAL = _visibilityDAL
        subscriptionEqualsPredicateMap = HashMap()
        subscriptionEqualsPredicateMap.put(SubscriptionEnum.WEEKDAY, this::areWeekdaySubscriptionsEquals)
    }

    override fun getIsObserverSubscribedToObservable(observerID: Int, observedID: Int): Boolean {
        var hasSubscription = false
        for(subscriptionTypes in subscriptionDALMap.keys) {
            hasSubscription = subscriptionDALMap[subscriptionTypes]!!.getIsObserverSubscribedToObservable(observerID, observedID)
            if(hasSubscription) break
        }
        return hasSubscription
    }
    override fun getAllSubscriptionsOfObserverOnObservable(observerId: Int, observableId: Int, subscriptionType: SubscriptionEnum?): Collection<Subscription> {
        val subscriptions: Collection<Subscription>
        if(subscriptionType == null) {
            subscriptions = ArrayList<Subscription>()
            subscriptionDALMap.keys.forEach {
                subscriptions.addAll(subscriptionDALMap[it]!!.getAllSubscriptionsOfObserverOnObservable(observerId, observableId))
            }
        }
        else subscriptions = subscriptionDALMap[subscriptionType]!!.getAllSubscriptionsOfObserverOnObservable(observerId, observableId)

        return subscriptions
    }

    override fun addSubscriptionFromObserverOnObservable(subscription: SubscriptionDTO, observerId: Int, observableId: Int, subscriptionType: SubscriptionEnum) {
        if(!visibilityDAL.hasAssocBetween(observerId, observableId)) {
            throw UnsupportedOperationException()
        }

        if (subscriptionDALMap[subscriptionType]!!.getIsObserverSubscribedToObservable(observerId, observableId)) {
            var subscriptions: Collection<Subscription> = getAllSubscriptionsOfObserverOnObservable(observerId, observableId)
            var existingSubs = subscriptions.stream()
                    .filter({ subscriptionEquals(it, subscription)})
                    .findAny()
            if(existingSubs.isPresent) {
                throw IllegalArgumentException("SubscriptionAlreadyExists")
            }
        }
        subscriptionDALMap[subscriptionType]!!.addSubscriptionFromObserverOnObservable(subscription, observerId, observableId)
    }

    override fun getSubscriptionById(subscriptionID: Int, subscriptionType: SubscriptionEnum): Subscription {
        return subscriptionDALMap[subscriptionType]!!.getSubscriptionById(subscriptionID)
    }

    override fun getSubscriptionOfObserverOnObservableEqualsToReceived(subscription: SubscriptionDTO, observerId: Int, observableId: Int, subscriptionType: SubscriptionEnum): Subscription {
        return subscriptionDALMap[subscriptionType]!!.getSubscriptionOfObserverOnObservableEqualsToReceived(subscription, observerId, observableId)
    }

    override fun removeSubscriptionOfObserverOnObservableById(subscriptionID: Int, observerID: Int, observableID: Int, subscriptionType: SubscriptionEnum) {
        subscriptionDALMap[subscriptionType]!!.removeSubscriptionOfObserverOnObservableById(subscriptionID, observerID, observableID)
    }

    override fun removeAllSubscriptionsOfObservableOnObservable(observerID: Int, observableId: Int) {
        subscriptionDALMap.keys.forEach {
            subscriptionDALMap[it]!!.removeSubscriptionsOfObserverOnObservable(observerID, observableId)
        }
    }

    override fun updateSubscription(subscription: SubscriptionDTO, subscriptionId: Int, subscriptionType: SubscriptionEnum) {
        subscriptionDALMap[subscriptionType]!!.updateSubscriptionDetails(subscriptionId, subscription)
    }

    /** Auxiliary functions **/

    private fun subscriptionEquals(sub1: Subscription, sub2: SubscriptionDTO): Boolean {
        if(sub1.subscriptionType.equals(sub2.type).not())
            return false
        return subscriptionEqualsPredicateMap[SubscriptionEnum.valueOf(sub1.subscriptionType.toUpperCase())]!!.invoke(sub1, sub2)
    }

    private fun areWeekdaySubscriptionsEquals(w1: Subscription, w2: SubscriptionDTO): Boolean {
        val sub1 = w1 as WeekDaySubscription
        val sub2 = w2 as WeekDaySubscriptionDTO
        return (sub1.dayOfWeek == sub2.dayOfWeek &&
                sub1.startWatchHour == sub2.startWatchHour &&
                sub1.startWatchMinutes == sub2.startWatchMinutes &&
                sub1.endWatchHour == sub2.endWatchHour &&
                sub1.endWatchMinutes == sub2.endWatchMinutes)
    }
}