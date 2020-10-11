package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.Entities.RequestControlAccessParams

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access ControlAccessParameters entities
 * from a DataSource
 */
interface IRequestControlAccessParamsDAL {

    fun getAll(): Collection<RequestControlAccessParams>
}