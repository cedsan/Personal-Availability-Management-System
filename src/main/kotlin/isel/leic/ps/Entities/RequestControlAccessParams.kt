package isel.leic.ps.Entities

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the model representation  for the PathAccessControlParams entity.
 */
class RequestControlAccessParams(val method: String, val regex: String, val accessible_roles: Collection<String>)