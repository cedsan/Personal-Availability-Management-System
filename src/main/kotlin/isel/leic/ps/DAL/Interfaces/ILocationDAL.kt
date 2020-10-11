package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.LocationDTO
import isel.leic.ps.Entities.Location

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access Location entities
 * from a DataSource
 */
interface ILocationDAL {

    fun updateObservableLocation(observableId: Int, location: LocationDTO): Unit

    fun getObservableLastLocation(observableId: Int): Location

}