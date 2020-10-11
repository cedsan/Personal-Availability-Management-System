package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.GlobalRestrictionVisibilityUpdateDTO
import isel.leic.ps.Entities.GlobalVisibilityRestrictionConfig


interface IGlobalVisibilityRestrictionService {
    fun getGlbVisibilityRestrictionConfigurations(observableID: Int): GlobalVisibilityRestrictionConfig
    fun updateGlobalVisibilityRestrictionConfigurations(observableID: Int, updatedRestriction: GlobalRestrictionVisibilityUpdateDTO?)
}