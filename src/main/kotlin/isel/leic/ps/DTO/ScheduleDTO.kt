package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents the base class of the hierarchy of the Schedule DTO.
 * It's used to allow having a different sub Schedule DTOs
 * with DALs with same operations according to their specific DTO type.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class ScheduleDTO (val type: String)