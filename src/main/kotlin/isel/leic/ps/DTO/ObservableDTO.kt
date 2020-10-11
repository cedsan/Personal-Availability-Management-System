package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ObservableDTO(val name: String = "",
                         val number: Int = 0,
                         val email: String = "",
                         val avatar_url: String = ""){
}