package isel.leic.ps.Entities

/**
 * This class consists of the computational representation
 * of the DB GlobalVisibilityRestriction Table entities
 */
class GlobalVisibilityRestrictionConfig(var id: Int,
                                        var canSeeWhenAvailable: Boolean,
                                        var canSeeSchedule: Boolean,
                                        var canSeeLocation: Boolean) {
}