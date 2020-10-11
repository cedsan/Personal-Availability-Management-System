package isel.leic.ps.Exception


/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the exception theown whe a entity that was being requested is not
 * obtained.
 */
class EntityNotFoundException(msg: String) : Exception(msg) {

}