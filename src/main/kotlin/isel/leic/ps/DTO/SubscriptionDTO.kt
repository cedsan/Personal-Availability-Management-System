package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Represents the base class of the hierarchy of the SubscriptionDTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open abstract class SubscriptionDTO(val type: String)