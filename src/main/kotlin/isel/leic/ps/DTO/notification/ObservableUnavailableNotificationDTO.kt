package isel.leic.ps.DTO.notification

import isel.leic.ps.APINotification.Constants


data class ObservableUnavailableNotificationDTO (val observable_id: Int = 0, val observable_name: String, val observable_avatar_url: String): BaseNotificationDTO(Constants.BECAME_UNAVAILABLE_NOTIF_CODE) {
}