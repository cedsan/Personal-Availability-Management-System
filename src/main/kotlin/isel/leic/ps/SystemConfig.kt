package isel.leic.ps

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Contains the constants used to configure general service configurations.
 */
class SystemConfig {
    companion object {
        val HOST_HOME = String.format("http://%s", System.getenv("HOST_ADDR"))
        val AUTHORIZATION_SERVER_CRED_BASIC = System.getenv("AUTH_SERVER_CREDENTIALS_TOKEN")
        val AUTHORIZAION_SERVER_HOST_ADDR = System.getenv("AUTH_SERVER_HOST_ADDR")
        val FCM_API_KEY = System.getenv("FCM_API_KEY")
    }
}