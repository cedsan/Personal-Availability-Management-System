package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.ControlAccess.Role

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies operations to access UserRole entities
 * from a DataSource
 */
interface IUserRoleDAL {

    fun getUserRoles(user_id: String): Collection<Role>
}