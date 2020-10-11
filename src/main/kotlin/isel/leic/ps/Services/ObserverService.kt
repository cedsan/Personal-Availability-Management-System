package isel.leic.ps.Services

import isel.leic.ps.DAL.Interfaces.IObserverDAL
import isel.leic.ps.DAL.Interfaces.IVisibilityRestrictionDAL
import isel.leic.ps.DTO.FirebaseNotifcationRegistrationParamsDTO
import isel.leic.ps.DTO.ObserverDTO
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer
import isel.leic.ps.Entities.VisibilityRestriction
import isel.leic.ps.Exception.EntityNotFoundException
import isel.leic.ps.Services.Interfaces.IObserverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the business layer operations for the Observer resource.
 */
@Service
class ObserverService : IObserverService {

    private val observerDAL: IObserverDAL
    private val visibilityRestrictionDAL: IVisibilityRestrictionDAL

    @Autowired
    constructor(_observerDAL: IObserverDAL, _visibilityRestrictionDAL: IVisibilityRestrictionDAL) {
        observerDAL = _observerDAL
        visibilityRestrictionDAL = _visibilityRestrictionDAL
    }

    override fun getAllObservers():Collection<Observer> {
        return observerDAL.getAllObservers()
    }

    override fun getObserverById(id: Int): Observer {
        return observerDAL.getObserverById(id)
    }

    override fun getObserverByNumber(number: Int): Observer {
        return observerDAL.getObserverByNumber(number)
    }

    override fun createObserver(observerDTO: ObserverDTO): Observer {
        observerDAL.createObserver(observerDTO)
        return observerDAL.getObserverByNumber(observerDTO.number)
    }

    override fun getAllObservableOfObserverByObserverID(observerID: Int): Collection<Observable> {
        // Get from ObserverDAL all the Observable
        return observerDAL.getAllObservedOfObserverByObserverID(observerID)
    }

    override fun getObservableOfObserverById(observerID: Int, observedID: Int): Observable {
        // Get the specific Observable of the Observer from the DAL
        return observerDAL.getObservableOfObserverById(observerID, observedID)
    }

    override fun saveRegistrationToken(registrationParams: FirebaseNotifcationRegistrationParamsDTO?) {
        observerDAL.updateObserverRegistrationToken(registrationParams)
    }

    override fun getObsevablesObserverAlreadyHasSubscriptionByObserverId(observerID: Int): Collection<Observable> {
        return observerDAL.getObsevablesObserverAlreadyHasSubscription(observerID)
    }

    override fun getVisibilityRestrictionOfObservableOnObserver(observerId: Int, observableId: Int): VisibilityRestriction {
        return visibilityRestrictionDAL.getVisibilityRestrictionOfObserverOnObservable(observerId, observableId)
    }

    override fun getObserverByEmail(username: String): Observer? {
        return observerDAL.getObserverByEmail(username)!!
    }
}