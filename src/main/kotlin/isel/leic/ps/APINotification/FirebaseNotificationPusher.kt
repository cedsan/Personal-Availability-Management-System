package isel.leic.ps.APINotification

import de.bytefish.fcmjava.client.FcmClient
import de.bytefish.fcmjava.http.client.IFcmClient
import de.bytefish.fcmjava.http.options.IFcmClientSettings
import de.bytefish.fcmjava.model.options.FcmMessageOptions
import de.bytefish.fcmjava.requests.data.DataMulticastMessage
import de.bytefish.fcmjava.responses.FcmMessageResponse

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Class responsible sending the requests to the Firebase notification service
 * used to have notifications sent to users
 */
class FirebaseNotificationPusher {

    val fcmClient: IFcmClient

    constructor(settings: IFcmClientSettings) {
        fcmClient = FcmClient(settings)
    }

    fun sendMessage(recipients: Array<String>, messageTitle: String, messageContent: Any, messageOptions: FcmMessageOptions, responseHandler: (FcmMessageResponse) -> Unit): Unit {
        var dataMulticasMessage = DataMulticastMessage(messageOptions, recipients.toMutableList(), messageContent)
        var response: FcmMessageResponse? = null
        try {
            response = fcmClient.send(dataMulticasMessage)
        }catch (e: Exception) {
            // We should do something with the exception
            var x = 1
        }

        response?.let {
            if(response!!.numberOfFailure > 0) {
                throw IllegalArgumentException("Unable to send notification")
            }
        }
    }

}