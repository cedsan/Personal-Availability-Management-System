package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(entityClass = arrayOf("collection", "observer"))
class ObserverCollectionOutputModel: BaseResource {

    @Siren4JProperty
    val observers: Collection<Entity>

    constructor(_observers: Collection<ObserverOM>) {
        observers = _observers.map(ReflectingConverter.newInstance()::toEntity)
    }
}