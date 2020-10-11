package isel.leic.ps

import de.bytefish.fcmjava.http.options.IFcmClientSettings
import de.bytefish.fcmjava.model.options.FcmMessageOptions
import isel.leic.ps.APINotification.FirebaseNotificationPusher
import isel.leic.ps.DAL.DefaultSubscriptionDBDAL
import isel.leic.ps.DAL.Interfaces.IScheduleDAL
import isel.leic.ps.DAL.Interfaces.ISubscriptionDAL
import isel.leic.ps.DAL.WeekDaySubscriptionDBDAL
import isel.leic.ps.DAL.WeeklyScheduleDBDAL
import isel.leic.ps.DTO.notification.ObservableAvailableNotificationDTO
import isel.leic.ps.Domain.Schedule.ScheduleEnum
import isel.leic.ps.Domain.Subscription.SubscriptionEnum
import isel.leic.ps.Register.DAL.IIDProviderUserCreater
import isel.leic.ps.Register.DAL.MitreIdUserCreater
import isel.leic.ps.Register.DataSource.IdProviderUserDbDataSourceTransactionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ImportResource
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.time.Duration
import java.util.regex.Pattern

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * The class defining the entry point for our application.
 */
@SpringBootApplication
@ImportResource("applicationContext.xml")
open class SgdlApplication {
    companion object {
        val FCM_CONNECTION_SERVER_ENDPOINT = "https://fcm.googleapis.com/fcm/send"
        val FCM_API_KEY = "AAAArGlBJ5A:APA91bFEJd3hm1DoP-QMBr40rRcKH7v_H0rw7O9yKx1gvuea7o67qyj_b8Ha_ru8AkDq82FsaV1VUEaybMVNDtthoB5qCd--eoLeVAa5NJtMY2C-0UXCqyPW-RA-pFtNmnKwhR8kZIJH"

        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(SgdlApplication::class.java, *args)
        }
    }

    /********** Bean Configurations **********/

    @Bean
    open fun getFirebaseNotificationPusher(): FirebaseNotificationPusher {
        return FirebaseNotificationPusher(object: IFcmClientSettings {
            override fun getApiKey(): String = FCM_API_KEY

            override fun getFcmUrl(): String = FCM_CONNECTION_SERVER_ENDPOINT
        })
    }

    @Bean
    open fun getSubscriptionEnumToDALMapper(@Autowired dataSourceTransactionManager : DataSourceTransactionManager)
            : HashMap<SubscriptionEnum, ISubscriptionDAL> {
        val map: HashMap<SubscriptionEnum, ISubscriptionDAL> = HashMap()
        map.put(SubscriptionEnum.WEEKDAY, WeekDaySubscriptionDBDAL(dataSourceTransactionManager))
        map.put(SubscriptionEnum.DEFAULT, DefaultSubscriptionDBDAL(dataSourceTransactionManager))
        return map
    }

    @Bean
    open fun getScheduleEnumToDALMapper(@Autowired dataSourceTransactionManager : DataSourceTransactionManager): HashMap<ScheduleEnum, IScheduleDAL> {
        val map: HashMap<ScheduleEnum, IScheduleDAL> = HashMap()
        map.put(ScheduleEnum.WEEKDAY, WeeklyScheduleDBDAL(dataSourceTransactionManager))
        return map
    }

    @Bean
    open fun getIdDBProviderUserCreater(): IIDProviderUserCreater {
        return MitreIdUserCreater(IdProviderUserDbDataSourceTransactionManager(DriverManagerDataSource("jdbc:postgresql://localhost:5432/oic", System.getenv("OIDC_USR"), System.getenv("OIDC_PSW"))))
    }
}
