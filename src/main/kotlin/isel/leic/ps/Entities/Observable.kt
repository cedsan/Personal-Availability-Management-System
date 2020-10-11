package isel.leic.ps.Entities

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the model representation  for the Observable entity.
 */
data class Observable(val id: Int,
                      val name: String,
                      val number: Int,
                      val email: String,
                      val avatar_url: String,
                      val hasAvailability: Boolean) {

}