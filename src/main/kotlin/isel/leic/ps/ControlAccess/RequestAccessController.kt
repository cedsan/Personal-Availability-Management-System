package isel.leic.ps.ControlAccess

import isel.leic.ps.Entities.RequestControlAccessParams
import isel.leic.ps.Exception.PathNotFoundException
import isel.leic.ps.Services.Interfaces.IObservableService
import isel.leic.ps.Services.Interfaces.IObserverService
import org.springframework.http.HttpMethod
import java.util.regex.Pattern

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines an implementation to check if a client
 * has access to a given resource based on the
 * path and the roles necessary to access each (path, HttpMethod) pair.
 * When the request is not a safe method, additionally
 * the resource owner is verified.
 */
class RequestAccessController(val requestCtrlAccessParams: Collection<RequestControlAccessParams>, val observableService: IObservableService, val observerService: IObserverService): IRequestControlAccessController{

    companion object {
        private val ID_POSITION_IN_PATH = 2
    }

    override fun hasPermissionToMakeRequest(userMail: String?, reqParams: RequestParams, userRoles: Collection<Role>): Boolean {
        requestCtrlAccessParams.forEach {
            val pathMatchesPattern = reqParams.path.matches(Pattern.compile(it.regex).toRegex())
            if ((it.method.equals(reqParams.method.toString())).and(pathMatchesPattern)) {
                for (role: Role in userRoles) {
                    if (it.accessible_roles.contains(Role.NONE.name)) return true
                    if ((it.accessible_roles.contains(role.name))) {
                        if (reqParams.method.isSafeMethod()) return true
                        else return isResourceRequesterIsResourceOwner(userMail!!, reqParams, it.accessible_roles.map(Role::valueOf))
                    }
                }
                // It should only match one regular expression
                return false
            }
        }

        // If went through all of the PATHs and none matched return false
        throw PathNotFoundException()
    }

    private fun isResourceRequesterIsResourceOwner(userMail: String, reqParams: RequestParams, userRoles: Collection<Role>): Boolean {
        val pathParts = reqParams.path.split('/')
        val resourceOwnerId: Int
        try {
            resourceOwnerId = pathParts[ID_POSITION_IN_PATH].toInt()
        } catch (e:NumberFormatException) {
            // If path does not have an id the operation
            // must be done by and admin so return true
            return true
        }

        // Normally if this operation is a PUT or POST or DELETE should
        // be only in role type for the specific type of user to make it
        // so if one is null it must be on the other type (This has to be
        // maintained until the Observer and Observable are joined
        if(pathParts[1].equals("observable")) {
            observableService.getObservableByEmail(userMail)?.let {
                return it.id == resourceOwnerId
            }
        } else {
            observerService.getObserverByEmail(userMail)?.let {
                return it.id == resourceOwnerId
            }
        }
        return false
    }

    private fun HttpMethod.isSafeMethod(): Boolean {
        val unsafeMethodSet: HashSet<HttpMethod> = HashSet(arrayListOf(HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.POST))
        return unsafeMethodSet.contains(this).not()
    }
}