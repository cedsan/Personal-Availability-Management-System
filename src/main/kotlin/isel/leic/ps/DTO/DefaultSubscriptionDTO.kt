package isel.leic.ps.DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class DefaultSubscriptionDTO(subscriptionType: String): SubscriptionDTO(subscriptionType)