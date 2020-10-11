package isel.leic.ps.Exception

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * The exception instance that indicates when the
 * user that was being inserted already existed.
 */
class UserAlreadyExistsException : Exception("User with selected username already exists") {
}