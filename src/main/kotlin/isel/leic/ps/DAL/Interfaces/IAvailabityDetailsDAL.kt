package isel.leic.ps.DAL.Interfaces

import isel.leic.ps.Entities.AvailabilityDetail


interface IAvailabityDetailsDAL {
    fun setUnavailable(observableId: Int)
    fun setAvailable(observableId: Int)
    fun getAvailability(observableId: Int): AvailabilityDetail

}