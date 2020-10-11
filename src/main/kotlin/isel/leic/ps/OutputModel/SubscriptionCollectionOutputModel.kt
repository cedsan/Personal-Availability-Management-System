package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(entityClass = arrayOf("collection", "subscription"))
class SubscriptionCollectionOutputModel: BaseResource {

    @Siren4JProperty
    var subscriptionList: Collection<Entity>

    constructor(_subscriptionList: Collection<SubscriptionOM>) {
        subscriptionList = _subscriptionList.map(ReflectingConverter.newInstance()::toEntity)
    }
}