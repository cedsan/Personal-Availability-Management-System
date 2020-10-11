package isel.leic.ps.OutputModel

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.BaseResource
import org.springframework.hateoas.ResourceSupport

@Siren4JEntity(name = "observable")
class ObservableOM : BaseResource {

    val id: Int
    val number: Int
    val name:String
    val email: String
    val avatar_url: String
    val hasAvailability: Boolean

    @JsonCreator
    constructor(@JsonProperty("id") id: Int,@JsonProperty("number") number: Int,
                @JsonProperty("name") name: String, @JsonProperty("email") email: String,
                @JsonProperty("avatar_url") avatar_url: String, @JsonProperty("hasAvailability") hasAvailability: Boolean) {
        this.id = id
        this.name = name
        this.number = number
        this.email = email
        this.avatar_url = avatar_url
        this.hasAvailability = hasAvailability
    }
}