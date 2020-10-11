package isel.leic.ps.DAL.Interfaces


/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access Visibility entities
 * from a DataSource
 */
interface IVisibilityDAL {
    fun createVisibilityAssociation(observerID: Int, observableID: Int)
    fun deleteVisibilityAssociation(observerID: Int, observableID: Int)
    fun creatingPendingVisibilityAssocRequest(observerID: Int, observableID: Int)
    fun hasAssocBetween(observerID: Int, observableID: Int): Boolean
}