package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.DTO.GlobalRestrictionVisibilityUpdateDTO
import isel.leic.ps.Entities.GlobalVisibilityRestrictionConfig


interface IGlobalVisibilityRestrictionDAL {
    fun getGlobalVisibilityRestriction(observableID: Int): GlobalVisibilityRestrictionConfig
    fun updateGlobalVisibilityRestriction(observableID: Int, updatedGlobalRestriction: GlobalRestrictionVisibilityUpdateDTO)
}