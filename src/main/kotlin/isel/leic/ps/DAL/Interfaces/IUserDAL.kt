package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.UserDTO
import isel.leic.ps.DTO.UserInfoDto
import isel.leic.ps.Domain.User.UserType

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies related operations to access User entities
 * from a DataSource
 */
interface IUserDAL {
    fun getUserType(userMail: String): UserInfoDto?
    fun hasUserWithEmail(userMail: String): Boolean
    fun createUser(userDto: UserDTO)
}