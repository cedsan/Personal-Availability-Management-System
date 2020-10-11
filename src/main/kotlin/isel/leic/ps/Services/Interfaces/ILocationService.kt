package isel.leic.ps.Services.Interfaces

import isel.leic.ps.Entities.Location

interface ILocationService {

    fun getLocationOfObservable(observableID: Int): Location
}