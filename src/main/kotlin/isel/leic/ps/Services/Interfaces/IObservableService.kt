package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.LocationDTO
import isel.leic.ps.DTO.ObservableDTO
import isel.leic.ps.Entities.Location
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer

interface IObservableService {

    fun getAllObservable():Collection<Observable>
    fun getObservableById(id: Int) : Observable
    fun getObservableByNumber(number: Int) : Observable
    fun createObservable(observableDTO: ObservableDTO) : Unit
    fun getAllObserversOfObservable(observableId: Int): Collection<Observer>
    fun updateLocation(id: Int, locationDTO: LocationDTO)
    fun getObservableLocation(id: Int): Location
    fun locationEntered(observableId: Int, locationDTO: LocationDTO): Unit
    fun getObservableByEmail(username: String): Observable?
}