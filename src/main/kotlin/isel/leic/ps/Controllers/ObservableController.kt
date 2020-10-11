package isel.leic.ps.Controllers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import isel.leic.ps.DTO.LocationDTO
import isel.leic.ps.DTO.ObservableDTO
import isel.leic.ps.DTO.WeeklyScheduleDTO
import isel.leic.ps.Domain.Schedule.ScheduleEnum
import isel.leic.ps.Domain.Schedule.ScheduleGetterFactory
import isel.leic.ps.Entities.AvailabilityDetail
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Schedule
import isel.leic.ps.Entities.WeeklySchedule
import isel.leic.ps.OutputModel.*
import isel.leic.ps.OutputModel.RepresentationHelper.ObservableRepresentationHelper
import isel.leic.ps.Services.Interfaces.*
import isel.leic.ps.Utils.HTTPConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class defines the handler for the observable resource
 * requests.
 */
@RestController
@RequestMapping(value= "/observable")
class ObservableController {

    private val observableService: IObservableService
    private val subscriptionService: ISubscriptionService
    private val observerService: IObserverService
    private val scheduleService: IScheduleService
    private val availabilityService: IAvailabilityService

    companion object {
        private val scheduleAdapterMapper: HashMap<ScheduleEnum, (Schedule) -> ScheduleOM>

        init {
            scheduleAdapterMapper = HashMap()
            scheduleAdapterMapper.put(ScheduleEnum.WEEKDAY, this::adaptWeeklyScheduleToOutputModel)
        }

        fun adaptObservableToOutputModel(o: Observable) : ObservableOM {
            return ObservableOM(o.id,
                    o.number,
                    o.name,
                    o.email,
                    o.avatar_url,
                    o.hasAvailability)
        }

        fun adaptAvailabilityDetailToOutputModel(ad: AvailabilityDetail): AvailabilityDetailOM {
            return AvailabilityDetailOM(ad.isAvailable)
        }

        fun adaptWeeklyScheduleToOutputModel(s: Schedule): WeeklyScheduleOM {
            s as WeeklySchedule
            return WeeklyScheduleOM(s.id, s.dayOfWeek, s.startAvailabilityHour, s.startAvailabilityMinute, s.endAvailabilityHour, s.endAvailabilityMinute, s.availabilityLocationLatitude, s.availabilityLocationLongitude, s.isLocationValid, s.scheduleType)
        }

        fun adaptScheduleToOutputModel(s: Schedule, scheduleType: ScheduleEnum): ScheduleOM {
            return scheduleAdapterMapper[scheduleType]!!(s)
        }
    }

    @Autowired()
    constructor(_observableService: IObservableService, _subscribeService: ISubscriptionService, _observerService: IObserverService,
                _scheduleService: IScheduleService, _availabilityService: IAvailabilityService) {
        observableService = _observableService
        subscriptionService = _subscribeService
        observerService = _observerService
        scheduleService = _scheduleService
        availabilityService = _availabilityService
    }


    @RequestMapping(value = "", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getAllObservables(): HttpEntity<Entity> {
        val observablesOM = observableService.getAllObservable().map(Companion::adaptObservableToOutputModel)
        observablesOM.forEach({ it.entityLinks = arrayListOf(ObservableRepresentationHelper.getObservableSelfLink(it)) })
        val observableCollectionOM = ObservableCollectionOutputModel(observablesOM)
        observableCollectionOM.entityActions = arrayListOf(ObservableRepresentationHelper.getAddNewObservableAction())
        observableCollectionOM.entityLinks = arrayListOf(ObservableRepresentationHelper.getAllObservableSelfLink())
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(observableCollectionOM))
    }

    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getObservableById(@PathVariable("id") id: Int): HttpEntity<Entity> {
        val observableOM = adaptObservableToOutputModel(observableService.getObservableById(id))
        observableOM.entityLinks = ObservableRepresentationHelper.getObservableLinks(observableOM)
        observableOM.entityActions = ObservableRepresentationHelper.getObservableActions(observableOM.id)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(observableOM))
    }

    @RequestMapping(value = "/{id}/observer", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getAllObserversOfObservable(@PathVariable("id") id: Int): HttpEntity<Entity> {
        val observersOM = observableService.getAllObserversOfObservable(id)
            .map(ObserverController.Companion::adaptObserverToOutputModel)
        observersOM.forEach {
            addSelfLinkToObserverDetailsOnObservable(id, it)
        }
        val observerCollectionOM = ObserverCollectionOutputModel(observersOM)
        observerCollectionOM.entityLinks = arrayListOf(ObservableRepresentationHelper.getCollectionObserversOfObservableSelfLink(id))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(observerCollectionOM))
    }

    @RequestMapping(value = "/{id}/observer/{observerID}", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getObserverDetails(@PathVariable("id")observableID: Int, @PathVariable("observerID")observerID: Int): HttpEntity<Entity> {
        val observerOM = ObserverController.adaptObserverToOutputModel(observerService.getObserverById(observerID))
        observerOM.entityLinks = arrayListOf(ObservableRepresentationHelper.getVisibilityRestrictionOfObservableOnObserverLink(observableID, observerID),
                ObservableRepresentationHelper.getObservableObserverSelfLink(observableID, observerID))

        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(observerOM))
    }

    @RequestMapping(value = "/{id}/location", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getLocationOfObservable(@PathVariable("id") id: Int): HttpEntity<Entity> {
        val locationOM = ObserverController.adaptLocationToOutputModel(observableService.getObservableLocation(id))
        locationOM.entityActions = ObservableRepresentationHelper.getSendObservableLocationAction(locationOM, id)
        locationOM.entityLinks = arrayListOf(ObservableRepresentationHelper.getObservableLocationSelfLink(id))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(locationOM))
    }

    @RequestMapping(value = "/{id}/schedule", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getAllSchedulesOfObservable(@PathVariable("id") id: Int): HttpEntity<Entity> {
        val schedulesOM = scheduleService.getAllSchedulesOfObservable(id)
                .map { scheduleAdapterMapper[ScheduleEnum.valueOf(it.scheduleType.toUpperCase())]!!(it) }
        schedulesOM.forEach { addSelfLinkToSchedule(id, it) }
        val scheduleCollectionOM = ScheduleCollectionOutputModel(schedulesOM)
        scheduleCollectionOM.entityLinks = arrayListOf(ObservableRepresentationHelper.getObservableScheduleCollectionSelfLink(id))
        scheduleCollectionOM.entityActions = arrayListOf(ObservableRepresentationHelper.getObservableScheduleRemoveAllAction(id))
        scheduleCollectionOM.entityActions.addAll(ObservableRepresentationHelper.getObservableScheduleAddAction(id))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(scheduleCollectionOM))
    }

    @RequestMapping(value = "/{id}/schedule/{scheduleId}", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getScheduleOfObservable(@PathVariable("scheduleId")scheduleId: Int, @PathVariable("id") id: Int,
                                @RequestParam(name = "type", required = true) scheduleType: String): HttpEntity<Entity> {
        val scheduleEnumType = ScheduleEnum.valueOf(scheduleType.toUpperCase())
        val scheduleOM: ScheduleOM = scheduleAdapterMapper[scheduleEnumType]!!(scheduleService.getScheduleById(scheduleId, scheduleEnumType))
        addSelfLinkToSchedule(id, scheduleOM)
        scheduleOM.entityActions = arrayListOf(ObservableRepresentationHelper.getObservableScheduleRemoveByIdAction(id, scheduleOM.id, scheduleEnumType))
        scheduleOM.entityActions.add(ObservableRepresentationHelper.getObservableScheduleUpdateAction(id, scheduleOM.id, scheduleEnumType))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(scheduleOM))
    }

    private fun addSelfLinkToSchedule(observableId: Int, schedule: ScheduleOM) {
        schedule.entityLinks = arrayListOf(ObservableRepresentationHelper.getObservableScheduleSelfLink(observableId, schedule))
    }

    private fun addSelfLinkToObserverDetailsOnObservable(observableId: Int, observerOM: ObserverOM) {
        observerOM.entityLinks = arrayListOf(ObservableRepresentationHelper.getObservableObserverSelfLink(observableId, observerOM))
    }



    /***** POST *****/

    @RequestMapping(value = "/{id}/location", method = arrayOf(RequestMethod.POST))
    fun sendCurrentLocation(@PathVariable("id") id: Int, @RequestBody body: String): HttpEntity<Entity> {
        val locationDTO = ObjectMapper().readValue<LocationDTO>(body, LocationDTO::class.java)
        observableService.updateLocation(id, locationDTO)

        return ResponseEntity.ok().build()
    }

    @RequestMapping(value = "/{observableId}/schedule", method = arrayOf(RequestMethod.POST))
    fun addNewSchedule(@PathVariable("observableId") observableId: Int, @RequestBody body: String): HttpEntity<Entity> {
        val om: ObjectMapper = ObjectMapper()

        val jn : JsonNode = om.readTree(body)
        val type = jn["type"].asText()

        // Apply the base subscription if none is passed
        if(type == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        val scheduleType = ScheduleEnum.valueOf(type.toUpperCase())

        val scheduleDTO = ScheduleGetterFactory.getScheduleGetter(scheduleType)
                .getSchedule(body)

        scheduleService.addObservableSchedule(observableId, scheduleDTO, scheduleType)
        val scheduleOM = scheduleAdapterMapper[scheduleType]!!(scheduleService.getLastAddedSchedule(observableId, scheduleType))
        addSelfLinkToSchedule(observableId, scheduleOM)
        scheduleOM.entityActions = arrayListOf(ObservableRepresentationHelper.getObservableScheduleRemoveByIdAction(observableId, scheduleOM.id, scheduleType))
        scheduleOM.entityActions.add(ObservableRepresentationHelper.getObservableScheduleUpdateAction(observableId, scheduleOM.id, scheduleType))

        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(scheduleOM))
    }

    @RequestMapping(value = "", method = arrayOf(RequestMethod.POST))
    fun createObservable(@RequestBody body: String): HttpEntity<Entity> {
        val observedDTO = ObjectMapper().readValue<ObservableDTO>(body, ObservableDTO::class.java)
        observableService.createObservable(observedDTO)

        val createdObservable = observableService.getObservableByEmail(observedDTO.email)
        return ResponseEntity.created(URI(ObservableRepresentationHelper.getObservableSelfLink(createdObservable!!.id).href)).build()
    }

    @RequestMapping(value = "/{id}/unavailable", method = arrayOf(RequestMethod.POST))
    fun changeAvailabilityToUnavailable(@PathVariable("id") observableId: Int): HttpEntity<Entity> {
        availabilityService.setUnavailable(observableId)
        val observableAvailabilityDetailOM = adaptAvailabilityDetailToOutputModel(availabilityService.getAvailability(observableId))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(observableAvailabilityDetailOM))
    }

    @RequestMapping(value = "/{id}/available", method = arrayOf(RequestMethod.POST))
    fun changeAvailabilityToAvailable(@PathVariable("id") observableId: Int): HttpEntity<Entity> {
        availabilityService.setAvailable(observableId)
        val observableAvailabilityDetailOM = adaptAvailabilityDetailToOutputModel(availabilityService.getAvailability(observableId))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(observableAvailabilityDetailOM))
    }

    @RequestMapping(value = "/{id}/entered-location", method = arrayOf(RequestMethod.POST))
    fun enteredScheduledLocation(@PathVariable("id") id: Int, @RequestBody body: String): HttpEntity<Entity> {
        val locationDTO = ObjectMapper().readValue<LocationDTO>(body, LocationDTO::class.java)
        observableService.locationEntered(id, locationDTO)
        val observableAvailabilityDetailOM = adaptAvailabilityDetailToOutputModel(availabilityService.getAvailability(id))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(observableAvailabilityDetailOM))
    }

    /***** PUT *****/

    @RequestMapping(value = "/{id}/schedule/{scheduleID}", method = arrayOf(RequestMethod.PUT))
    fun updateSchedule(@PathVariable("id") id: Int, @PathVariable("scheduleID") scheduleID: Int, @RequestBody body: String): HttpEntity<Entity> {
        val om: ObjectMapper = ObjectMapper()

        val jn : JsonNode = om.readTree(body)
        val type = jn["type"].asText()

        // Apply the base subscription if none is passed
        if(type == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        val scheduleType = ScheduleEnum.valueOf(type.toUpperCase())

        val scheduleDTO = ScheduleGetterFactory.getScheduleGetter(scheduleType)
                .getSchedule(body)

        scheduleService.updateSchedule(id, scheduleID, scheduleDTO, scheduleType)

        val scheduleOM = scheduleAdapterMapper[scheduleType]!!(scheduleService.getScheduleById(scheduleID, scheduleType))
        addSelfLinkToSchedule(id, scheduleOM)
        scheduleOM.entityActions = arrayListOf(ObservableRepresentationHelper.getObservableScheduleRemoveByIdAction(id, scheduleOM.id, scheduleType))
        scheduleOM.entityActions.add(ObservableRepresentationHelper.getObservableScheduleUpdateAction(id, scheduleOM.id, scheduleType))

        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(scheduleOM))
    }

    /***** DELETE *****/

    @RequestMapping(value = "/{id}/schedule", method = arrayOf(RequestMethod.DELETE))
    fun deleteAllSchedule(@PathVariable("id") observableId: Int): HttpEntity<Entity> {
        scheduleService.removeAllSchedule(observableId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @RequestMapping(value = "/{id}/schedule/{scheduleID}", method = arrayOf(RequestMethod.DELETE), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun deleteSchedule(@PathVariable("id") id: Int, @PathVariable("scheduleID") scheduleID: Int, @RequestParam(name = "type", required = true)scheduleType: String): HttpEntity<Entity> {
        scheduleService.removeSchedule(scheduleID, ScheduleEnum.valueOf(scheduleType.toUpperCase()))
        //return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"state\" : \"Deleted\"}")
        val schedulesOM = scheduleService.getAllSchedulesOfObservable(id)
                .map { scheduleAdapterMapper[ScheduleEnum.valueOf(it.scheduleType.toUpperCase())]!!(it) }
        schedulesOM.forEach { addSelfLinkToSchedule(id, it) }
        val scheduleCollectionOM = ScheduleCollectionOutputModel(schedulesOM)
        scheduleCollectionOM.entityLinks = arrayListOf(ObservableRepresentationHelper.getObservableScheduleCollectionSelfLink(id))
        scheduleCollectionOM.entityActions = arrayListOf(ObservableRepresentationHelper.getObservableScheduleRemoveAllAction(id))
        scheduleCollectionOM.entityActions.addAll(ObservableRepresentationHelper.getObservableScheduleAddAction(id))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(scheduleCollectionOM))
    }
}