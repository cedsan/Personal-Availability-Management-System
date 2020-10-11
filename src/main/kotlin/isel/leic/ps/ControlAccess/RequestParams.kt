package isel.leic.ps.ControlAccess

import org.springframework.http.HttpMethod

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 */
class RequestParams(val method: HttpMethod,val path: String) {
}