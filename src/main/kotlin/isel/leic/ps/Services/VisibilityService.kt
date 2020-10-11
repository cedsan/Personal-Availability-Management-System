package isel.leic.ps.Services

import isel.leic.ps.DAL.Interfaces.IObservableDAL
import isel.leic.ps.DAL.Interfaces.IVisibilityDAL
import isel.leic.ps.DTO.CreateAssociationDTO
import isel.leic.ps.Services.Interfaces.IObservableService
import isel.leic.ps.Services.Interfaces.IObserverService
import isel.leic.ps.Services.Interfaces.IVisibilityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the business layer operations for the Visibility resource.
 */
@Service
class VisibilityService: IVisibilityService {

    private val visibilityDAL: IVisibilityDAL
    private val observableService: IObservableService
    private val observerService: IObserverService

    @Autowired
    constructor(_visibilityDAL: IVisibilityDAL, _observableService: IObservableService, _observerService: IObserverService) {
        visibilityDAL = _visibilityDAL
        observableService = _observableService
        observerService = _observerService
    }
    override fun createVisibilityAssociation(observerID: Int, observableID: Int): Boolean {
        if (visibilityDAL.hasAssocBetween(observerID, observableID))
            return false
        visibilityDAL.createVisibilityAssociation(observerID, observableID)
        return true
    }

    override fun createVisibilityAssociation(visibilityAssociationDTO: CreateAssociationDTO): Boolean {
        val observer = observerService.getObserverByEmail(visibilityAssociationDTO.observerEmail)
        val observable = observableService.getObservableByEmail(visibilityAssociationDTO.observableEmail)
        if (observable == null || observer == null)
            throw IllegalArgumentException("The specified observer or observable id does not exits")
        if (visibilityDAL.hasAssocBetween(observer.id, observable.id))
            return false
        visibilityDAL.createVisibilityAssociation(observer.id, observable.id)
        return true
    }

    override fun deleteVisibilityAssociation(observerID: Int, observableID: Int) {
        visibilityDAL.deleteVisibilityAssociation(observerID, observableID)
    }

    override fun creatingPendingVisibilityAssocRequest(observerID: Int, observableID: Int) {
        visibilityDAL.creatingPendingVisibilityAssocRequest(observerID, observableID)
    }

    override fun hasVisibilityOfObserverOnObservable(observerID: Int, observableID: Int) = visibilityDAL.hasAssocBetween(observerID, observableID)
}