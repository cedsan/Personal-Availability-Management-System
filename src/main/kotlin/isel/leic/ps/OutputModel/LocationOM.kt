package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.annotations.Siren4JProperty
import com.google.code.siren4j.resource.BaseResource
import java.sql.Timestamp

@Siren4JEntity(name = "location")
class LocationOM: BaseResource {

    val id: Int
    val latitude: Double
    val longitude: Double
    @Siren4JProperty
    val lastUpdatedDate: Timestamp

    constructor(_id: Int,_latitude: Double, _longitude: Double, _lastUpdatedDate: Timestamp) {
        id = _id
        latitude = _latitude
        longitude = _longitude
        lastUpdatedDate = _lastUpdatedDate
    }
}