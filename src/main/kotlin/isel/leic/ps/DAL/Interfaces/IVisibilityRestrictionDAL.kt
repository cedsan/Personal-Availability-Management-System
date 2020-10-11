package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.RestrictionVisibilityDTO
import isel.leic.ps.Entities.VisibilityRestriction

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access VisibilityRestriction entities
 * from a DataSource
 */
interface IVisibilityRestrictionDAL {
    fun updateVisibilityRestriction(observableID: Int, observerID: Int, updatedRestriction: RestrictionVisibilityDTO?)
    fun getVisibilityRestrictionOfObserverOnObservable(observerID: Int, observableID: Int): VisibilityRestriction
}