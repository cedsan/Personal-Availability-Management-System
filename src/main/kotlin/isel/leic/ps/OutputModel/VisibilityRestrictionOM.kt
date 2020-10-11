package isel.leic.ps.OutputModel

import com.google.code.siren4j.annotations.Siren4JEntity
import com.google.code.siren4j.resource.BaseResource

@Siren4JEntity(name = "visibility_restriction")
class VisibilityRestrictionOM(var canSeeWhenAvailable: Boolean,
                              var canSeeSchedule: Boolean,
                              var canSeeLocation: Boolean): BaseResource() {
}