package isel.leic.ps.Entities

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the model representation  for the Observer entity.
 */
class Observer(var id: Int,
               var name: String,
               var number: Int,
               var email: String,
               var avatar_url: String,
               val registration_Token: String?) {
}