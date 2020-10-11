package isel.leic.ps.APINotification

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * This class defines some constants associated with
 * the notification service.
 */
class Constants {

    companion object {
        // Notification types
        val BECAME_AVAILABLE_NOTIF_CODE   = 0
        val BECAME_UNAVAILABLE_NOTIF_CODE = 1

        // Notification messages
        val BECAME_AVAILABLE_NOTIF_TITLE   = "Observable became available"
    }
}