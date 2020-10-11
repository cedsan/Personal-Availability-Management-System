package isel.leic.ps.Services

import de.bytefish.fcmjava.model.options.FcmMessageOptions
import de.bytefish.fcmjava.responses.FcmMessageResponse
import isel.leic.ps.APINotification.Constants
import isel.leic.ps.APINotification.FirebaseNotificationPusher
import isel.leic.ps.DAL.Interfaces.IAvailabityDetailsDAL
import isel.leic.ps.DAL.Interfaces.IObservableDAL
import isel.leic.ps.DAL.Interfaces.IVisibilityRestrictionDAL
import isel.leic.ps.DTO.notification.BaseNotificationDTO
import isel.leic.ps.DTO.notification.ObservableAvailableNotificationDTO
import isel.leic.ps.DTO.notification.ObservableUnavailableNotificationDTO
import isel.leic.ps.Entities.AvailabilityDetail
import isel.leic.ps.Entities.Observable
import isel.leic.ps.Entities.Observer
import isel.leic.ps.Services.Helpers.ObservableHelper
import isel.leic.ps.Services.Interfaces.IAvailabilityService
import isel.leic.ps.Services.Interfaces.ISubscriptionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.CompletableFuture

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the the business layer operations for the Availability resource.
 */
@Service
class AvailabilityService: IAvailabilityService {

    private val availabilityDetailsDAL: IAvailabityDetailsDAL
    private val notificationPusher: FirebaseNotificationPusher
    private val observableDAL: IObservableDAL
    private val visibilityRestrictionDAL: IVisibilityRestrictionDAL
    private val subscriptionService: ISubscriptionService

    @Autowired
    constructor(_availabilityDetailsDAL: IAvailabityDetailsDAL, _notificationPusher: FirebaseNotificationPusher,
                _observableDAL: IObservableDAL, _visibilityRestrictionDAL: IVisibilityRestrictionDAL,
                _subscriptionService: ISubscriptionService) {
        availabilityDetailsDAL = _availabilityDetailsDAL
        notificationPusher = _notificationPusher
        observableDAL = _observableDAL
        visibilityRestrictionDAL = _visibilityRestrictionDAL
        subscriptionService = _subscriptionService
    }

    override fun setUnavailable(observableId: Int) {
        availabilityDetailsDAL.setUnavailable(observableId)
        val observable = observableDAL.getObservableById(observableId)
    }

    override fun setAvailable(observableId: Int) {
        availabilityDetailsDAL.setAvailable(observableId)
        val observable = observableDAL.getObservableById(observableId)
        CompletableFuture.runAsync {
            performNotificationRoutine(observable, ObservableAvailableNotificationDTO(observable.id, observable.name, observable.avatar_url), Constants.BECAME_AVAILABLE_NOTIF_TITLE)
        }
    }

    override fun getAvailability(observableId: Int): AvailabilityDetail {
        return availabilityDetailsDAL.getAvailability(observableId)
    }

    private fun handleFcmSendResponse(fcmMsgResp: FcmMessageResponse) {
        // Handle the response here
    }

    private fun performNotificationRoutine(observable: Observable, notificationDTO: BaseNotificationDTO, messageTitle: String) {
        val observersWithVisibility = observableDAL.getAllObserversOfObservableById(observable.id)
        val observersToReceiveNotification = observersWithVisibility.filter {
            ObservableHelper.filterObserversWithNoRestriction(it, observable.id, visibilityRestrictionDAL)
                    .and(ObservableHelper.filterObserversWithSubscriptionsAbleForNotification(observable.id, it, subscriptionService))
        }
        if (observersToReceiveNotification.isEmpty()) return

        val observersRegistrationTokensForNotification = observersToReceiveNotification.map(Observer::registration_Token).filterNotNull()
                .toTypedArray()

        // If the observers don't have defined a token
        if (observersRegistrationTokensForNotification.isEmpty()) return

        val options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofHours(1))
                .build()

        notificationPusher.sendMessage(observersRegistrationTokensForNotification, messageTitle, notificationDTO,
                options, this::handleFcmSendResponse)
    }
}