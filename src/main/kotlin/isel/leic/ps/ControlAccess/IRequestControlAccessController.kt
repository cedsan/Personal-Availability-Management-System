package isel.leic.ps.ControlAccess


interface IRequestControlAccessController {
    fun hasPermissionToMakeRequest(userMail: String?, reqParams: RequestParams, userRoles: Collection<Role>): Boolean
}