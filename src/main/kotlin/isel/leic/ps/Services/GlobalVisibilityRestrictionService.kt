package isel.leic.ps.Services

import isel.leic.ps.DAL.Interfaces.IGlobalVisibilityRestrictionDAL
import isel.leic.ps.DTO.GlobalRestrictionVisibilityUpdateDTO
import isel.leic.ps.Entities.GlobalVisibilityRestrictionConfig
import isel.leic.ps.Services.Interfaces.IGlobalVisibilityRestrictionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the the business layer operations for the GlobalVisibilityRestriction resource.
 */
@Service
class GlobalVisibilityRestrictionService: IGlobalVisibilityRestrictionService {

    private val globalVisibilityRestrictionDAL: IGlobalVisibilityRestrictionDAL


    @Autowired
    constructor(_globalVisibilityRestrictionDAL: IGlobalVisibilityRestrictionDAL) {
        globalVisibilityRestrictionDAL = _globalVisibilityRestrictionDAL
    }

    override fun getGlbVisibilityRestrictionConfigurations(observableID: Int): GlobalVisibilityRestrictionConfig {
        return globalVisibilityRestrictionDAL.getGlobalVisibilityRestriction(observableID)
    }

    override fun updateGlobalVisibilityRestrictionConfigurations(observableID: Int, updatedGlobalRestriction: GlobalRestrictionVisibilityUpdateDTO?) {
        if(updatedGlobalRestriction == null) {
            throw NullPointerException()
        }

        globalVisibilityRestrictionDAL.updateGlobalVisibilityRestriction(observableID, updatedGlobalRestriction)
    }
}