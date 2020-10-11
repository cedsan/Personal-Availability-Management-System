package isel.leic.ps.Register.DAL

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Adds a new user in the IdentityProvider database configured
 */
interface IIDProviderUserCreater {

    fun createUser(username: String, password: String, name: String)
}