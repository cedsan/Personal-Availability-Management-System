package isel.leic.ps.Services.Helpers

import isel.leic.ps.DAL.Interfaces.IVisibilityRestrictionDAL
import isel.leic.ps.Entities.*
import isel.leic.ps.Entities.Observer
import isel.leic.ps.Exception.EntityNotFoundException
import isel.leic.ps.Services.Interfaces.ISubscriptionService
import java.util.*


class ObservableHelper {
    companion object {
        val MONDAY_UNTIL_FRIDAY = 8
         val SATURDAY_UNTIL_SUNDAY = 9
         val EVERYDAY = 0
         fun filterObserversWithNoRestriction(o: Observer, observableID: Int, visibilityRestrictionDAL: IVisibilityRestrictionDAL): Boolean {
            val visibilityRestrictionOfObserverOnObservable: VisibilityRestriction
            try {
                visibilityRestrictionOfObserverOnObservable = visibilityRestrictionDAL.getVisibilityRestrictionOfObserverOnObservable(o.id, observableID)
            } catch (e: EntityNotFoundException) {
                return false
            }
            return visibilityRestrictionOfObserverOnObservable.canSeeWhenAvailable
        }

        fun filterObserversWithSubscriptionsAbleForNotification(observableID: Int, o: Observer, subscriptionService: ISubscriptionService): Boolean {
            val subscription = subscriptionService.getAllSubscriptionsOfObserverOnObservable(o.id, observableID)
            return subscription.filter(this::filterSubscriptionsAbleForNotificationRightNow)
                    .any() // Returns true if collection contains at least one element
        }


        fun filterSubscriptionsAbleForNotificationRightNow(subscription: Subscription): Boolean {
            // Predicate in case contains a "Following" DefaultSubscription
            if(subscription is DefaultSubscription) return true
            subscription as WeekDaySubscription
            val cal = Calendar.getInstance()
            val curHour = cal[Calendar.HOUR_OF_DAY]
            val curMinute = cal[Calendar.MINUTE]
            val dayOfWeek = cal[Calendar.DAY_OF_WEEK]
            if(isNotificationInSubscriptionDayOfWeek(subscription, dayOfWeek)
                    && isCurHourInSubscriptionInterval(subscription, curHour, curMinute)) {
                if (subscription.endWatchHour > curHour)
                    return true
                if (subscription.endWatchHour == curHour && subscription.endWatchMinutes > curMinute)
                    return true
            }
            // Its out of the notification interval
            return false
        }

        private fun isNotificationInSubscriptionDayOfWeek(subscription: Subscription, dayOfWeek: Int): Boolean {
            subscription as WeekDaySubscription
            return subscription.dayOfWeek == EVERYDAY || subscription.dayOfWeek == dayOfWeek ||
                    (subscription.dayOfWeek >= Calendar.MONDAY && subscription.dayOfWeek <= Calendar.FRIDAY && dayOfWeek == MONDAY_UNTIL_FRIDAY) ||
                    (subscription.dayOfWeek == Calendar.SATURDAY || subscription.dayOfWeek == Calendar.SUNDAY && dayOfWeek == SATURDAY_UNTIL_SUNDAY)
        }

        private fun isCurHourInSubscriptionInterval(subscription: Subscription, curHour: Int, curMinute: Int): Boolean {
            subscription as WeekDaySubscription
            return subscription.startWatchHour < curHour || (subscription.startWatchHour ==  curHour &&
                    subscription.startWatchMinutes <= curMinute)
        }
    }
}