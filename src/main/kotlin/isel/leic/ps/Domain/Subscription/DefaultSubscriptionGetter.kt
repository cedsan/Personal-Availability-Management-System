package isel.leic.ps.Domain.Subscription

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import isel.leic.ps.DTO.DefaultSubscriptionDTO
import isel.leic.ps.DTO.SubscriptionDTO

/**
 * Instituto Superior de Engenharia Lisboa
 * Project : Personal Availability Management System by Geographic Location
 * Authors : Ronilton Neves Nº 39643 & Cedric Santos Nº 40978
 * Mentors : Pedro Félix & Luís Falcão
 *
 * Defines implementations of the operations access Schedule
 * entities from the used DataSource
 */
class DefaultSubscriptionGetter: ISubscriptionGetter {
    override fun getSubscription(body: String): SubscriptionDTO {
        var om: ObjectMapper = ObjectMapper()
        var jn : JsonNode = om.readTree(body)
        var type = jn["type"].asText()
        return DefaultSubscriptionDTO(type)
    }
}