package isel.leic.ps.Entities

import isel.leic.ps.Domain.Subscription.SubscriptionEnum


class DefaultSubscription(val id: Int): Subscription(SubscriptionEnum.DEFAULT.label) {
}