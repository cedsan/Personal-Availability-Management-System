package isel.leic.ps.OutputModel

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JFieldOption
import com.google.code.siren4j.annotations.Siren4JPropertyIgnore
import com.google.code.siren4j.annotations.Siren4JSubEntity
import com.google.code.siren4j.component.Action
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.component.Link
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(name = "observer")
class ObserverOM: BaseResource {

    val id: Int
    val name:String
    val number: Int
    val email: String
    val avatar_url: String

    //@Siren4JSubEntity(uri = "localhost:8080/observer/{parent.number}/observables")
    //var observables: ObservedCollectionOutputModel? = null

    @Siren4JSubEntity()
    var observables: Collection<ObservableOM>? = null
    //var observables: ObservedCollectionOutputModel? = null

    @JsonCreator
    constructor(@JsonProperty("id") id: Int, @JsonProperty("name") name: String, @JsonProperty("number") number: Int,
                @JsonProperty("email") email: String, @JsonProperty("avatar_url") avatar_url: String) {
        this.id = id
        this.name = name
        this.number = number
        this.email = email
        this.avatar_url = avatar_url
    }
}