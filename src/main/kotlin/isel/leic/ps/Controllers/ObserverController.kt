package isel.leic.ps.Controllers

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.component.Link
import com.google.code.siren4j.converter.ReflectingConverter
import isel.leic.ps.Controllers.ObserverController.Companion.adaptSubscriptionToOutputModel
import isel.leic.ps.DTO.FirebaseNotifcationRegistrationParamsDTO
import isel.leic.ps.DTO.ObserverDTO
import isel.leic.ps.DTO.SubscriptionDTO
import isel.leic.ps.Domain.Schedule.ScheduleEnum
import isel.leic.ps.Domain.Subscription.SubscriptionEnum
import isel.leic.ps.Domain.Subscription.SubscriptionGetterFactory
import isel.leic.ps.Entities.*
import isel.leic.ps.Exception.EntityNotFoundException
import isel.leic.ps.OutputModel.*
import isel.leic.ps.OutputModel.Error.LocationNotFoundErrorOM
import isel.leic.ps.OutputModel.RepresentationHelper.ObserverRepresentationHelper
import isel.leic.ps.Services.Interfaces.*
import isel.leic.ps.Utils.HTTPConstants
import jdk.nashorn.internal.ir.annotations.Ignore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class defines the handler for the observer resource
 * requests.
 */
@RestController
@RequestMapping("/observer")
open class ObserverController {

    private val observerService: IObserverService
    private val subscriptionService: ISubscriptionService
    private val locationService: ILocationService
    private val visibilityService: IVisibilityService
    private val scheduleService: IScheduleService
    private val globalVisibilityRestrictionService: IGlobalVisibilityRestrictionService

    @Autowired
    constructor(_observerService: IObserverService, _subscribeService: ISubscriptionService, _locationService: ILocationService, _visibilityService: IVisibilityService,
                _scheduleService: IScheduleService, _globalVisibilityRestrictionService: IGlobalVisibilityRestrictionService) {
        observerService = _observerService
        subscriptionService = _subscribeService
        locationService = _locationService
        visibilityService = _visibilityService
        scheduleService = _scheduleService
        globalVisibilityRestrictionService = _globalVisibilityRestrictionService
    }

    companion object {
        private val subscriptionAdapterMapper: HashMap<SubscriptionEnum, (Subscription) -> SubscriptionOM>

        init {
            subscriptionAdapterMapper = HashMap<SubscriptionEnum, (Subscription) -> SubscriptionOM>()
            subscriptionAdapterMapper.put(SubscriptionEnum.WEEKDAY, this::adaptWeekdaySubscriptionToOutputModel)
            subscriptionAdapterMapper.put(SubscriptionEnum.DEFAULT, this::adaptDefaultSubscriptionToOutputModel)
        }
        fun adaptObserverToOutputModel(o: Observer) : ObserverOM {
            return ObserverOM(o.id,
                    o.name,
                    o.number,
                    o.email,
                    o.avatar_url)
        }

        fun adaptLocationToOutputModel(l: Location) : LocationOM {
            return LocationOM(l.id, l.latitude, l.longitude, l.lastUpdatedDateTime)
        }

        fun adaptSubscriptionToOutputModel(subsType: SubscriptionEnum, subscription: Subscription): SubscriptionOM {
            return subscriptionAdapterMapper[subsType]!!.invoke(subscription)
        }

        private fun adaptWeekdaySubscriptionToOutputModel(s: Subscription): SubscriptionOM {
            s as WeekDaySubscription
            return WeekDaySubscriptionOM(s.id, s.dayOfWeek, s.startWatchHour, s.startWatchMinutes, s.endWatchHour, s.endWatchMinutes, s.subscriptionType)
        }

        private fun adaptDefaultSubscriptionToOutputModel(s: Subscription): SubscriptionOM {
            s as DefaultSubscription
            return DefaultSubscriptionOM(s.id, s.subscriptionType)
        }
    }


    @RequestMapping(value = "", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    @ResponseBody
    fun getAllObservers(): HttpEntity<Entity> {
        val outputModelObservers = observerService.getAllObservers()
                .map(ObserverController.Companion::adaptObserverToOutputModel)
        outputModelObservers.forEach({ it.entityLinks = arrayListOf(ObserverRepresentationHelper.getObserverSelfLink(it)) })
        val outputObservableCollection = ObserverCollectionOutputModel(outputModelObservers)
        outputObservableCollection.entityActions = arrayListOf(ObserverRepresentationHelper.getAddNewObserverActionTogetAllObserverAction())

        return ResponseEntity<Entity>(ReflectingConverter.newInstance().toEntity(outputObservableCollection), HttpStatus.OK)
    }

    @RequestMapping(value = "/{id}", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    @ResponseBody
    fun getObserver(@PathVariable("id") id: Int): HttpEntity<Entity> {
        val observer: ObserverOM
        try {
            observer = adaptObserverToOutputModel(observerService.getObserverById(id))
        }catch (e: EntityNotFoundException) {
            return ResponseEntity.notFound().build()
        }

        /* Obtain the observer's observables to be embedded in the resource representation */
        val observables = observerService.getObsevablesObserverAlreadyHasSubscriptionByObserverId(id)
                .map (ObservableController.Companion::adaptObservableToOutputModel)
        observables.forEach { it.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableAssociatedWithObserverSelfLink(id, it.id)) }

        observer.observables = observables
        observer.entityLinks = arrayListOf(ObserverRepresentationHelper.getObserverSelfLink(observer), ObserverRepresentationHelper.getObserverAllObservablesWithVisibilityAssociation(id, ObserverRepresentationHelper.GET_OBSERVABLES_REL))
        observer.entityActions = arrayListOf(ObserverRepresentationHelper.getObserverRegistrationTokenPutAction(id))
        /* Generate the Entity representation */
        val observerEntity = ReflectingConverter.newInstance().toEntity(observer)

        return ResponseEntity<Entity>(observerEntity, HttpStatus.OK)
    }

    private fun  filterAlreadySubscribed(observable: Observable, observerId: Int): Boolean {
        return subscriptionService.getIsObserverSubscribedToObservable(observerId, observable.id)
    }

    // QUERY STRING -> [subscribed=true/false]
    @RequestMapping(value = "/{observerId}/observable", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getAllObservedOfObserverByObserverID(@PathVariable("observerId")observerID: Int, @RequestParam(name = "subscribed", required = false) subscribed: Boolean? = null): HttpEntity<Entity> {
        val observables = observerService.getAllObservableOfObserverByObserverID(observerID)
        val filteredObservables : Collection<Observable>
        if(subscribed != null)
            filteredObservables = (if(subscribed != null && subscribed) observables.filter { filterAlreadySubscribed(it, observerID) }   else observables.filter{ filterAlreadySubscribed(it, observerID).not() })
        else
            filteredObservables = observables

        var observablesOM = filteredObservables
                .map (ObservableController.Companion::adaptObservableToOutputModel)

        observablesOM.forEach { it.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableAssociatedWithObserverSelfLink(observerID, it.id)) }
        val collectionOfObservables = ObservableCollectionOutputModel(observablesOM)
        collectionOfObservables.entityLinks = arrayListOf(ObserverRepresentationHelper.getObserverAllObservablesWithVisibilityAssociation(observerID))
        return ResponseEntity<Entity>(ReflectingConverter.newInstance().toEntity(collectionOfObservables), HttpStatus.OK)
    }

    @RequestMapping(value = "/{observerId}/observable/{observedId}", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getObservableOfObserverByObserverId(@PathVariable("observerId")observerID: Int, @PathVariable("observedId")observedID: Int): HttpEntity<Entity> {
        var observable = observerService.getObservableOfObserverById(observerID, observedID)

        val globalVisibilityRestrictionOfObservable = globalVisibilityRestrictionService.getGlbVisibilityRestrictionConfigurations(observedID)
        val visibilityRestriction = observerService.getVisibilityRestrictionOfObservableOnObserver(observerID, observedID)

        if(visibilityRestriction.canSeeWhenAvailable.not().or(globalVisibilityRestrictionOfObservable.canSeeWhenAvailable.not()))
            observable = Observable(observable.id, observable.name, observable.number, observable.email, observable.avatar_url, false)

        val observableOutputModel = ObservableController.Companion.adaptObservableToOutputModel(observable)

        // According to the allowed visibility add link to entity resource
        observableOutputModel.entityLinks = ArrayList<Link>()
        observableOutputModel.entityLinks.add(ObserverRepresentationHelper.getObservableAssociatedWithObserverSelfLink(observerID, observedID))
        observableOutputModel.entityLinks.add(ObserverRepresentationHelper.getObservableFromObserverSubscriptionLink(observerID, observedID))
        if(visibilityRestriction.canSeeLocation.and(globalVisibilityRestrictionOfObservable.canSeeLocation))
            observableOutputModel.entityLinks.add(ObserverRepresentationHelper.getObservableFromObserverLocationLink(observerID,observedID))
        if(visibilityRestriction.canSeeSchedule.and(globalVisibilityRestrictionOfObservable.canSeeSchedule))
            observableOutputModel.entityLinks.add(ObserverRepresentationHelper.getObservableFromObserverSchedulesLink(observerID, observedID))


        val observableEntity = ReflectingConverter.newInstance().toEntity(observableOutputModel)

        return ResponseEntity.ok(observableEntity)
    }

    @RequestMapping(value = "/{observerID}/observable/{observableID}/location", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getObsersableOfObserverLocation(@PathVariable("observableID")observableID: Int, @PathVariable("observerID")observerID: Int): HttpEntity<Entity> {
        val visibilityRestriction = observerService.getVisibilityRestrictionOfObservableOnObserver(observerID, observableID)

        if(!visibilityRestriction.canSeeLocation)
            return ResponseEntity.noContent().build()

        val location = locationService.getLocationOfObservable(observableID)
        val locationOM = if (location.isValid) adaptLocationToOutputModel(location) else null

        if(location.isValid) {
            locationOM!!.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverLocationLink(observerID, observableID, isSelf = true))
            return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(locationOM))
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ReflectingConverter.newInstance().toEntity(LocationNotFoundErrorOM()))
    }

    @RequestMapping(value = "/{observerID}/observable/{observableID}/schedule", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getObsersableOfObserverScheduleCollection(@PathVariable("observableID")observableID: Int, @PathVariable("observerID")observerID: Int): HttpEntity<Entity> {
        if(!visibilityService.hasVisibilityOfObserverOnObservable(observerID, observableID))
            return ResponseEntity.notFound().build()

        val visibilityRestriction = observerService.getVisibilityRestrictionOfObservableOnObserver(observerID, observableID)
        if(!visibilityRestriction.canSeeSchedule)
            return ResponseEntity.noContent().build()

        val schedulesOM =  scheduleService.getAllSchedulesOfObservable(observableID)
                .map { ObservableController.adaptScheduleToOutputModel(it, ScheduleEnum.valueOf(it.scheduleType.toUpperCase())) }
        schedulesOM.forEach { it.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverScheduleLink(observerID, observableID, it)) }
        val schedulesCollectionOM = ScheduleCollectionOutputModel(schedulesOM)
        schedulesCollectionOM.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverSchedulesLink(observerID, observableID, true))

        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(schedulesCollectionOM))
    }

    @RequestMapping(value = "/{observerID}/observable/{observableID}/schedule/{scheduleId}", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getObsersableOfObserverSchedule(@PathVariable("observableID")observableID: Int, @PathVariable("observerID")observerID: Int, @PathVariable("scheduleId")scheduleId: Int,
                                        @RequestParam(name = "type", required = true) scheduleType: String): HttpEntity<Entity> {
        if(!visibilityService.hasVisibilityOfObserverOnObservable(observerID, observableID))
            return ResponseEntity.notFound().build()

        val visibilityRestriction = observerService.getVisibilityRestrictionOfObservableOnObserver(observerID, observableID)
        if(!visibilityRestriction.canSeeSchedule)
            return ResponseEntity.noContent().build()

        var scheduleEnumType = ScheduleEnum.valueOf(scheduleType.toUpperCase())
        val schedulesOM =  ObservableController.adaptScheduleToOutputModel(scheduleService.getScheduleById(scheduleId, scheduleEnumType), scheduleEnumType)
        schedulesOM.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverScheduleLink(observerID, observableID, schedulesOM))

        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(schedulesOM))
    }

    @RequestMapping(value = "/{observerID}/observable/{observableID}/subscription", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getSubscriptionsOfObserverOnObservable(@PathVariable("observableID")observableID: Int, @PathVariable("observerID")observerID: Int,
                                               @RequestParam(name = "type", required = false) subscriptionTypeToFilterBy: String? = null): HttpEntity<Entity> {
        var subscriptionEnumType = if(subscriptionTypeToFilterBy != null) SubscriptionEnum.valueOf(subscriptionTypeToFilterBy?.toUpperCase())
                                   else null
        val subscriptions = subscriptionService.getAllSubscriptionsOfObserverOnObservable(observerID, observableID, subscriptionEnumType)
        if(subscriptionEnumType != null && subscriptionEnumType.equals(SubscriptionEnum.DEFAULT)) {
            if(subscriptions.count() > 0) {
                val subscriptionOM = adaptSubscriptionToOutputModel(subscriptionEnumType, subscriptions.first())
                addSelfLinkToSubscription(observerID, observableID, subscriptionOM)
                subscriptionOM.entityActions = ObserverRepresentationHelper.getSubscriptionOfObserverOnObservableActions(observerID, observableID, subscriptionOM.id, subscriptionTypeToFilterBy!!)
                return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(subscriptionOM))
            }
            else
                return ResponseEntity.notFound().build()
        }
        else {
            val subscriptionsOM = subscriptions
            .map { subscriptionAdapterMapper[SubscriptionEnum.valueOf(it.subscriptionType.toUpperCase())]!!(it) }
            subscriptionsOM.forEach { addSelfLinkToSubscription(observerID, observableID, it) }
            val subscriptionCollectionOM = SubscriptionCollectionOutputModel(subscriptionsOM)
            subscriptionCollectionOM.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverSubscriptionLink(observerID, observableID, isSelf = true))
            subscriptionCollectionOM.entityActions = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverSubscriptionCollectionRemoveAllAction(observerID, observableID))
            subscriptionCollectionOM.entityActions.addAll(ObserverRepresentationHelper.getObservableFromObserverSubscriptionCollectionAddNewAction(observerID, observableID))
            return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(subscriptionCollectionOM))
        }
    }

    private fun addSelfLinkToSubscription(observerID: Int, observableID: Int, subs: SubscriptionOM) {
        subs.entityLinks = arrayListOf(ObserverRepresentationHelper.getObserverSubscriptionToObservableSelfLink(observerID, observableID, subs))
    }

    // QUERY STRING -> type=[weekday,default]
    @RequestMapping(value = "/{observerID}/observable/{observableID}/subscription/{subscriptionID}", method = arrayOf(RequestMethod.GET), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    fun getObsersableOfObserverSubscription(@PathVariable("subscriptionID")subscriptionID: Int, @PathVariable("observableID")observableID: Int, @PathVariable("observerID")observerID: Int,
                                            @RequestParam(name = "type", required = true) subscriptionType: String): HttpEntity<Entity> {
        // Get subscription type from query string
        val subscriptionType = SubscriptionEnum.valueOf(subscriptionType.toUpperCase())
        val subscriptionEntity = subscriptionService.getSubscriptionById(subscriptionID, subscriptionType)
        val subscriptionOM = adaptSubscriptionToOutputModel(subscriptionType, subscriptionEntity)
        addSelfLinkToSubscription(observerID, observableID, subscriptionOM)
        subscriptionOM.entityActions = ObserverRepresentationHelper.getSubscriptionOfObserverOnObservableActions(observerID, observableID, subscriptionID, subscriptionEntity.subscriptionType)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(subscriptionOM))
    }

    //********* POST ********//

    @RequestMapping(value = "/{observerId}/observable/{observedId}/subscription", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun addNewSubscriptionToObservable(@PathVariable("observerId")observerID: Int, @PathVariable("observedId")observedID: Int,
                              @RequestBody body: String?): ResponseEntity<Entity> {
        val om: ObjectMapper = ObjectMapper()

        val jn :JsonNode= om.readTree(body)
        val type = jn["type"].asText()


        val subscription: SubscriptionDTO

        // Apply the base subscription if non is passed
        if(type == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        val subscriptionType = SubscriptionEnum.valueOf(type.toUpperCase())

        subscription = SubscriptionGetterFactory.getSubscriptionGetter(subscriptionType)
                .getSubscription(body!!)

        try {
            subscriptionService.addSubscriptionFromObserverOnObservable(subscription, observerID, observedID, subscriptionType)
        } catch (e: IllegalArgumentException) {
            // Return json error indicating that subscription with same params
            // cannot be inserted
        }


        // And return the representation of the recently added subscription
        val subscriptionOM = adaptSubscriptionToOutputModel(subscriptionType, subscriptionService.getSubscriptionOfObserverOnObservableEqualsToReceived(subscription, observerID, observedID, subscriptionType))
        addSelfLinkToSubscription(observerID, observedID, subscriptionOM)
        subscriptionOM.entityActions = ObserverRepresentationHelper.getSubscriptionOfObserverOnObservableActions(observerID, observedID, subscriptionOM.id, subscriptionOM.type)
        return ResponseEntity
                .created(URI(ObserverRepresentationHelper.getObserverSubscriptionToObservableSelfLink(observerID, observedID, subscriptionOM)!!.href))
                .body(ReflectingConverter.newInstance().toEntity(subscriptionOM))
    }

    @RequestMapping(value = "", method = arrayOf(RequestMethod.POST))
    fun createObservable(@RequestBody body: String): HttpEntity<Entity> {
        // Receives in the body of the request the data to create the Observable entity
        val observerDTO = ObjectMapper().readValue<ObserverDTO>(body, ObserverDTO::class.java)
        // Parse it and delegate the request to the Service
        observerService.createObserver(observerDTO)
        // if no error in inserting the instance in the DB
        // return created
        return ResponseEntity.ok().build()
    }

    //***** DELETE *****//

    @RequestMapping(value = "/{observerID}/observable/{observableID}/subscription/{subscriptionID}", method = arrayOf(RequestMethod.DELETE), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun deleteSubscriptionObsersableOfObserver(@PathVariable("subscriptionID")subscriptionID: Int, @PathVariable("observableID")observableID: Int, @PathVariable("observerID")observerID: Int,
                                               @RequestParam(name = "type", required = true) type: String): HttpEntity<Entity> {
        var subscriptionType = SubscriptionEnum.valueOf(type.toUpperCase())
        subscriptionService.removeSubscriptionOfObserverOnObservableById(subscriptionID, observerID, observableID, subscriptionType)
        val subscriptions = subscriptionService.getAllSubscriptionsOfObserverOnObservable(observerID, observableID, SubscriptionEnum.WEEKDAY)
        val subscriptionsOM = subscriptions
                .map { subscriptionAdapterMapper[SubscriptionEnum.valueOf(it.subscriptionType.toUpperCase())]!!(it) }
        subscriptionsOM.forEach { addSelfLinkToSubscription(observerID, observableID, it) }
        val subscriptionCollectionOM = SubscriptionCollectionOutputModel(subscriptionsOM)
        subscriptionCollectionOM.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverSubscriptionLink(observerID, observableID, isSelf = true))
        subscriptionCollectionOM.entityActions = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverSubscriptionCollectionRemoveAllAction(observerID, observableID))
        subscriptionCollectionOM.entityActions.addAll(ObserverRepresentationHelper.getObservableFromObserverSubscriptionCollectionAddNewAction(observerID, observableID))
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(subscriptionCollectionOM))
    }

    @RequestMapping(value = "/{observerId}/observable/{observedId}/subscription", method = arrayOf(RequestMethod.DELETE), produces = arrayOf(HTTPConstants.MediaType.JSON_SIREN))
    @ResponseBody
    fun removeAllSubscriptionsOnObservable(@PathVariable("observerId")observerID: Int, @PathVariable("observedId")observableId: Int): HttpEntity<Entity> {
        subscriptionService.removeAllSubscriptionsOfObservableOnObservable(observerID, observableId)
        val subscriptions = subscriptionService.getAllSubscriptionsOfObserverOnObservable(observerID, observableId, SubscriptionEnum.WEEKDAY)
            val subscriptionsOM = subscriptions
                    .map { subscriptionAdapterMapper[SubscriptionEnum.valueOf(it.subscriptionType.toUpperCase())]!!(it) }
            subscriptionsOM.forEach { addSelfLinkToSubscription(observerID, observableId, it) }
            val subscriptionCollectionOM = SubscriptionCollectionOutputModel(subscriptionsOM)
            subscriptionCollectionOM.entityLinks = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverSubscriptionLink(observerID, observableId, isSelf = true))
            subscriptionCollectionOM.entityActions = arrayListOf(ObserverRepresentationHelper.getObservableFromObserverSubscriptionCollectionRemoveAllAction(observerID, observableId))
            subscriptionCollectionOM.entityActions.addAll(ObserverRepresentationHelper.getObservableFromObserverSubscriptionCollectionAddNewAction(observerID, observableId))
            return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(subscriptionCollectionOM))
    }


    //***** PUT *****//

    @RequestMapping(value = "/{observerId}/observable/{observedId}/subscription/{subscriptionID}", method = arrayOf(RequestMethod.PUT))
    @ResponseBody
    fun updateSubscriptionToObservable(@PathVariable("observerId")observerID: Int, @PathVariable("observedId")observedID: Int,
                                       @PathVariable("subscriptionID") subscriptionId: Int, @RequestBody body: String?): HttpEntity<Entity> {
        // Check which subscription strategy is being requested
        var om: ObjectMapper = ObjectMapper()

        var jn: JsonNode = om.readTree(body)
        var type = jn["type"].asText()

        var subscription: SubscriptionDTO

        if (type == null)   return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val subscriptionType = SubscriptionEnum.valueOf(type.toUpperCase())

        if (subscriptionType == SubscriptionEnum.DEFAULT) return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()

        subscription = SubscriptionGetterFactory.getSubscriptionGetter(subscriptionType)
                .getSubscription(body!!)

        try {
            subscriptionService.updateSubscription(subscription, subscriptionId, subscriptionType)
        } catch (e: IllegalArgumentException) {
            // Return json error indicating that subscription with same params
            // cannot be inserted
        }

        val subscriptionOM = adaptSubscriptionToOutputModel(subscriptionType, subscriptionService.getSubscriptionOfObserverOnObservableEqualsToReceived(subscription, observerID, observedID, subscriptionType))
        addSelfLinkToSubscription(observerID, observedID, subscriptionOM)
        return ResponseEntity.ok(ReflectingConverter.newInstance().toEntity(subscriptionOM))
    }

    @RequestMapping(value = "/{observerID}/notification/token", method = arrayOf(RequestMethod.PUT))
    fun submitFirabaseToken(@RequestBody body: String, @PathVariable("observerID")observerID: Int): HttpEntity<Entity> {
        val registrationParams = ObjectMapper().readValue(body, FirebaseNotifcationRegistrationParamsDTO::class.java) // The root json object received
        observerService.saveRegistrationToken(registrationParams)
        return ResponseEntity.ok().build()
    }

}

