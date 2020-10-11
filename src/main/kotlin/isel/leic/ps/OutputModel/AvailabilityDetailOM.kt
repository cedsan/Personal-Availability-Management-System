package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(name = "availability_detail")
class AvailabilityDetailOM(val hasAvailability: Boolean): BaseResource()