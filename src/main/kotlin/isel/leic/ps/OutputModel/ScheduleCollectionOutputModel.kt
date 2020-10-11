package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.component.Entity
import com.google.code.siren4j.converter.ReflectingConverter
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(entityClass = arrayOf("collection", "schedule"))
class ScheduleCollectionOutputModel: BaseResource {

    @Siren4JProperty
    var scheduleList: Collection<Entity>

    constructor(_scheduleList: Collection<ScheduleOM>) {
        scheduleList = _scheduleList.map(ReflectingConverter.newInstance()::toEntity)
    }
}