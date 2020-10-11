package isel.leic.ps.Domain.Subscription

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * The factory to use to instantiate a given SubscriptionGetter
 * from a given subscription type enum specified
 */
class SubscriptionGetterFactory {

    companion object {

        var subscriptionGetterMap : HashMap<SubscriptionEnum, ISubscriptionGetter?> = HashMap()

        init {
            subscriptionGetterMap.put(SubscriptionEnum.DEFAULT, DefaultSubscriptionGetter())
            subscriptionGetterMap.put(SubscriptionEnum.WEEKDAY, WeeklySubscriptionGetter())
        }

        /**
         * Factory method that according to the type of
         * the Subscription passed, returns the instance to create it
         */
        fun getSubscriptionGetter(type: SubscriptionEnum): ISubscriptionGetter {
            return subscriptionGetterMap[type]!!
        }
    }

}