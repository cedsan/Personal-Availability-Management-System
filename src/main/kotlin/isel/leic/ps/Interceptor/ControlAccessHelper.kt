package isel.leic.ps.Interceptor

import isel.leic.ps.Interceptor.DTO.TokenIntrospectionResponseDTO
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

/**
 * Consists of a class that defines a simple helper method
 * that performs a request to the configured AS introspection endpoint
 * in order to obtain information of the user sending the access token
 */
class ControlAccessHelper {

    companion object {
        fun getTokenIntrospectionDetails(accessToken: String): TokenIntrospectionResponseDTO {
            val headers = LinkedMultiValueMap<String, String>()
            //headers.add("Authorization", "Basic ZmVjOTk0MWEtOWU0NS00MzgyLWEzODMtNzU0NzRiMjZjZmQwOkFKdGM3UVNUcHZ1UXJsNkllWnZQR09qSnlzNlBqOUN6T2xjVHlNR1FuUG9TS292ZTBKSW1TVURURF9fV2pOWTk0V2JJN1o5MlUwRUU4WHljVlRDRmxNUQ==") //Cedric
            //headers.add("Authorization", "Basic ZDk2MThlMmYtNDRhYS00MjY0LWExYzYtNjUxYzg3OTlmZTdjDQpYU1Fqcmk3NWZZZUxDdDZ6azN5bVRkQzdzU25jY2toTG5aaTAxT1JIb2hJZnBLellKMG1KU3BVRUlYV2JHQkYtU3NGNmt5bzRIb2MtckxrOXhlU29Udw==") //Neves
            headers.add("Authorization", "Basic ZDk2MThlMmYtNDRhYS00MjY0LWExYzYtNjUxYzg3OTlmZTdjOlhTUWpyaTc1ZlllTEN0NnprM3ltVGRDN3NTbmNja2hMblppMDFPUkhvaElmcEt6WUowbUpTcFVFSVhXYkdCRi1Tc0Y2a3lvNEhvYy1yTGs5eGVTb1R3") //Neves
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE)
            headers.add("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            val body = String.format("token=%s&token_type_hint=access_token", accessToken)

            val entity = HttpEntity<String>(body, headers)
            val res = RestTemplate().exchange("http://localhost:8080/openid-connect-server-webapp/introspect", HttpMethod.POST, entity, TokenIntrospectionResponseDTO::class.java).body
            return res
        }
    }
}