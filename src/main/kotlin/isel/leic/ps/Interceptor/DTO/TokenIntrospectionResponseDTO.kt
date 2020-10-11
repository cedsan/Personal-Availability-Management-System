package isel.leic.ps.Interceptor.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TokenIntrospectionResponseDTO(val active: Boolean = false, var user_id: String? = null)