package isel.leic.ps.Controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import isel.leic.ps.ControlAccess.Role
import isel.leic.ps.DTO.UserDTO
import isel.leic.ps.OutputModel.ObserverOM
import isel.leic.ps.OutputModel.RepresentationHelper.ObservableRepresentationHelper
import isel.leic.ps.OutputModel.RepresentationHelper.ObserverRepresentationHelper
import isel.leic.ps.Services.Interfaces.IObservableService
import isel.leic.ps.Services.Interfaces.IObserverService
import isel.leic.ps.Services.Interfaces.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class defines the handler for the user creation
 * requests.
 */
@RestController
@RequestMapping(value= "/user")
class UserController {

    @Autowired
    lateinit private var userService: IUserService

    @Autowired
    lateinit private var observableService: IObservableService

    @Autowired
    lateinit private var observerService: IObserverService

    /**
     * Handler that returns JSON object containing the user type and
     * with the user mail.
     */
    @RequestMapping(value = "", method = arrayOf(RequestMethod.GET), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun getUserType(@RequestParam(value = "user", required = true) userMail: String?): HttpEntity<String> {
        val userType = userService.getUserType(userMail!!)
        if(userType == null)
            return ResponseEntity.notFound().build()

        return ResponseEntity.ok().body(ObjectMapper().writeValueAsString(userType))
    }


    /********** POST *********/

    @RequestMapping(value = "/register", method = arrayOf(RequestMethod.POST))
    fun registerNewUser(@RequestBody body: String): HttpEntity<Entity> {
        val userDto = ObjectMapper().readValue<UserDTO>(body, UserDTO::class.java)
        userService.registerNewUser(userDto)

        val userRegistrationType = Role.valueOf(userDto.userType.toUpperCase())
        val createdUserId : Int?
        var createdEntityOutputModel: Entity? = null

        when (userRegistrationType) {
            Role.OBSERVER -> {
                val observer: ObserverOM
                createdUserId = observerService.getObserverByEmail(userDto.username)!!.id
                observer = ObserverController.adaptObserverToOutputModel(observerService.getObserverById(createdUserId))
                val observables = observerService.getObsevablesObserverAlreadyHasSubscriptionByObserverId(createdUserId)
                        .map (ObservableController.Companion::adaptObservableToOutputModel)
                observables.forEach { it.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableAssociatedWithObserverSelfLink(createdUserId, it.id)) }

                observer.observables = observables
                /* Add the links */
                observer.entityLinks = arrayListOf(ObserverRepresentationHelper.getObserverSelfLink(observer), ObserverRepresentationHelper.getObserverAllObservablesWithVisibilityAssociation(createdUserId))

                createdEntityOutputModel = ReflectingConverter.newInstance().toEntity(observer)
            }

            Role.OBSERVABLE -> {
                createdUserId = observableService.getObservableByEmail(userDto.username)!!.id
                val observableOM = ObservableController.adaptObservableToOutputModel(observableService.getObservableById(createdUserId))

                observableOM.entityLinks = ObservableRepresentationHelper.getObservableLinks(observableOM)
                observableOM.entityActions = ObservableRepresentationHelper.getObservableActions(observableOM.id)
                createdEntityOutputModel = ReflectingConverter.newInstance().toEntity(observableOM)
            }

            Role.NONE -> {}
            Role.ADMIN -> {}
        }

        return ResponseEntity.ok(createdEntityOutputModel)
    }
}