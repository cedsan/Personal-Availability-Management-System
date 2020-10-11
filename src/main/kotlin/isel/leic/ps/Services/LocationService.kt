package isel.leic.ps.Services

import isel.leic.ps.DAL.Interfaces.ILocationDAL
import isel.leic.ps.Entities.Location
import isel.leic.ps.Services.Interfaces.ILocationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the the business layer operations for the Location resource.
 */
@Service
class LocationService : ILocationService {

    val locationDAL: ILocationDAL

    @Autowired
    constructor(_locationDAL: ILocationDAL) {
        locationDAL = _locationDAL
    }

    override fun getLocationOfObservable(observableID: Int): Location {
        return locationDAL.getObservableLastLocation(observableID)
    }
}