package isel.leic.ps.DTO.notification

import isel.leic.ps.APINotification.Constants


data class ObservableAvailableNotificationDTO(val observable_id: Int = 0, val observable_name: String, val observable_avatar_url: String): BaseNotificationDTO(Constants.BECAME_AVAILABLE_NOTIF_CODE) {
}