package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.FirebaseNotifcationRegistrationParamsDTO
import isel.leic.ps.DTO.ObserverDTO
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access Observer entities
 * from a DataSource
 */
interface IObserverDAL {

    fun getAllObservers():Collection<Observer>

    fun getObserverById(id: Int) : Observer

    fun getObserverByNumber(number: Int) : Observer

    fun createObserver(observerDTO: ObserverDTO) : Unit

    fun getAllObservedOfObserverByObserverID(observerID: Int): Collection<Observable>

    fun getObservableOfObserverById(observerID: Int, observedID: Int): Observable
    fun updateObserverRegistrationToken(registrationParams: FirebaseNotifcationRegistrationParamsDTO?)
    fun getObsevablesObserverAlreadyHasSubscription(observerID: Int): Collection<Observable>
    fun getObserverByEmail(username: String): Observer?

}