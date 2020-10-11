package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GlobalRestrictionVisibilityUpdateDTO(val canSeeLocation: Boolean = true, val canSeeSchedule: Boolean = true, val canSeeWhenAvailable: Boolean = true)