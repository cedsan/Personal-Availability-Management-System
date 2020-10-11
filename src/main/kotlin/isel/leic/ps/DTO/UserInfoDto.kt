package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoDto(val userType: String = "", val userId: Int = 0)