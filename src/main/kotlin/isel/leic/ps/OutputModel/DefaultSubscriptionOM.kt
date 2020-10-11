package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity

/**
 * Represents the OutputModel for the DefaultSubscription entity.
 * Since the following action it's a binary value, represent it
 * through the boolean
 */
@Siren4JEntity(entityClass = arrayOf("subscription", "default_subscription"))
class DefaultSubscriptionOM(id: Int, subscriptionType: String): SubscriptionOM(id, subscriptionType) {
    val following = true
}