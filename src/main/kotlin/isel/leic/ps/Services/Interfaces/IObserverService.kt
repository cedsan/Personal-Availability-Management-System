package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.FirebaseNotifcationRegistrationParamsDTO
import isel.leic.ps.DTO.ObserverDTO
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer
import isel.leic.ps.Entities.VisibilityRestriction

/**
 * Specifies the contract that all implementations of Service for an Observer Controller
 * should support.
 */
interface IObserverService {

    fun getAllObservers():Collection<Observer>

    fun getObserverById(id: Int) : Observer

    fun getObserverByNumber(number: Int) : Observer

    fun createObserver(observerDTO: ObserverDTO) : Observer
    fun getAllObservableOfObserverByObserverID(observerID: Int): Collection<Observable>
    fun getObservableOfObserverById(observerID: Int, observedID: Int): Observable
    fun saveRegistrationToken(registrationParams: FirebaseNotifcationRegistrationParamsDTO?)

    fun getObsevablesObserverAlreadyHasSubscriptionByObserverId(observerID: Int): Collection<Observable>

    fun getVisibilityRestrictionOfObservableOnObserver(observerId: Int, observableId: Int): VisibilityRestriction
    fun getObserverByEmail(username: String): Observer?
}