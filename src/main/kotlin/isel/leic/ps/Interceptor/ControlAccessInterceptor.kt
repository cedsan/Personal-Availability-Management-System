package isel.leic.ps.Interceptor

import isel.leic.ps.ControlAccess.RequestAccessController
import isel.leic.ps.ControlAccess.RequestParams
import isel.leic.ps.ControlAccess.Role
import isel.leic.ps.DAL.Interfaces.IUserRoleDAL
import isel.leic.ps.Exception.PathNotFoundException
import isel.leic.ps.Interceptor.DTO.TokenIntrospectionResponseDTO
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * The purpose of this request interceptor handler
 * is to provide access control to the received requests
 */
class ControlAccessInterceptor: HandlerInterceptor {

    val hasPermissionToMakeRequest: (String?, RequestParams, Collection<Role>) -> Boolean

    val users_roleDAL: IUserRoleDAL

    constructor(_users_roleDAL: IUserRoleDAL, _hasPermissionToMakeRequest: (String?, RequestParams, Collection<Role>) -> Boolean) {
        hasPermissionToMakeRequest = _hasPermissionToMakeRequest
        users_roleDAL = _users_roleDAL
    }

    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        val method = request!!.method.toString()
        if (method.equals(RequestMethod.OPTIONS.toString()))
            return true

        val queryString = request.queryString
        var path = if(queryString.isNullOrEmpty()) request.servletPath else String.format("%s?%s", request.servletPath, queryString)

        var accessToken: String? = request.getHeader("authorization")
        accessToken = accessToken?.removePrefix("Bearer ")

        var isReqAuthenticated = false

        var tokenIntrospectionDetails = TokenIntrospectionResponseDTO()



        accessToken?.apply {
            tokenIntrospectionDetails = ControlAccessHelper.getTokenIntrospectionDetails(accessToken!!)
            isReqAuthenticated = tokenIntrospectionDetails.active
            // If the access token is not null and the token is active in the AS
            // the request is authenticated
        }

        // Get the user roles
        val userRoles: Collection<Role> =
                if(isReqAuthenticated) {
                    users_roleDAL.getUserRoles(tokenIntrospectionDetails.user_id!!)
                } else arrayListOf<Role>(Role.NONE)

        try {
            if (hasPermissionToMakeRequest(tokenIntrospectionDetails?.user_id, RequestParams(HttpMethod.valueOf(method), path), userRoles))
                return true // And pass the request to the path handler
            else {
                response!!.status = HttpStatus.UNAUTHORIZED.value()
                return false
            }
        } catch (pnfe: PathNotFoundException) {
            response!!.status = HttpStatus.NOT_FOUND.value()
            return false
        }

    }



    override fun postHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?, modelAndView: ModelAndView?) { }
    override fun afterCompletion(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?, ex: Exception?) { }
}