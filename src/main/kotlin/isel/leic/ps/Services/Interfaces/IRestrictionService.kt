package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.RestrictionVisibilityDTO
import isel.leic.ps.Entities.VisibilityRestriction


interface IRestrictionService {
    fun updateVisibilityRestriction(observableID: Int, observerID: Int, updatedRestriction: RestrictionVisibilityDTO?)
    fun getVisibilityRestrictionOfObservableOnObsever(observableID: Int, observerID: Int): VisibilityRestriction
}