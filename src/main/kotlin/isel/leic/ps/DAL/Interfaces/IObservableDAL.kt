package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.ObservableDTO
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access Observable entities
 * from a DataSource
 */
interface IObservableDAL {

    fun getAllObservable():Collection<Observable>

    fun getObservableById(id: Int) : Observable

    fun getObservableByNumber(number: Int) : Observable

    fun createObservable(observableDTO: ObservableDTO) : Unit
    fun  getAllObserversOfObservableById(observableId: Int): Collection<Observer>

    fun isIdOfObservable(observableId: Int): Boolean
    fun getObservableByEmail(username: String): Observable
}