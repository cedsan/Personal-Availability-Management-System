package isel.leic.ps.Services

import isel.leic.ps.DAL.Interfaces.IVisibilityDAL
import isel.leic.ps.DAL.Interfaces.IVisibilityRestrictionDAL
import isel.leic.ps.DTO.RestrictionVisibilityDTO
import isel.leic.ps.Entities.VisibilityRestriction
import isel.leic.ps.Exception.NoAssociationBetweenException
import isel.leic.ps.Services.Interfaces.IRestrictionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the business layer operations for the VisibilityRestriction resource.
 */
@Service
class VisibilityRestrictionService: IRestrictionService {

    private val visibilityRestrictionDAL: IVisibilityRestrictionDAL
    private val visibilityDAL: IVisibilityDAL

    @Autowired
    constructor(_visibilityRestrictionDAL: IVisibilityRestrictionDAL, _visibilityDAL: IVisibilityDAL) {
        visibilityRestrictionDAL = _visibilityRestrictionDAL
        visibilityDAL = _visibilityDAL
    }

    override fun updateVisibilityRestriction(observableID: Int, observerID: Int, updatedRestriction: RestrictionVisibilityDTO?) {
        if(!visibilityDAL.hasAssocBetween(observerID, observableID))
            throw NoAssociationBetweenException("No visibility association existing between the specified observable and observer")
        visibilityRestrictionDAL.updateVisibilityRestriction(observableID, observerID, updatedRestriction)
    }

    override fun getVisibilityRestrictionOfObservableOnObsever(observableID: Int, observerID: Int): VisibilityRestriction {
        return visibilityRestrictionDAL.getVisibilityRestrictionOfObserverOnObservable(observerID, observableID)
    }
}