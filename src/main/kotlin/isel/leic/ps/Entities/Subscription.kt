package isel.leic.ps.Entities

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class logically represents the generalization Entity for the various
 * subscriptions.
 */
open class Subscription(val subscriptionType: String) {

    override fun equals(other: Any?): Boolean {
        return other!!::class.java == this::class.java
                && this.subscriptionType.equals((other as Subscription).subscriptionType)
    }

}