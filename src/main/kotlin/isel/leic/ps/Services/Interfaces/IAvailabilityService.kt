package isel.leic.ps.Services.Interfaces

import isel.leic.ps.Entities.AvailabilityDetail


interface IAvailabilityService {
    fun setUnavailable (observableId: Int)
    fun setAvailable (observableId: Int)
    fun getAvailability(observableId: Int): AvailabilityDetail
}