package isel.leic.ps.Services.Interfaces

import isel.leic.ps.DTO.UserDTO
import isel.leic.ps.DTO.UserInfoDto
import isel.leic.ps.Domain.User.UserType


interface IUserService {
    fun getUserType(userMail: String): UserInfoDto?
    fun registerNewUser(userDto: UserDTO)
}