package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.annotations.Siren4JSubEntity
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import com.google.code.siren4j.resource.BaseResource
import org.springframework.hateoas.Resource
import org.springframework.hateoas.ResourceSupport

@Siren4JEntity(entityClass = arrayOf("collection", "observable"))
class ObservableCollectionOutputModel : BaseResource {

    @Siren4JProperty
    var observableList: Collection<Entity>

    constructor(_observableList: Collection<ObservableOM>) {
        observableList = _observableList.map(ReflectingConverter.newInstance()::toEntity)
    }
}