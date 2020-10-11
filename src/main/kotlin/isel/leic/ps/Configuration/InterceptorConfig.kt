package isel.leic.ps.Configuration

import isel.leic.ps.ControlAccess.RequestAccessController
import isel.leic.ps.ControlAccess.Role
import isel.leic.ps.DAL.Interfaces.IRequestControlAccessParamsDAL
import isel.leic.ps.DAL.Interfaces.IUserRoleDAL
import isel.leic.ps.DAL.RequestControlAccessParamsDAL
import isel.leic.ps.Entities.RequestControlAccessParams
import isel.leic.ps.Interceptor.ControlAccessInterceptor
import isel.leic.ps.Services.Interfaces.IObservableService
import isel.leic.ps.Services.Interfaces.IObserverService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Configuration class used to configure the interceptor
 * that will be responsible for handling requests.
 */
@Configuration
open class InterceptorConfig: WebMvcConfigurerAdapter() {

    @Autowired
    lateinit var userRoleDAL: IUserRoleDAL

    @Autowired
    lateinit var requestControlAccessParamsDAL: IRequestControlAccessParamsDAL

    @Autowired
    lateinit var observerService: IObserverService

    @Autowired
    lateinit var observableService: IObservableService

    override fun addInterceptors(registry: InterceptorRegistry?) {
        super.addInterceptors(registry)
        registry!!.addInterceptor(ControlAccessInterceptor(userRoleDAL, { userMail, reqParams, roles -> RequestAccessController(requestControlAccessParamsDAL.getAll(), observableService, observerService).hasPermissionToMakeRequest(userMail, reqParams, roles) }))
    }
}