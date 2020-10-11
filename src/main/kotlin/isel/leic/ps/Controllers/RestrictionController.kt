package isel.leic.ps.Controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import isel.leic.ps.DTO.GlobalRestrictionVisibilityUpdateDTO
import isel.leic.ps.DTO.RestrictionVisibilityDTO
import isel.leic.ps.Entities.GlobalVisibilityRestrictionConfig
import isel.leic.ps.Entities.VisibilityRestriction
import isel.leic.ps.OutputModel.GlobalVisibilityRestrictionOM
import isel.leic.ps.OutputModel.RepresentationHelper.VisibilityRestrictionRepresentationHelper
import isel.leic.ps.OutputModel.VisibilityRestrictionOM
import isel.leic.ps.Services.Interfaces.IGlobalVisibilityRestrictionService
import isel.leic.ps.Services.Interfaces.IRestrictionService
import isel.leic.ps.Utils.HTTPConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class represents the controller for the
 * Observable information access restriction creation requests.
 */
@RestController
@RequestMapping("/observable")
class RestrictionController {

    private val restrictionService: IRestrictionService
    private val globalVisibilityRestrictionService: IGlobalVisibilityRestrictionService


    @Autowired
    constructor(_restrictionService: IRestrictionService, _globalVisibilityRestrictionService: IGlobalVisibilityRestrictionService) {
        restrictionService = _restrictionService
        globalVisibilityRestrictionService = _globalVisibilityRestrictionService
    }

    companion object {
        fun adaptVisibilityRestrictionToOutputModel(vr: VisibilityRestriction): VisibilityRestrictionOM {
            return VisibilityRestrictionOM(vr.canSeeWhenAvailable, vr.canSeeSchedule, vr.canSeeLocation)
        }

        fun adaptGlobalVisibilityRestrictionOutputModel(gvr: GlobalVisibilityRestrictionConfig): GlobalVisibilityRestrictionOM {
            return GlobalVisibilityRestrictionOM(gvr.canSeeWhenAvailable, gvr.canSeeSchedule, gvr.canSeeLocation)
        }
    }

    @RequestMapping(value = "/{id}/observer/{observerID}/restriction", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getVisibilityRestriction(@PathVariable("id")observableID: Int, @PathVariable("observerID")observerID: Int): HttpEntity<Entity> {
        val visibRestrictionOM = adaptVisibilityRestrictionToOutputModel(restrictionService.getVisibilityRestrictionOfObservableOnObsever(observableID, observerID))
        visibRestrictionOM.entityLinks = arrayListOf(VisibilityRestrictionRepresentationHelper.getVisibilityRestrictionOfObservableOnObserverSelfLink(observableID, observerID))
        visibRestrictionOM.entityActions = arrayListOf(VisibilityRestrictionRepresentationHelper.getUpdateObservableVisibilityRestrcitionOnObservableAction(observableID, observerID))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(visibRestrictionOM))
    }

    @RequestMapping(value = "/{id}/global-vis-restriction-config", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getGlobalVisibilityConfig(@PathVariable("id")observableID: Int): HttpEntity<Entity> {
        val gvrOM = adaptGlobalVisibilityRestrictionOutputModel(globalVisibilityRestrictionService.getGlbVisibilityRestrictionConfigurations(observableID))
        gvrOM.entityLinks = arrayListOf(VisibilityRestrictionRepresentationHelper.getObservableGlobalVisibilityRestrictionLink(observableID))
        gvrOM.entityActions = arrayListOf(VisibilityRestrictionRepresentationHelper.getUpdateObservableGlobalVisibilityRestrcitionAction(observableID))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(gvrOM))
    }

    @RequestMapping(value = "/{id}/observer/{observerID}/restriction", method = arrayOf(RequestMethod.PUT), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun updateRestrictionVisibility(@PathVariable("id")observableID: Int, @PathVariable("observerID")observerID: Int,
                                    @RequestBody body: String?): HttpEntity<Entity> {
        val om: ObjectMapper = ObjectMapper()
        val updatedRestriction = om.readValue(body, RestrictionVisibilityDTO::class.java)
        restrictionService.updateVisibilityRestriction(observableID, observerID, updatedRestriction)

        val visibRestrictionOM = adaptVisibilityRestrictionToOutputModel(restrictionService.getVisibilityRestrictionOfObservableOnObsever(observableID, observerID))
        visibRestrictionOM.entityLinks = arrayListOf(VisibilityRestrictionRepresentationHelper.getVisibilityRestrictionOfObservableOnObserverSelfLink(observableID, observerID))
        visibRestrictionOM.entityActions = arrayListOf(VisibilityRestrictionRepresentationHelper.getUpdateObservableVisibilityRestrcitionOnObservableAction(observableID, observerID))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(visibRestrictionOM))
    }

    @RequestMapping(value = "/{id}/global-vis-restriction-config", method = arrayOf(RequestMethod.PUT), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun updateGlobalRestrictionVisibility(@PathVariable("id")observableID: Int, @RequestBody body: String?): HttpEntity<Entity> {
        val om: ObjectMapper = ObjectMapper()
        val updatedRestriction = om.readValue(body, GlobalRestrictionVisibilityUpdateDTO::class.java)
        globalVisibilityRestrictionService.updateGlobalVisibilityRestrictionConfigurations(observableID, updatedRestriction)

        val gvrOM = adaptGlobalVisibilityRestrictionOutputModel(globalVisibilityRestrictionService.getGlbVisibilityRestrictionConfigurations(observableID))
        gvrOM.entityLinks = arrayListOf(VisibilityRestrictionRepresentationHelper.getObservableGlobalVisibilityRestrictionLink(observableID))
        gvrOM.entityActions = arrayListOf(VisibilityRestrictionRepresentationHelper.getUpdateObservableGlobalVisibilityRestrcitionAction(observableID))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(gvrOM))
    }
}