package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.CreateAssociationDTO


interface IVisibilityService {
    fun createVisibilityAssociation(observerID: Int, observableID: Int): Boolean
    fun createVisibilityAssociation(visibilityAssociationDTO: CreateAssociationDTO): Boolean
    fun deleteVisibilityAssociation(observerID: Int, observableID: Int)
    fun creatingPendingVisibilityAssocRequest(observerID: Int, observableID: Int)
    fun hasVisibilityOfObserverOnObservable(observerID: Int, observableID: Int): Boolean
}