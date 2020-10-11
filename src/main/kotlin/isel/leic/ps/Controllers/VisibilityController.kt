package isel.leic.ps.Controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.code.siren4j.component.Entity
import isel.leic.ps.DTO.CreateAssociationDTO
import isel.leic.ps.Services.Interfaces.IVisibilityService
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
 * This class defines the handler for the visibility resource
 * requests.
 */
@RestController
@RequestMapping("/visibility")
class VisibilityController {

    private val visibilityService: IVisibilityService

    @Autowired
    constructor(_visibilityService: IVisibilityService) {
        visibilityService = _visibilityService
    }

    @RequestMapping(value = "/assoc/{observerID}/{observableID}", method = arrayOf(RequestMethod.POST))
    fun createVisibilityAssociationBetween(@PathVariable("observerID") observerID: Int, @PathVariable("observableID") observableID: Int)
            : HttpEntity<Entity> {
        visibilityService.createVisibilityAssociation(observerID, observableID)
        return ResponseEntity.ok().build()
    }

    @RequestMapping(value = "/assoc", method = arrayOf(RequestMethod.POST))
    fun createVisibilityAssociationBetween(@RequestBody body: String)
            : HttpEntity<Entity> {
        val newAssocationDto = ObjectMapper().readValue(body, CreateAssociationDTO::class.java)
        visibilityService.createVisibilityAssociation(newAssocationDto)
        return ResponseEntity.ok().build()
    }

    @RequestMapping(value = "/assoc/{observerID}/{observableID}", method = arrayOf(RequestMethod.DELETE))
    fun removeVisibilityAssociationBetween(@PathVariable("observerID") observerID: Int, @PathVariable("observableID") observableID: Int)
            : HttpEntity<Entity> {
        visibilityService.deleteVisibilityAssociation(observerID, observableID)
        return ResponseEntity.ok().build()
    }

    // This endpoint will be used by services that tend to create a set of users and the Visibility associations
    // between them.
    @RequestMapping(value = "/assoc/request/{observerID}/{observableID}", method = arrayOf(RequestMethod.POST))
    fun createVisibilityRequestBetween(@PathVariable("observerID") observerID: Int, @PathVariable("observableID") observableID: Int)
            : HttpEntity<Entity> {
        visibilityService.creatingPendingVisibilityAssocRequest(observerID, observableID)
        return ResponseEntity.ok().build()
    }
}