package isel.leic.ps.Services

import isel.leic.ps.ControlAccess.Role
import isel.leic.ps.DAL.Interfaces.IObservableDAL
import isel.leic.ps.DAL.Interfaces.IObserverDAL
import isel.leic.ps.DAL.Interfaces.IUserDAL
import isel.leic.ps.DTO.ObservableDTO
import isel.leic.ps.DTO.ObserverDTO
import isel.leic.ps.DTO.UserDTO
import isel.leic.ps.DTO.UserInfoDto
import isel.leic.ps.Domain.User.UserType
import isel.leic.ps.Exception.UserAlreadyExistsException
import isel.leic.ps.Register.DAL.IIDProviderUserCreater
import isel.leic.ps.Register.DAL.MitreIdUserCreater
import isel.leic.ps.Register.DataSource.IdProviderUserDbDataSourceTransactionManager
import isel.leic.ps.Services.Interfaces.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Service

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Specifies the business layer operations for the User resource.
 */
@Service
class UserService: IUserService {

    @Autowired
    lateinit var userDAL: IUserDAL

    @Autowired
    lateinit var observableDAL: IObservableDAL

    @Autowired
    lateinit var observerDAL: IObserverDAL

    @Autowired
    lateinit var dbProviderUserCreater: IIDProviderUserCreater

    override fun getUserType(userMail: String): UserInfoDto? {
        return userDAL.getUserType(userMail)
    }

    override fun registerNewUser(userDto: UserDTO) {
        if(userDAL.hasUserWithEmail(userDto.username))
            throw UserAlreadyExistsException()
        // Add the user to the
        dbProviderUserCreater.createUser(userDto.username, userDto.password, userDto.name)

        // Now create the corresponding entity in the system according if Observer or Observable
        val role = Role.valueOf(userDto.userType.toUpperCase())
        when(role) {
            Role.OBSERVER -> {
                observerDAL.createObserver(ObserverDTO(userDto.name, userDto.number, userDto.username, userDto.avatarUrl))
            }
            Role.OBSERVABLE -> {
                observableDAL.createObservable(ObservableDTO(userDto.name, userDto.number, userDto.username, userDto.avatarUrl))
            }
            Role.NONE -> {}
            Role.ADMIN -> {}
        }
    }
}