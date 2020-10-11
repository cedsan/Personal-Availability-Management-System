package isel.leic.ps.Entities

import java.sql.Timestamp

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class consists of the computational representation
 * of the DB Location Table
 */
data class Location(val id: Int, val latitude: Double, val longitude: Double, val isValid: Boolean, val lastUpdatedDateTime: Timestamp)