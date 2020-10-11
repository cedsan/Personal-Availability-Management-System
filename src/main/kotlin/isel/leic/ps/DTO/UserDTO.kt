package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserDTO(val username: String = "", val password: String = "",
                   val name: String = "", val userType: String = "", val number: Int = 0, val avatarUrl: String = "")