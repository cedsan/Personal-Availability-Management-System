package isel.leic.ps.Services

import de.bytefish.fcmjava.model.options.FcmMessageOptions
import de.bytefish.fcmjava.responses.FcmMessageResponse
import isel.leic.ps.APINotification.FirebaseNotificationPusher
import isel.leic.ps.DAL.Interfaces.IAvailabityDetailsDAL
import isel.leic.ps.DAL.Interfaces.ILocationDAL
import isel.leic.ps.DAL.Interfaces.IObservableDAL
import isel.leic.ps.DAL.Interfaces.IVisibilityRestrictionDAL
import isel.leic.ps.DTO.LocationDTO
import isel.leic.ps.DTO.ObservableDTO
import isel.leic.ps.DTO.ScheduleDTO
import isel.leic.ps.DTO.notification.ObservableAvailableNotificationDTO
import isel.leic.ps.Domain.Schedule.ScheduleEnum
import isel.leic.ps.Entities.*
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer
import isel.leic.ps.Exception.EntityNotFoundException
import isel.leic.ps.Services.Helpers.ObservableHelper
import isel.leic.ps.Services.Interfaces.IObservableService
import isel.leic.ps.Services.Interfaces.IScheduleService
import isel.leic.ps.Services.Interfaces.ISubscriptionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.collections.HashMap

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the business layer operations for the Observable resource.
 */
@Service
class ObservableService : IObservableService {

    companion object {
        val scheduleTypeToIsInSchedulePredicate: HashMap<ScheduleEnum, (Schedule) -> Boolean> = HashMap()

        init {
            scheduleTypeToIsInSchedulePredicate.put(ScheduleEnum.WEEKDAY, this::isWeeklyScheduleInCurrentAvailabilityPeriod)
        }
        private fun isWeeklyScheduleInDayOfWeek(schedule: WeeklySchedule, dayOfWeek: Int): Boolean  {
            return schedule.dayOfWeek == ObservableHelper.EVERYDAY || schedule.dayOfWeek == dayOfWeek ||
                    (schedule.dayOfWeek >= Calendar.MONDAY && schedule.dayOfWeek <= Calendar.FRIDAY && dayOfWeek == ObservableHelper.MONDAY_UNTIL_FRIDAY) ||
                    (schedule.dayOfWeek == Calendar.SATURDAY || schedule.dayOfWeek == Calendar.SUNDAY && dayOfWeek == ObservableHelper.SATURDAY_UNTIL_SUNDAY)
        }

        private fun isWeeklyScheduleInAvailabilityInterval(schedule: WeeklySchedule, curHour: Int, curMinute: Int): Boolean {
            return schedule.startAvailabilityHour < curHour || (schedule.startAvailabilityHour ==  curHour &&
                    schedule.startAvailabilityMinute <= curMinute)
                    && (schedule.endAvailabilityHour > curHour ||  schedule.endAvailabilityHour == curHour && schedule.endAvailabilityMinute > curMinute)
        }

        private fun isWeeklyScheduleInCurrentAvailabilityPeriod(schedule: Schedule): Boolean {
            // Predicate in case contains a "Following" DefaultSubscription
            schedule as WeeklySchedule
            val cal = Calendar.getInstance()
            val curHour = cal[Calendar.HOUR_OF_DAY]
            val curMinute = cal[Calendar.MINUTE]
            val dayOfWeek = cal[Calendar.DAY_OF_WEEK]
            return (isWeeklyScheduleInDayOfWeek(schedule, dayOfWeek).and(isWeeklyScheduleInAvailabilityInterval(schedule, curHour, curMinute)))
        }
    }

    private val observableDAL: IObservableDAL
    private val locationDAL: ILocationDAL
    private val visibilityRestrictionDAL: IVisibilityRestrictionDAL
    private val subscriptionService: ISubscriptionService
    private val scheduleService: IScheduleService
    private val notificationPusher: FirebaseNotificationPusher
    private val availabilityDetailsDAL: IAvailabityDetailsDAL

    @Autowired
    constructor(_observableDAL: IObservableDAL, _locationDAL: ILocationDAL,
                _subscribeService: ISubscriptionService, _visibilityRestrictionDAL: IVisibilityRestrictionDAL,
                _scheduleService: IScheduleService, _notificationPusher: FirebaseNotificationPusher,
                _availabilityDetailsDAL: IAvailabityDetailsDAL) {
        observableDAL = _observableDAL
        locationDAL = _locationDAL
        visibilityRestrictionDAL = _visibilityRestrictionDAL
        availabilityDetailsDAL = _availabilityDetailsDAL

        /* Dependencies for notification purposes */
        scheduleService = _scheduleService
        subscriptionService = _subscribeService
        notificationPusher = _notificationPusher
    }

    override fun getAllObservable(): Collection<Observable> {
        return observableDAL.getAllObservable()
    }

    override fun getObservableById(id: Int): Observable {
        return observableDAL.getObservableById(id)
    }

    override fun getObservableByNumber(number: Int): Observable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createObservable(observableDTO: ObservableDTO): Unit {
        observableDAL.createObservable(observableDTO)
    }

    override fun getAllObserversOfObservable(observableId: Int): Collection<Observer> {
        return observableDAL.getAllObserversOfObservableById(observableId)
    }

    override fun updateLocation(id: Int, locationDTO: LocationDTO) {
        // Check if id corresponds to an Observable
        if(observableDAL.isIdOfObservable(id).not()) {
            throw IllegalArgumentException("No existing observable with the specified id")
        }

        locationDAL.updateObservableLocation(id, locationDTO)
    }

    override fun getObservableLocation(id: Int): Location {
        if(observableDAL.isIdOfObservable(id).not()) {
            throw IllegalArgumentException("No existing observable with the specified id")
        }
        return locationDAL.getObservableLastLocation(id)
    }

    override fun locationEntered(observableId: Int, locationDTO: LocationDTO) {
        if(observableDAL.isIdOfObservable(observableId).not()) {
            throw IllegalArgumentException("No existing observable with the specified id")
        }

        if(isInScheduleEnterTimeInLocation(observableId, locationDTO).not())
            return

        // Otherwise perform the update of the location
        locationDAL.updateObservableLocation(observableId, locationDTO)
        availabilityDetailsDAL.setAvailable(observableId)

        // And send the notification indicating the observable has entered a scheduled location
        // Schedule a thread to do this!!
        CompletableFuture.runAsync {
            performNotificationRoutine(observableId, locationDTO)
        }
    }

    override fun getObservableByEmail(username: String): Observable? {
        return observableDAL.getObservableByEmail(username)
    }

    private fun isInScheduleEnterTimeInLocation(observableId: Int, locationDTO: LocationDTO): Boolean {
        val observablesSchedulesInLocation = scheduleService.getAllSchedulesOfObservable(observableId).filter {
            scheduleLocationEqualToLocationDto(it, locationDTO)
        }.filter {
            isInAvailabilitySchedule(it)
        }

        return observablesSchedulesInLocation.isNotEmpty()
    }

    /**
     * Private method which performs the algorithm
     * to send notifications, after a Observable becomes available
     */
    private fun performNotificationRoutine(observableID: Int, locationDTO: LocationDTO?) {
        val observersWithVisibility = getAllObserversOfObservable(observableID)
        val observersToReceiveNotification = observersWithVisibility.filter {
            ObservableHelper.filterObserversWithNoRestriction(it, observableID, visibilityRestrictionDAL)
                    .and(ObservableHelper.filterObserversWithSubscriptionsAbleForNotification(observableID, it, subscriptionService))
        }
        if (observersToReceiveNotification.isEmpty()) return

        val observersRegistrationTokensForNotification = observersToReceiveNotification.map(Observer::registration_Token).filterNotNull()
                .toTypedArray()

        // If the observers don't have defined a token
        if (observersRegistrationTokensForNotification.isEmpty()) return

        val observable = getObservableById(observableID)

        val options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofHours(1))
                .build()

        notificationPusher.sendMessage(observersRegistrationTokensForNotification, observable.name + " became available", ObservableAvailableNotificationDTO(observable.id, observable.name, observable.avatar_url),
                options, this::handleFcmSendResponse)
    }

    private fun  isInAvailabilitySchedule(schedule: Schedule): Boolean {
        // According to the specific type of schedule
        return scheduleTypeToIsInSchedulePredicate[ScheduleEnum.valueOf(schedule.scheduleType.toUpperCase())]!!(schedule)
    }

    private fun  scheduleLocationEqualToLocationDto(schedule: Schedule, locationDTO: LocationDTO?): Boolean {
        return schedule.availabilityLocationLatitude == locationDTO!!.latitude && schedule.availabilityLocationLongitude == locationDTO.longitude
    }

    private fun handleFcmSendResponse(fcmMsgResp: FcmMessageResponse) {
        // Handle the response here
    }
}